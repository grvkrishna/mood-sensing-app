package com.grv.moodsensingapp.config;

import com.grv.moodsensingapp.config.jwt.JwtTokenAuthenticationFilter;
import com.grv.moodsensingapp.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Configuration
public class SpringSecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
//    @Bean
//    public InMemoryUserDetailsManager userDetailsManager(){
//        UserDetails userDetails = User.withDefaultPasswordEncoder()
//                .username("testadmin")
//                .password("testpassword")
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(userDetails);
//    }
//
//
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .authorizeHttpRequests((auth) -> auth
//                        .anyRequest()
//                        .authenticated()
//                )
//                .httpBasic();
//
//        return httpSecurity.build();
//    }


    @Bean
    SecurityFilterChain springWebFilterChain(HttpSecurity http,
                                             JwtTokenProvider tokenProvider) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(c -> c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/v1/auth/signin").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/mood/**").hasAnyRole("REPORT","ADMIN")
                        .requestMatchers("v1/mood/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtTokenAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    UserDetailsService customUserDetailsService() {
//        return (username) -> users.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found"));
        UserDetails userDetails = User.builder()
                .username("testadmin")
                .password("testpassword")
                .roles("ADMIN")
                .build();
        UserDetails reportUserDetails = User.builder()
                .username("testreport")
                .password("testpassword")
                .roles("REPORT")
                .build();
        Map<String,UserDetails> userDetailsList = new HashMap<>();
        userDetailsList.put("testadmin",userDetails);
        userDetailsList.put("testreport",reportUserDetails);
        try {
            return (username) -> userDetailsList.get(username);
        } catch (NullPointerException n){
            throw  new UsernameNotFoundException("Username:  not found");
        }
    }

    @Bean
    AuthenticationManager customAuthenticationManager(UserDetailsService userDetailsService) {
        return authentication -> {
            String username = authentication.getPrincipal() + "";
            String password = authentication.getCredentials() + "";
            UserDetails user = userDetailsService.loadUserByUsername(username);
            try {

                if (!password.equals(user.getPassword())) {
                    throw new BadCredentialsException("Bad credentials");
                }

                if (!user.isEnabled()) {
                    throw new DisabledException("User account is not active");
                }
            } catch (NullPointerException n){
                throw new UsernameNotFoundException("User account not found");
            }
            return new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
        };
    }
}

# Mood Sensing App


## Background:

We are creating backend for mood sensing app, here we will have user ,mood and location of user.

## Stack :

    Spring boot.
    Mongo db.
    java 17

## Data Model:

```json
{
  "user_name": "",
  "mood": "",
  "location": {
        "type": "points",
        "coordinates": [
            longitude,
            latitude
          ]
      }
}
   ```
<!-- TOC -->
    User Name : Required a nd alphanumeric.
    Mood:  required and enum value {happy, sad, neutral}
    Location : required and geolocation of user while uploading mood. Need to create index in mongo with “2dsphere“.
<!-- TOC -->


## Low level spec:

| Operation                                                                    | Note |
|------------------------------------------------------------------------------|------|
| Upload a mood given for user and location.                                   |  Store user information with mood and location in database.    |
| Return mood frequency for given user.                                        |    This end point will return user’s mood counts with respective of all moods.
|
| Given user’s current location , return closest location where user is happy. | With input of user and location return closes location where user is happy.



## API Details:
<!-- TOC -->

## 1. Authentication Request:
    Request Type: POST 
    URL : /v1/auth/signin
    Payload :
               {
                    "username" : "",
                    "password" : ""
                }

    Response Status : 201
    Respnse Body : {
                        "username": "",
                        "token": ""
                    }

 ## 2. Store mood:
    Request Type: POST 
    URL : /v1/mood/
    Payload :
               {
                    "name": "",
                    "mood": "",
                    "longitude" : "",
                    "latitude" : ""
                }

    Response Status : 201
    Respnse Body : {
                        "name": "",
                        "mood": "",
                        "longitude" : "",
                        "latitude" : ""
                    }
        
## 3. Get Mood frequency:
    Request Type: GET
    URL : /v1/mood/{name}
    Request Body: 
    Response Status : 200
    Response Body: {
                        "Sad": count,
                        "Happy": count,
                        "Neutral": count
                    }

## 4. Get Nearest Happy location:
    Request Type : GET
    URL: /v1/mood/{name}/{longitude}/{latitude}
    Request Body: 
    Respnse Status: 200
    Response Body: {
                        "name": "gaurav",
                        "mood": "happy",
                        "longitude": 77.0,
                        "latitude": 77.0
                    }
<!-- TOC -->

# Steps to run this Application
This project is based on java with maven build. 
## 1. Import project into IDE. (Built in Intellij)

## 2. Java 17  used.

## 3. MongoDb Used for backend
    Need to set mongo properties
    spring.data.mongodb.host=XXX.XXX.XXX.XXX
    spring.data.mongodb.port=27017
    spring.data.mongodb.database=mood-sensing
    spring.data.mongodb.username=
    spring.data.mongodb.password=
    spring.data.mongodb.auto-index-creation=true

## 4. Need to set Index into MongoDbCollection
    I have used geo index in mongo db for storing location , so we required to create create index for that.
    db.mood_details.createIndex( { location : "2dsphere" } )
    I have also used PostContruct anotation on a method which will create index auto on start of project

## 5. I have assumed two username and password.
    username: testadmin
    password: testpassword
    role: Admin
  
    username: testreport
    password: testpassword
    role: report

## 6. Roles
    Report: Report user has only access of GET.
    ADMIN: Admin user has access of all api.

## 7. Run in production mode
    ## Maven Build Option Command Line
    cd /directory/
    mvn clean
    mvn install
    
    cd target/
    java -jar mood-sensing-app-0.0.1-SNAPSHOT.jar --server.port=8080
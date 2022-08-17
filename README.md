# relay_sensor_stream_mongodb
## Modules
 - iot-sensor-consumer - cloud stream consumer to consume stream data and stores in Mongo db.
 - iot-sensor-model - provides model and Mongo repository.
 - iot-services-api - It provides api to get minimum,maximum,average or median value of stored date.
 
**Application flow**

![image](https://user-images.githubusercontent.com/48526042/184707694-a990b9ef-bf99-48f1-bbf4-fa0e67a53af7.png)

 ## Technologies
 - Maven
 - Java 11 
 - JWT
 - Kafka 
 - MongoDB
 - Spring cloud stream

 ## Requirements
 - Docker
 - MongoDB
 - Sensor event producer  https://github.com/operations-relay42/iot-producer-simulator-api 
 - Java 11

 ## Running with Docker 
- Install docker: https://docs.docker.com/engine/install/
- Go to project root folder and run the docker compose file

    docker-compose -f ./docker-compose.yml up --build -d

It will start:
    Mongodb,
    Sensor event consumer and 
    Sensor data query API

The IOT consumer starts on docker at localhost:8081 using the default profile. The sensor data API starts on docker at localhost:8082 using the default profile.

## Limitation
 - The startDateTime and endDateTime should be in YYYY-MM-DD or YYYY/MM/DD format now.This should be imporoved to support more formats.
 - In case of any failure, current application returning technical error message to user which should be improved.
 
## API URL and response
The API specification in [JSON](https://github.com/ananthanperiyasamy/relay_sensor_stream_mongodb/blob/main/reference/iot-api.json) and in [YAML](https://github.com/ananthanperiyasamy/relay_sensor_stream_mongodb/blob/main/reference/openapi.yaml) 

**Create/Registor user**

If user account already created then call /authenticate API directly else create a account first.
The sample request to create new user
```curlrc
curl --location --request POST 'http://localhost:8082/create/user' \
--header 'Content-Type: application/json' \
--data-raw '[
   {
    "username": "ananthan",
    "password" : "password"
 }
]'
````
![image](https://user-images.githubusercontent.com/48526042/184656892-c8f96b53-7784-4cfe-bb65-56551a8b5fa7.png)

**Authenticate**

Hit authenticate service with user account and passowrd to get JWT token which should be attached in all other request's header.The sample request looks,
```curlrc
curl --location --request POST 'http://localhost:8082/authenticate' \
--header 'Content-Type: application/json' \
--data-raw '[
   {
    "username": "ananthan",
    "password" : "password"
 }
]'
````
![image](https://user-images.githubusercontent.com/48526042/184657010-0e035428-0b3b-46b2-b8f6-dc993286cec5.png)
Without JWT token could not access any of below APIs.User will get 401 error 
![image](https://user-images.githubusercontent.com/48526042/184964056-ebcee2e3-d684-412a-94ba-6cbc8025152a.png)

**Operation to find Minimum**
```curlrc
curl --location --request GET 'http://localhost:8082/query/min?startDateTime=2022-01-14&endDateTime=2022-08-14&eventType=HUMIDITY&clusterId=1'
--header "Authorization: Bearer << TOKEN FROM AUTHENTICATE API>>"
````
![image](https://user-images.githubusercontent.com/48526042/184963516-68ee0795-012d-4390-8a14-bf0db3b5c06d.png)

**Operation to find Maximum**
```curlrc
curl --location --request GET 'http://localhost:8082/query/mac?startDateTime=2022-01-14&endDateTime=2022-08-14&eventType=HUMIDITY&clusterId=1'
--header "Authorization: Bearer << TOKEN FROM AUTHENTICATE API>>"
````
![image](https://user-images.githubusercontent.com/48526042/184963422-41a746f9-6c3f-4d9c-899a-09c0a3c5c320.png)

**Operation to find Middle**
```curlrc
curl --location --request GET 'http://localhost:8082/query/median?startDateTime=2022-01-14&endDateTime=2022-08-14&eventType=HUMIDITY&clusterId=1'
--header "Authorization: Bearer << TOKEN FROM AUTHENTICATE API>>"
````
![image](https://user-images.githubusercontent.com/48526042/184963613-8c625021-f364-4b0a-935f-ed04388a51b6.png)

**Operation to find Average**
```curlrc
curl --location --request GET 'http://localhost:8082/query/average?startDateTime=2022-01-14&endDateTime=2022-08-14&eventType=HUMIDITY&clusterId=1'
--header "Authorization: Bearer << TOKEN FROM AUTHENTICATE API>>"
````
![image](https://user-images.githubusercontent.com/48526042/184963726-b8bf966f-c85c-4876-b252-1f80d9cf901c.png)



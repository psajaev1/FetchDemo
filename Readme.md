## FetchDemo
Demo for Fetch coding assessment. This test was done using Spring boot and gradle. Dockerfile is provided for running this application locally. 

This API has two endpoints 
1. POST `/receipts/process`
2. GET `/receipts/{id}/points`

# Running the application 
Clone the directory and enter the root level 
1. `./gradlew build` -- this command will generate the JAR that the dockerfile uses
2. `docker image build -t fetch-demo .` -- creates a docker image called fetch-demo using the generated JAR
3. `docker container run --name fetch-demo -p 8083:8083 -d fetch-demo` -- starts a container on port 8083

After running these commands, the server will be available at localhost:8083


# Testing the application 

I was using the following CURL commands for testing this service. These examples were taken from the github test repo for this assignment.  

POST endpoint

 curl --location 'http://localhost:8083/receipts/process' \  
--header 'Accept: application/json' \  
--header 'Content-Type: application/json' \  
--data '{  
  "retailer": "Target",  
  "purchaseDate": "2022-01-01",  
  "purchaseTime": "13:01",  
  "items": [  
    {  
      "shortDescription": "Mountain Dew 12PK",  
      "price": "6.49"  
    },{  
      "shortDescription": "Emils Cheese Pizza",  
      "price": "12.25"  
    },{  
      "shortDescription": "Knorr Creamy Chicken",  
      "price": "1.26"  
    },{  
      "shortDescription": "Doritos Nacho Cheese",  
      "price": "3.35"  
    },{  
      "shortDescription": "   Klarbrunn 12-PK 12 FL OZ  ",  
      "price": "12.00"  
    }  
  ],  
  "total": "35.35"  
}  
'   

GET endpoint 
`curl --location 'http://localhost:8083/receipts/f3d78f16-2247-486f-a07d-0606aa117367/points'`

# Error Handling  

For error handling, the POST endpoint will return a custom exception with a 400 BAD_REQUEST when one of the fields is missing or improper date format
GET endpoint will return a 404 with custom exception when the receipt ID is not associated with what is stored. 
Null checks are done for all of the reciept object fields. 

# Date Validation Checks 
Validation checks for dates are done to make sure the two fields aren't null and that they are in the correct format.  
Proper format for dates should be "yyyy-MM-dd" and "HH:mm".

# In Memory Solution 
I used a in memory solution to store the generated uuid and points for a reciept. I created a map bean that gets created at runtime and will exist while the application is running.   
The map has reciept ID as the key and the points of the reciept as the value



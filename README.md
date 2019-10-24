# Money transfer Rest API

A Java RESTful API for money transfers between users accounts

### Technologies
- JAX-RS API
- H2 in memory database
- Log4j
- Jetty Container (for Test and Demo app)
- Apache HTTP Client


### How to run
```sh
mvn exec:java
```

Application starts a jetty server on localhost port 8080 An H2 in memory database initialized with some sample user and account data To view

- http://localhost:8080/user/test1
- http://localhost:8080/user/test2
- http://localhost:8080/account/1
- http://localhost:8080/account/2

### Available Services

| HTTP METHOD | PATH | USAGE |
| -----------| ------ | ------ |
| GET | /user/{userName} | get user by user name | 
| GET | /user/all | get all users | 
| PUT | /user/create | create a new user | 
| POST | /user/{userId} | update user | 
| DELETE | /user/{userId} | remove user | 
| GET | /account/{accountId} | get account by accountId | 
| GET | /account/all | get all accounts | 
| GET | /account/{accountId}/balance | get account balance by accountId | 
| PUT | /account/create | create a new account
| DELETE | /account/{accountId} | remove account by accountId | 
| PUT | /account/{accountId}/withdraw/{amount} | withdraw money from account | 
| PUT | /account/{accountId}/deposit/{amount} | deposit money to account | 
| POST | /transaction | perform transaction between 2 user accounts | 

### Http Status
- 200 OK: The request has succeeded
- 400 Bad Request: The request could not be understood by the server 
- 404 Not Found: The requested resource cannot be found
- 500 Internal Server Error: The server encountered an unexpected condition 

### Sample JSON for User and Account
##### User : 
```sh
{  
  "userName":"test1",
  "emailAddress":"test1@gmail.com"
} 
```
##### User Account: : 

```sh
{  
   "userName":"test1",
   "balance":10.0000,
   "currencyCode":"GBP"
} 
```

#### User Transaction:
```sh
{  
   "currencyCode":"EUR",
   "amount":100000.0000,
   "fromAccountId":1,
   "toAccountId":2
}
```



## Donation
If this project help you reduce time to develop, you can give me a cup of coffee :) 

[![paypal](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.me/parthsolanki49)

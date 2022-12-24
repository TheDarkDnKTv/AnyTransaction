# Test payment microservice application for AnyMind

This is implementation of POS integrated system.
As it's POS system, I've chosen PostgresSQL as database due relational abilities.

## App Specification

- Language: Java 17
- Spring boot: 3.0.0
- Build tool: Gradle 7.6
- API: GraphQL

API Endpoint is on `http://localhost:8080/graphql`

Playground Endpoint is on `http://localhost:8080/graphiql`

## Requests
Request example are available in [postman collection](AnyTransaction.postman_collection.json)

All fields are mandatory.

Since it's complicated to do hierarchical objects as input for GraphQL, I've decided to pass json object as String

### CASH ON DELIVERY example
```graphql
mutation {
  makePayment(transaction: {
    customerId: 0,
    price: "80.0",
    priceModifier: 0.99,
    paymentMethod: "CASH_ON_DELIVERY",
    datetime: "2022-12-24T20:22:00+09:00"
    additionalItem: "{\"courier\":\"YAMATO\"}"
  }) {
    finalPrice
    points
  }
}
```

### VISA example
```graphql
mutation {
  makePayment(transaction: {
    customerId: 0,
    price: "80.0",
    priceModifier: 0.99,
    paymentMethod: "VISA",
    datetime: "2022-12-24T20:22:00+09:00"
    additionalItem: "{\"last4\":\"4925\"}"
  }) {
    finalPrice
    points
  }
}
```

Since was not specified `additionalItems` for banking/cheque, I have chosen next one:

#### BANK TRANSFER `additionalItem`
```json
{
  "accountNumber": "any string",
  "accountName": "any string",
  "bankName": "any string",
  "bankAddress": "any string"
}
```

#### CHEQUE `additionalItem`
```json
{
  "chequeNumber": "any string",
  "accountName": "any string",
  "bankName": "any string",
  "bankAddress": "any string"
}
```

## Building and deploying
To run this microservice it is required to have PostgresSQL up and running with created database.

Required tools to deploy:
- Git
- Java JDK 17 (with env. variable JAVA_HOME)
- Docker

### Deploy
Open terminal in this folder and run next commands:

```bash
gradlew build
```

Then configure database URL and credentials in docker-compose.yml, after done run next command:

```bash
docker-compose up -d
```

Deploy is done.

## Interview Objectives
- [x] You have to use a database
- [x] You have to use Git
- [x] Your code has to be clear
- [x] You know good coding practices and patterns
- [x] Your API can handle incorrect data
- [x] Your architecture is extendable/testable
- [x] Your system can be executed easily
- [x] You can consider multi-thread, many servers
- [x] Your system should be tested
- [x] Your system should be able to scale with newer payment methods
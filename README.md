# Test payment microservice application for AnyMind

This is implementation of POS integrated system.

## App Specification

- Language: Java 17
- Spring boot: 3.0.0
- Build tool: Gradle 7.6
- API: GraphQL

As it's POS system, I've chosen PostgresSQL as database due relational abilities. And also PostgresSQL is pretty fast and reliable database to use, even support horizontal and vertical scaling.
In implementation, I've used next patterns: Repository, Dependency Injection. As part of SOLID principles most noticeable is Dependency Inversion principle and Interface segregation principle.

## Adding new payment methods

In case if payment calculation logic was not changed, we need just to add enum constant to [PaymentType](src/main/java/thedarkdnktv/anytransaction/domain/enums/PaymentType.java)
 and specify modifiers for min,max price and points.

In case logic changed, we need to implement new logic calculation class and change type determination logic.

## Endpoints

- [http://localhost:8080/graphql](http://localhost:8080/graphql) (API Endpoint)
- [http://localhost:8080/graphiql](http://localhost:8080/graphiql) (Playground Endpoint)


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
git clone https://github.com/TheDarkDnKTv/AnyTransaction
```

```bash
gradlew build
```

Then configure database URL and credentials in docker-compose.yml, after done run next command:

```bash
docker-compose up -d
```

Deploy is done.

In case you need more instances to connect it load-balancer, you can set `APPLICATION_ARGS` in the `docker-compose.yml` with `--server.port=<port>`

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
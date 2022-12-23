# Test payment microservice application for AnyMind

This is implementation of POS integrated system.
As it's POS system, I've chosen PostgresSQL as database due relational abilities.

## Deploy
Before deploying make sure you created PostgresSQL database.
URL of it and credentials should be specified in `application.yaml` in `src/resources` under category `datasource`

TODO: deploy instructions

## Interview Objectives
- [x] You have to use a database
- [x] You have to use Git
- [x] Your code has to be clear
- [x] You know good coding practices and patterns
- [ ] Your API can handle incorrect data
- [ ] Your architecture is extendable/testable
- [ ] Your system can be executed easily
- [ ] You can consider multi-thread, many servers
- [ ] Your system should be tested
- [ ] Your system should be able to scale with newer payment methods
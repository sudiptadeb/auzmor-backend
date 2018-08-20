# Auzmor Api

### This project provides a tomcat war with the following features

- Basic Authentication based on the username and auth_id in the dump
- Input validation for all the params
- from and to parameter validation against the data in the dump
- STOP request to a number block that number from sending your messages for 4 hours
- Rate limiting for outbound sms based on time limit and max requests

### Project structure and extra resources

- The project is bundles as a maven module and follows that directory structure
- There is a postman collection export file under `src/test/`
- The requirement specification is present in the base directory of the repo

### Testing and Running the build

- Pre-Requesites
  - maven (3.x)
  - postgres (>=8)
  - redis (>=3)
  - tomcat (>=7)
- Environment setup
  - download source form this repo
  - download and import the dump from the requirement pdf into the postgres server
  - update the `src/main/webapp/WEB-INF/config.properties` with the database and redis details
  - start the postgres and redis servers
- Steps to build
  - run `mvn package`
  - the war will be create under `target/auzmor-api.war`
  - just deploy the war file in tomcat
  - or you can use the embedded tomcat, run `mvn tomcat7:run` to start the build at http://localhost:7070
- Steps to test
  - this project uses junit5 and rest-assured
  - lower the time limits and max requests in the `src/main/webapp/WEB-INF/config.properties` to speed up the test
  - run `mvn verify` to run both the unit tests and the integration tests

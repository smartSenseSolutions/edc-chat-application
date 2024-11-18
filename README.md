# Seed Multi module demo

## Spring boot multi module boiler plate <a id="introduction"></a>

This repository contains boiler-plate codebase using Java, Spring boot as core technology.

## Developer Documentation

To run application locally, this section describes the tooling as well as
the local development setup.

## Module Information

**ss-api**          : It contains utilities like constants, dtos, validators, utility classes. This will be published as a package and this can be used by other Java projects as a depedency

**ss-dao**          : It contains database entities and repositories.

**ss-service**      : It contains abstraction of business logic.

**ss-service-impl** : It contains business login.

**ss-web**          : This is runnable spring boot application. It contains application Configs, Db-migration files, Exception handler, API definition.

## Tooling

| Area     | Tool     | Download Link                                   | Comment                                                                                           |
|----------|----------|-------------------------------------------------|---------------------------------------------------------------------------------------------------|
| IDE      | IntelliJ | https://www.jetbrains.com/idea/download/        | Additionally the [envfile plugin](https://plugins.jetbrains.com/plugin/7861-envfile) is suggested |
| Build    | Gradle   | https://gradle.org/install/                     |
| Runtime  | Docker   | https://www.docker.com/products/docker-desktop/ |                                                                                                   |
| Database | DBeaver  | https://dbeaver.io/                             |
| IAM      | Keycloak | https://www.keycloak.org/                       |                                                                                                   |

## Local Development Setup

1. Run [MainApplication.java](ss-web%2Fsrc%2Fmain%2Fjava%2Fss%2Fmod%2Fdemo%2FMainApplication.java) in IDE. It will up database and keycloak using docker compose(ref: [Spring boot support for docker compose](https://spring.io/blog/2023/06/21/docker-compose-support-in-spring-boot-3-1))
2. Open API doc on http://localhost:8080/api/app/ui/swagger-ui/index.html
3. Click on Authorize on swagger UI and on the dialog click again on Authorize.
4. Login with username=valid-user and password=password

## Build application locally

Build with test cases

```
./gradlew build
```

Build without test cases

```
./gradlew build -i -x test
```

## Test Coverage

Jacoco is used to generate the coverage report. The report generation
and the coverage verification are automatically executed after tests.

The generated HTML report can be found under `jacoco-report/html/`

To generate the report run the command

```
./gradlew testCodeCoverageReport
```

To check the coverage run the command

```
./gradlew jacocoTestCoverageVerification
```

## Common issues and solutions during local setup

#### 1. Can not build with test cases

Test cases are written using the Spring Boot integration test frameworks. These test frameworks start the Spring Boot
test context, which allows us to perform integration testing. In our tests, we utilize the Testcontainers
library (https://java.testcontainers.org/) for managing Docker containers. Specifically, we use Testcontainers to start
PostgreSQL and Keycloak Docker containers locally.

Before running the tests, please ensure that you have Docker runtime installed and that you have the necessary
permissions to run containers.

Alternative, you can skip test during the build with ``` ./gradlew clean build -x test```

#### 2. Database migration related issue

We have implemented database migration using Liquibase (https://www.liquibase.org/). Liquibase allows us to manage
database schema changes effectively.

In case you encounter any database-related issues, you can resolve them by following these steps:

1. Delete all tables from the database.
2. Restart the application.
3. Upon restart, the application will recreate the database schema from scratch.

This process ensures that any issues with the database schema are resolved by recreating it in a fresh state.

## Environment Variables <a id= "environmentVariables"></a>

| name                           | description                                     | default value              |
|--------------------------------|-------------------------------------------------|----------------------------|
| APP_PORT                       | port number of application                      | 8080                       |
| SECURITY_ENABLE                | To keep spring security enable/disable          | true                       |
| KEYCLOAK_REALM_NAME            | Realm name of keycloak                          | SWD                        |
| KEYCLOAK_CLIENT_ID             | Keycloak public client id                       | pb_backend                 |
| KEYCLOAK_ROLE_CLIENT_ID        | Keycloak private client id                      | pb_backend                 |
| KEYCLOAK_AUTH_URL              | Keycloak server url                             | http://localhost:9090/auth |
| DB_HOST                        | Database host                                   | localhost                  |
| DB_PORT                        | Port of database                                | 5432                       |
| DB_NAME                        | Database name                                   | multi-module-demo          |
| USE_SSL                        | Whether SSL is enabled in database server       | false                      |
| DB_USER                        | Database username                               |                            |
| DB_PASSWORD                    | Database password                               |                            |
| MULTIPART_MAX_FILE_SIZE        | Max multiple file size                          | 50 MB                      |
| MULTIPART_MAX_REQUEST_SIZE     | Max multiple part request size                  | 51 MB                      |
| RETRY_CONNECTION_TIMEOUT_MILLI | Duration in millis for retry connection timeout | 200                        |
| RETRY_READ_TIMEOUT_MILLI       | Duration for read timeout                       | 500                        |
| RETRY_MAX_IDLE_CONNECTION      | Duration for idle connection                    | 10                         |
| RETRY_ALIVE_DURATION_MIN       | Duration for retry alive                        | 5                          |
| RETRY_MAX_ATTEMPT              | Max retry attempt                               | 5                          |
| RETRY_INTERVAL_MILLI           | Duration between retry                          | 2000                       |

## Reference of external lib

1. https://www.testcontainers.org/modules/databases/postgres/
2. https://github.com/dasniko/testcontainers-keycloak
3. https://github.com/smartSenseSolutions/smartsense-java-commons

# edc-chat-application

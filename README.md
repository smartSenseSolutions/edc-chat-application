# Chat application using EDC

This is a reference implementation of Chat application utilizing data transfer using EDC.
This definition is part of [Third Eclipse Tractus-X Community Days](https://eclipse-tractusx.github.io/blog/community-days-12-2024).


## Tools and technology
- Java - Spring boot
- ReactJs
- WebSocket
- [EDC - 0.7.2](https://github.com/eclipse-tractusx/tractusx-edc)
- [SSI Dim Wallet stub - 0.0.3](https://github.com/eclipse-tractusx/ssi-dim-wallet-stub)
- Hashicorp Vault
- Docker and Docker compose for local deployment using docker compose

## High level architecture

We have multiple participants in Eclipse Tractus-x dataspace, let's assume We have two business partner and they want to chat/transfer messages. Both partners need to have following application installed:

1. EDC
2. Backend application, this will connect UI application with EDC and used for custom data management
3. UI application
4. PostgreSQL Database
5. Hashicorp Vault

There will be some operator company(trusted issuer) which will host following components:

1. SSI Dim wallet stub application

### High level diagram

![EDC Chat app.jpg](docs/images/EDC_Chat_app.jpg)

## Run in local IDE
### Backend Application

#### How to Start the Backend Application [edc-chat-app-backend]
1. **Build the Project**
   Open the project terminal and run:
   ```bash
   ./gradlew clean build
2. **Run the Backend Application**
  - Set following env variable:
##### Environment Variables

| **Env Name**                     | **Description**                                                 |
|-----------------------------------|-----------------------------------------------------------------|
| `CHAT_DATASOURCE_HOST`            | Database host                                                  |
| `CHAT_DATASOURCE_PORT`            | Database port                                                  |
| `CHAT_DATASOURCE_DATABASE`        | Database name (create if it does not exist)                    |
| `CHAT_DATASOURCE_USERNAME`        | Database username                                              |
| `CHAT_DATASOURCE_PASSWORD`        | Database password                                              |
| `CHAT_EDC_AUTHCODE`               | EDC AuthCode                                                   |
| `CHAT_EDC_ASSETID`                | Asset ID to configure for communication                        |
| `CHAT_EDC_POLICYID`               | Asset Policy ID                                                |
| `CHAT_EDC_CONTRACTDEFINITIONID`   | Asset Contract Definition ID                                   |
| `CHAT_EDC_URL`                    | EDC Base Management URL                                        |

 - Run the application
   Run the application by executing the following command::
   ```bash
   ./gradlew bootRun

### UI Application
#### How to Start the Chat-App UI [edc-chat-app-ui]

1. **Install Dependencies**
   Before starting the application, install the required dependencies. Run the following command in the project directory:
   ```bash
   npm install

2. **Run UI Application**
   After installing the dependencies, start the application using:
   ```bash
   npm start

## Local Deployment using docker compose

Please refer [README.md](deployment/README.md)

## Technical Documentation

Please refer [technical.md](docs/technical.md)

## Out of scope

- Any authentication and authorization
- EDC discovery service, each participant need to register partner's BPN and EDC URL to start chat
- Fancy UI/UX
- Deployment using helm
- Negative scenarios

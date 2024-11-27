# Chat Application backend

## About The Project

This is the backend implementation for chat application through EDC. The server connects to the below entities for a
successful chat flow:

- a Business Partner's EDC to establish a two-way communication.
- a Postgres database to store other Business Partners' information and chat history.
- a simple UI to send messages to and view messages from other Business Partners.

## Packages Overview

The backend application consists of the below packages:

- **dao:** Contains the database entities and their JPA repositories
- **edc:** This package contains a client to communicate with another EDC, services to create assets, policies,
  negotiate contract, etc.
- **service:** Houses the services that implement the application's business logic
- **utils:** The utils package contains application configurations, constants, validation methods, etc.
- **web:** Houses the main application and the REST controllers for communicating with the server
    - **apidocs:** This child package contains the documentation of the APIs in the web package and is used in
      generating the OpenAPI specification

## WebSocket

This application uses WebSocket connection to facilitate simultaneous transfer of messages between frontend and the
server. The [WebSocketConfig.java](src/main/java/com/smartsense/chat/config/WebSocketConfig.java) implements
`WebSocketMessageBrokerConfigurer` interface to configure below properties:

- **STOMP Endpoint:** This endpoint is added to the `StompEndpointRegistry` for the clients to look up and connect to
  the WebSocket.
- **Message Broker destinations:** The client subscribes to these prefixes for receiving messages from the server.
- **Application Destination Prefixes:** The client sends messages to the server using these API prefixes.
- **User Destination Prefix:** This prefix is used while establishing a unique session between the client and the
  server.

## How to deploy the backend application :

### Pre-requisite to start backend Application

- EDC
- Wallet Stub
- Postgres Database

### Local setup :

- Build the Project
  Open the project terminal and run:
   ```bash
   ./gradlew clean build
- For locally deploy EDC, Postgres, wallet using Docker, please refer : [README.md](deployment/README.md)
- After deployment, set the following environment variables:

| **Env Name**                    | **Description**                             |
|---------------------------------|---------------------------------------------|
| `CHAT_DATASOURCE_HOST`          | Database host                               |
| `CHAT_DATASOURCE_PORT`          | Database port                               |
| `CHAT_DATASOURCE_DATABASE`      | Database name (create if it does not exist) |
| `CHAT_DATASOURCE_USERNAME`      | Database username                           |
| `CHAT_DATASOURCE_PASSWORD`      | Database password                           |
| `CHAT_EDC_AUTHCODE`             | EDC AuthCode                                |
| `CHAT_EDC_ASSETID`              | Asset ID to configure for communication     |
| `CHAT_EDC_POLICYID`             | Asset Policy ID                             |
| `CHAT_EDC_CONTRACTDEFINITIONID` | Asset Contract Definition ID                |
| `CHAT_EDC_URL`                  | EDC Base Management URL                     |

- Run the application by executing the following command:
   ```bash
   ./gradlew bootRun

## Database

For data migration, we use Liquibase. So when the application starts or restarts, the data will already be added or
updated.

### Database Table Documentation

#### `business_partner` Table

| **Column Name** | **Data Type** | **Constraints**       | **Default Value**    | **Description**                             |
|-----------------|---------------|-----------------------|----------------------|---------------------------------------------|
| `id`            | UUID          | PRIMARY KEY, NOT NULL | `uuid_generate_v4()` | Unique identifier for the record.           |
| `name`          | VARCHAR(100)  | NOT NULL              | None                 | Name of the business partner.               |
| `bpn`           | VARCHAR(50)   | NOT NULL              | None                 | Business Partner Number.                    |
| `edc_url`       | VARCHAR(250)  | NOT NULL              | None                 | URL for EDC communication.                  |
| `created_at`    | TIMESTAMP(6)  | NULL                  | `NOW()`              | Timestamp when the record was created.      |
| `updated_at`    | TIMESTAMP(6)  | NULL                  | `NOW()`              | Timestamp when the record was last updated. |

---

#### `chat_messages` Table

| **Column Name**   | **Data Type** | **Constraints** | **Default Value** | **Description**                               |
|-------------------|---------------|-----------------|-------------------|-----------------------------------------------|
| `id`              | SERIAL        | PRIMARY KEY     | Auto-increment    | Unique identifier for the record              |
| `partner_bpn`     | VARCHAR(255)  | NOT NULL        | None              | BPN of the partner                            |
| `message`         | TEXT          | NOT NULL        | None              | The chat message content                      |
| `self_owner`      | BOOLEAN       | NULL            | None              | Indicates whether the message is self-owned   |
| `is_chat_success` | BOOLEAN       | NULL            | None              | Indicates if the chat was successful          |
| `negotiation_id`  | VARCHAR(255)  | NULL            | None              | Contract negotiation id                       |
| `agreement_id`    | VARCHAR(255)  | NULL            | None              | Contract agreement id                         |
| `transfer_id`     | VARCHAR(255)  | NULL            | None              | Transfer id                                   |
| `error_detail`    | TEXT          | NULL            | None              | Error details if any of the EDC process fails |
| `created_at`      | TIMESTAMP(6)  | NULL            | `NOW()`           | Timestamp when the message was created        |

#### `edc_offer_details` Table

| **Column Name** | **Data Type** | **Constraints** | **Default Value** | **Description**                         |
|-----------------|---------------|-----------------|-------------------|-----------------------------------------|
| `id`            | SERIAL        | PRIMARY KEY     | Auto-increment    | Unique identifier for the record.       |
| `receiver_bpn`  | VARCHAR(255)  | NOT NULL        | None              | BPN of the receiver.                    |
| `offer_id`      | VARCHAR(255)  | NOT NULL        | None              | Policy offer id.                        |
| `asset_id`      | VARCHAR(255)  | NULL            | None              | Receiver's asset id.                    |
| `created_at`    | TIMESTAMP(6)  | NULL            | `NOW()`           | Timestamp when the message was created. |


## To be added by Neha

Description added by Neha

# How to deploy the backend application :
## Pre-requisite to start backend Application

- EDC
- Wallet Stub
- Postgres Database

## Local setup :
- Build the Project
  Open the project terminal and run:
   ```bash
   ./gradlew clean build
- For locally deploy EDC, Postgres, wallet using Docker, please refer : [README.md](deployment/README.md)
- After deployment, set the following environment variables:

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

- Run the application by executing the following command:
   ```bash
   ./gradlew bootRun

## Backend Application Architecture
#### Add diagram

#### Description


# Database
For data migration, we use Liquibase. So when the application starts or restarts, the data will already be added or updated.

## Database Table Documentation

### `business_partner` Table

| **Column Name** | **Data Type**   | **Constraints**             | **Default Value**    | **Description**                    |
|------------------|-----------------|-----------------------------|----------------------|-------------------------------------|
| `id`            | UUID            | PRIMARY KEY, NOT NULL       | `uuid_generate_v4()` | Unique identifier for the record.  |
| `name`          | VARCHAR(100)    | NOT NULL                    | None                 | Name of the business partner.      |
| `bpn`           | VARCHAR(50)     | NOT NULL                    | None                 | Business Partner Number.           |
| `edc_url`       | VARCHAR(250)    | NOT NULL                    | None                 | URL for EDC communication.         |
| `created_at`    | TIMESTAMP(6)    | NULL                        | `NOW()`              | Timestamp when the record was created. |
| `updated_at`    | TIMESTAMP(6)    | NULL                        | `NOW()`              | Timestamp when the record was last updated. |

---

### `edc_process_states` Table

| **Column Name**   | **Data Type**   | **Constraints**             | **Default Value**    | **Description**                      |
|--------------------|-----------------|-----------------------------|----------------------|---------------------------------------|
| `id`              | SERIAL          | PRIMARY KEY                 | Auto-increment       | Unique identifier for the record.    |
| `receiver_bpn`    | VARCHAR(20)     | NOT NULL                    | None                 | BPN of the receiver.                 |
| `offer_id`        | VARCHAR(255)    | NULL                        | None                 | Identifier for the offer.            |
| `negotiation_id`  | VARCHAR(255)    | NULL                        | None                 | Identifier for the negotiation.      |
| `agreement_id`    | VARCHAR(255)    | NULL                        | None                 | Identifier for the agreement.        |
| `transfer_id`     | VARCHAR(255)    | NULL                        | None                 | Identifier for the data transfer.    |
| `error_detail`    | TEXT            | NULL                        | None                 | Details about any errors.            |
| `created_at`      | TIMESTAMP(6)    | NULL                        | `NOW()`              | Timestamp when the record was created. |
| `updated_at`      | TIMESTAMP(6)    | NULL                        | `NOW()`              | Timestamp when the record was last updated. |

---

### `chat_messages` Table

| **Column Name**          | **Data Type**   | **Constraints**                          | **Default Value**    | **Description**                              |
|---------------------------|-----------------|-------------------------------------------|----------------------|----------------------------------------------|
| `id`                     | SERIAL          | PRIMARY KEY                              | Auto-increment       | Unique identifier for the record.           |
| `edc_process_state_id`   | INT             | FOREIGN KEY REFERENCES edc_process_states(id) | None           | Links to the associated process state.       |
| `partner_bpn`            | VARCHAR(255)    | NOT NULL                                 | None                 | BPN of the partner.                          |
| `message`                | TEXT            | NOT NULL                                 | None                 | The chat message content.                    |
| `self_owner`             | BOOLEAN         | NULL                                     | None                 | Indicates whether the message is self-owned. |
| `created_at`             | TIMESTAMP(6)    | NULL                                     | `NOW()`              | Timestamp when the message was created.      |
| `is_chat_success`        | BOOLEAN         | NULL                                     | None                 | Indicates if the chat was successful.        |





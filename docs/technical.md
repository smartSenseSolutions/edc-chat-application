## Technical documentation for EDC chat application

This documentation explain technical details how this solution is implemented.

### Data flow/Sequence Diagram

## Add Business Partner
**Description**: This step is pre-requisite step to chat with any business partner. 
Provided EDC url will use to fetch catalog and transfer process. It will store BPN number and edc url in database to perform operations.

![Add Business Partner](./images/add_business_partner.png)

## Chat Request flow
**Description**: This diagram showcase entire chat application request flow from sender application stack to receiver application using EDC. 

![Chat request flow](./images/chat_request_flow.png)


## API docs
### API Endpoints

**1. Query Catalog:**
**Description**: This API provides detailed information about assets created by receiver.

**POST** ``/v2/catalog/request``


**Request Headers**

| **Parameter Name** | **Type** | **Decscription** |
|--------------------|----------|------------------|
| x-api-key          | string   | EDC_API_KEY      |

Request body

```json
{
    "@context": {
        "dct": "https://purl.org/dc/terms/",
        "edc": "https://w3id.org/edc/v0.0.1/ns/",
        "odrl": "http://www.w3.org/ns/odrl/2/"
    },
    "@type": "edc:CatalogRequest",
    "protocol": "dataspace-protocol-http",
    "counterPartyAddress": "http://localhost:8195/api/v1/dsp",
    "counterPartyId": "BPNL000000000001",
    "querySpec": {
        "filterExpression": [
            {
                "operandLeft": "https://w3id.org/edc/v0.0.1/ns/id",
                "operandRight": "edc-chat-app",
                "operator": "="
            }
        ]
    }
}
```

Response body
```json
{
    "@id": "ca70a8f7-bacd-4446-9508-6976460a8945",
    "@type": "dcat:Catalog",
    "dspace:participantId": "BPNL000000000001",
    "dcat:dataset": {
        "@id": "edc-chat-app",
        "@type": "dcat:Dataset",
        "odrl:hasPolicy": {
            "@id": "Y29udHJhY3RfMQ==:YXNzZXRfMQ==:NjU3Nzg5ZGMtOWM0NC00ZjA5LWFkOWQtZjAyMTk3MTYwN2E2",
            "@type": "odrl:Offer",
            "odrl:permission": {
                "odrl:action": {
                    "@id": "odrl:use"
                }
            }
        },
        "type": {
            "@id": "Asset"
        },
        "id": "edc-chat-app"
    },
    "dcat:service": {
        "@id": "441ac4d5-d125-4de1-a73a-5e5601bca281",
        "@type": "dcat:DataService",
        "dcat:endpointDescription": "dspace:connector",
        "dcat:endpointUrl": "http://localhost:8195/api/v1/dsp",
        "dct:terms": "dspace:connector",
        "dct:endpointUrl": "http://localhost:8195/api/v1/dsp"
    },
    "participantId": "BPNL000000000001",
    "@context": {
        "@vocab": "https://w3id.org/edc/v0.0.1/ns/",
        "edc": "https://w3id.org/edc/v0.0.1/ns/",
        "tx": "https://w3id.org/tractusx/v0.0.1/ns/",
        "tx-auth": "https://w3id.org/tractusx/auth/",
        "cx-policy": "https://w3id.org/catenax/policy/",
        "dcat": "http://www.w3.org/ns/dcat#",
        "dct": "http://purl.org/dc/terms/",
        "odrl": "http://www.w3.org/ns/odrl/2/",
        "dspace": "https://w3id.org/dspace/v0.8/"
    }
}
```

**2. Initiate Negotiation :**
**Description**: This API initiate negotiation for particular offer between sender and receiver.

**POST** ``/v2/edrs``


**Request Headers**

| **Parameter Name** | **Type** | **Decscription** |
|--------------------|----------|------------------|
| x-api-key          | string   | EDC_API_KEY      |

Request body

```json
{
    "@context": [
        "https://w3id.org/tractusx/policy/v1.0.0",
        "http://www.w3.org/ns/odrl.jsonld",
        {
            "edc": "https://w3id.org/edc/v0.0.1/ns/"
        }
    ],
    "@type": "ContractRequest",
    "edc:counterPartyAddress": "http://localhost:8195/api/v1/dsp",
    "edc:protocol": "dataspace-protocol-http",
    "edc:counterPartyId": "BPNL000000000001",
    "edc:policy": {
        "@id": "Y29udHJhY3RfMQ==:YXNzZXRfMQ==:NjU3Nzg5ZGMtOWM0NC00ZjA5LWFkOWQtZjAyMTk3MTYwN2E2",
        "@type": "Offer",
        "permission": [
            {
                "action": "use"
            }
        ],
        "target": "edc-chat-app",
        "assigner": "BPNL000000000001"
    }
}
```

Response body
```json
{
    "@type": "IdResponse",
    "@id": "5cc4fbc1-6a53-482f-8031-5a34d82723e7",
    "createdAt": 1732527673086,
    "@context": {
        "@vocab": "https://w3id.org/edc/v0.0.1/ns/",
        "edc": "https://w3id.org/edc/v0.0.1/ns/",
        "tx": "https://w3id.org/tractusx/v0.0.1/ns/",
        "tx-auth": "https://w3id.org/tractusx/auth/",
        "cx-policy": "https://w3id.org/catenax/policy/",
        "odrl": "http://www.w3.org/ns/odrl/2/"
    }
}
```


**3. Get Contract Agreement :**
**Description**: This API provide contract state.

**GET** ``v2/contractnegotiations/{negotiationId}``


**Request Headers**

| **Parameter Name** | **Type** | **Decscription** |
|--------------------|----------|------------------|
| x-api-key          | string   | EDC_API_KEY      |

**Path Parameters**

| **Parameter Name** | **Type** | **Decscription** |
|--------------------|----------|------------------|
| negotiationId      | string   | Negotiation Id   |

Response body
```json
{
    "@type": "ContractNegotiation",
    "@id": "5e653010-90a2-4e35-99a2-000002b3fed5",
    "type": "CONSUMER",
    "protocol": "dataspace-protocol-http",
    "state": "FINALIZED",
    "counterPartyId": "BPNL000000000001",
    "counterPartyAddress": "http://localhost:8195/api/v1/dsp",
    "callbackAddresses": {
        "@type": "CallbackAddress",
        "transactional": true,
        "uri": "local://adapter",
        "events": [
            "contract.negotiation",
            "transfer.process"
        ]
    },
    "createdAt": 1732529967144,
    "contractAgreementId": "d8054ae6-0709-465b-8b04-6c8abbbb06b1",
    "@context": {
        "@vocab": "https://w3id.org/edc/v0.0.1/ns/",
        "edc": "https://w3id.org/edc/v0.0.1/ns/",
        "tx": "https://w3id.org/tractusx/v0.0.1/ns/",
        "tx-auth": "https://w3id.org/tractusx/auth/",
        "cx-policy": "https://w3id.org/catenax/policy/",
        "odrl": "http://www.w3.org/ns/odrl/2/"
    }
}
```

**4. Initiate Transfer  :**
**Description**: This API initiate transfer process.

**POST** ``/v2/edrs/request``


**Request Headers**

| **Parameter Name** | **Type** | **Decscription** |
|--------------------|----------|------------------|
| x-api-key          | string   | EDC_API_KEY      |

Request body

```json
{
    "@context": {
        "@vocab": "https://w3id.org/edc/v0.0.1/ns/"
    },
    "@type": "QuerySpec",
    "offset": 0,
    "limit": 1,
    "filterExpression": [
        {
            "operandLeft": "agreementId",
            "operator": "=",
            "operandRight": "0f06f252-017e-4301-a563-3f315f02a3d7"
        }
    ]
}
```

Response body
```json
[
    {
        "@id": "ade44434-8801-4b96-a947-28f342430548",
        "@type": "EndpointDataReferenceEntry",
        "providerId": "BPNL000000000001",
        "assetId": "edc-chat-app",
        "agreementId": "5e653010-90a2-4e35-99a2-000002b3fed5",
        "transferProcessId": "ade44434-8801-4b96-a947-28f342430548",
        "createdAt": 1732705773977,
        "contractNegotiationId": "7ab41415-823f-4691-9a10-f821713c8147",
        "@context": {
            "@vocab": "https://w3id.org/edc/v0.0.1/ns/",
            "edc": "https://w3id.org/edc/v0.0.1/ns/",
            "tx": "https://w3id.org/tractusx/v0.0.1/ns/",
            "tx-auth": "https://w3id.org/tractusx/auth/",
            "cx-policy": "https://w3id.org/catenax/policy/",
            "odrl": "http://www.w3.org/ns/odrl/2/"
        }
    }
]
```
**5. Get Data address detail :**
**Description**: This API provide fetch data address of receiver edc where data can be transfer.

**GET** ``/v2/edrs/{transferProcessId}/dataaddress``


**Request Headers**

| **Parameter Name** | **Type** | **Decscription** |
|--------------------|----------|------------------|
| x-api-key          | string   | EDC_API_KEY      |

**Path Parameters**

| **Parameter Name** | **Type** | **Decscription**                            |
|--------------------|----------|---------------------------------------------|
| transferProcessId  | string   | TransferProcess Id fetch from previous step |

Response body
```json
{
    "@type": "DataAddress",
    "endpointType": "https://w3id.org/idsa/v4.1/HTTP",
    "tx-auth:refreshEndpoint": "http://localhost:8196/public",
    "tx-auth:audience": "did:web:c8df-14-195-76-134.ngrok-free.app:BPNL000000000000",
    "type": "https://w3id.org/idsa/v4.1/HTTP",
    "endpoint": "http://localhost:8196/public",
    "tx-auth:refreshToken": "eyJraWQiOiJwdWJsaWMiLCJhbGciOiJSUzI1NiJ9.eyJleHAiOjE3MzI3MDYwNzIsImlhdCI6MTczMjcwNTc3MiwianRpIjoiNzFmODhlZTgtN2ExMC00ZWU1LThkNmItMGM1OGExOTgyZDQxIn0.POIn_T8s4JWxv6Ra723GpQ2Je0bVnWwX28IaORGKiWkeaqWg5vHpnAW2KKCRXUYUuCHSTcDVMSNzVoNOmgAB_BkbdHu5LDage3TuYh8N-dQfzHWfyxx7gyKR48A6hMNDQdydUFGS0jeQ5AEM9DCgEVzfVSutbSheYreyKLK7Qa6XhqOs6TOtmdnDcVM3R79rNosUnVVLtyQksUruvxuoXwg66yIEMwpzEzysH0jIzzcLEFhp_Fnv4e2OsJHxCkTBSiKT6LY2CD7w9lCN3nTKX6iimxhfHxTv5L0ThU6zNY3_lx317Xiz3HocxIa2PQ1pQTQpJrFrRUcDCkPj08Xq4Q",
    "tx-auth:expiresIn": 300,
    "authorization": "eyJraWQiOiJwdWJsaWMiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJCUE5MMDAwMDAwMDAwMDAxIiwiYXVkIjoiQlBOTDAwMDAwMDAwMDAwMCIsInN1YiI6IkJQTkwwMDAwMDAwMDAwMDEiLCJleHAiOjE3MzI3MDYwNzIsImlhdCI6MTczMjcwNTc3MiwianRpIjoiMDYxMTQwNTctZWY5Ni00ZDhiLTg1YWQtYjBiMDE1YTM1NDIyIn0.WGXxnH3UZbLHWbT7hn6ZxOXeIGKpLQyft6wuprrkYDRQmHEEDWIOIzvOTpQTZtn7ZlzEKK_PWVpPXgqK8Sy2G43sTWoIVMsPxE5YUU8VJISCwRlbg3A_E2bm5DxXj6-aqCeUlG92CLwffAvjEAp67G6fMrdTvaQFbivautbEGv7N1wIpvogbvo535p0ZNI9vDnPzNxZgn3ssK6tB4DH77KjVKWUvOEVz80v0QMXfpv4PsaLrRn0rWfNolj8Ub0vILPib68JM3R8TbVqxrOGbbwVbiQ8gU-JI0gBpdzGDLyupOSklN5INF2Ugg9TQKOZaEgkxk3QaD2pMcPwue2nFwg",
    "tx-auth:refreshAudience": "did:web:c8df-14-195-76-134.ngrok-free.app:BPNL000000000000",
    "@context": {
        "@vocab": "https://w3id.org/edc/v0.0.1/ns/",
        "edc": "https://w3id.org/edc/v0.0.1/ns/",
        "tx": "https://w3id.org/tractusx/v0.0.1/ns/",
        "tx-auth": "https://w3id.org/tractusx/auth/",
        "cx-policy": "https://w3id.org/catenax/policy/",
        "odrl": "http://www.w3.org/ns/odrl/2/"
    }
}
```
Note: We need extract two parameters from above response which is *authorization* and  *endpoint*.We have to call *endpoint* with given *authorization* code along with our chat body.


**6. Start Data Transfer  :**
**Description**: This API initiate data transfer process.

**POST** ``/public`` *This endpoint will be same endpoint we extract parameter from last step*


**Request Headers**

| **Parameter Name** | **Type** | **Decscription**                       |
|--------------------|----------|----------------------------------------|
| authorization      | string   | Extracted parameter from previous step |

Request body

```json
{

    "receiverBpn": "BPNL000000000000",
    "message": "chat message from sender"
}
```

Response body
```json

    {
        "message": "message sent successfully"
    }

```



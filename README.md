# Chat application using EDC

This is a reference implementation of Chat application utilizing data transfer using EDC.
This definition is part of [Third Eclipse Tractus-X Community Days](https://eclipse-tractusx.github.io/blog/community-days-12-2024).


## Tools and technology
- Java - Spring boot
- ReactJs
- WebSocket
- [EDC - 0.7.7](https://github.com/eclipse-tractusx/tractusx-edc)
- [SSI Dim Wallet stub - 0.0.4](https://github.com/eclipse-tractusx/ssi-dim-wallet-stub)
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
    To be added my Mittal

## Local Deployment using docker compose

Please refer [README.md](deployment/README.md)

## Technical Documentation 

Please refer [technical.md](docs/technical.md)

## Out of scope
- Any authentication and authorization 
- EDC discovery service, each participant need to register partner's BPN and EDC DSP URL to start chat
- Fancy UI/UX
- Deployment using helm
- Negative scenarios

## Note
- There we be latency in transfer chat message due to contract negotiation and transfer process of the EDC 
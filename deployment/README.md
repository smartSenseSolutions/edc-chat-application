# Deployment document to setup in local

This document provide step-by-step guide line to deploy Chat application using docker compose.

For better understanding, let's take example like Tractux-X is a operator company and we have two participant smartSense and Cofinity-X.


Once you deploy this stack, it will run below containers

- smartSense(provider)
- - EDC
- - Backend application
- - Chat UI application 
- Cofinity-X(consumer)
- - EDC
- - Backend application 
- - Chat UI application
- Common application 
- - SSI dim wallet stub 
- - PostgreSQL database


## Data setup and initial config

- Operator/issuer BPN: BPNL0000TRACTUSX
- Company 1 BPN: BPNL00SMARTSENSE
- Company 2 BPN: BPNL000COFINITYX


### Pre-requirement
- Docker 
- Docker compose

### Deploy in local
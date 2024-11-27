# Deployment document to setup in local

This document provide step-by-step guide line to deploy Chat application using docker compose.

For better understanding, let's take example like Tractux-X is a operator company and we have two participant smartSense and Catena-X.

Once you deploy this stack, it will run below containers

- smartSense(provider)
- - EDC(Control and data plane)
- - Backend application
- - Chat UI application 
- Catena-X(consumer)
- - EDC(Control and data plane)
- - Backend application 
- - Chat UI application
- Common application 
- - SSI dim wallet stub 
- - PostgreSQL database


## Data setup and initial config

- Operator/issuer BPN: BPNL0000TRACTUSX
- Company 1 BPN: BPNL00SMARTSENSE
- Company 2 BPN: BPNL00000CATENAX


### Pre-requirement
- Docker 
- Docker compose

### Deploy in local
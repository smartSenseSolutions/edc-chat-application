# Deploymet document to setup in local

This document provide step-by-step guide line to deploy Chat aplication using docker compose.

Once you deploy this stack, it will run below containers

- Company 1 side(provider)
- - EDC
- - Backend application
- - Chat UI application 
- Company 2 side(consumer)
- - EDC
- - Backend application 
- - Chat UI application
- Common application 
- - SSI dim wallet stub 


## Data setup and initial config

- Oparator/issuer BPN: BPNL0000TRACTUSX
- Company 1 BPN: BPNL00SMARTSENSE
- Company 2 BPN: BPNL000COFINITYX


### Pre-requerment
- Docker 
- Docker compose

### Deploy in local
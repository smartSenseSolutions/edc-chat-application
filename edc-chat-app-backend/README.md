# Chat Application backend

## About The Project

This is the backend implementation for chat application through EDC. The server connects to the below entities for a
successful chat flow:
- Business Partner's EDC to establish a two-way communication.
- a Postgres database to store other Business Partners' information and chat history.
- a simple UI to send messages to and view messages from other Business Partners. This connection uses WebSocket to
  instantly transfer messages from the server to the client.

## Packages Overview

The backend application consists of the below packages:
- **dao:** Contains the database entities and their JPA repositories
- **edc:** This package contains a client to communicate with another EDC, services to create assets, policies, negotiate contract, etc. 
- **service:** Houses the services that implement the application's business logic
- **utils:** The utils package contains application configurations, constants, validation methods, etc.
- **web:** Houses the main application and the REST controllers for communicating with the server
    - **apidocs:** This child package contains the documentation of the APIs in the web package and is used in generating the OpenAPI specification

// add websocket details

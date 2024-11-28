## UI application for Chat using EDC

This is ReactJS application for chat using EDC.

### Run in local

#### Pre-Requirements

1. Both(smartSense and Catena-X) EDCs and backend services should be up and running
2. Node.js should be installed in your local

#### Environment variables

| Environment Variable      | Value                   | Description                                                         |
|---------------------------|-------------------------|---------------------------------------------------------------------|
| `REACT_APP_API_BASE_URL`  | `http://localhost:8083` | Base URL for the backend API. This is where API requests are sent.  |
| `REACT_APP_WEBSOCKET_URL` | `ws://localhost:8083`   | WebSocket URL for real-time communication with the backend server.  |


#### Steps

1. Update environment variables in  [.env.producer](.env.smartsense) and [.env.consumer](.env.catena-x)
2. Install need packages using command ``npm install``
3. Run consumer app using command ``npm run start:catenax``, you can access consumer UI on ``http://localhost:3000``
4. Run producer app using command ``npm run start:smartsense``, you can access consumer UI on ``http://localhost:3001``

# Stage 1: Build the React application
FROM node:16 as build

# Set the maintainer information
LABEL maintainer="Your Name <your.email@example.com>"

# Set working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application files
COPY . .

# Build the React application for production
RUN npm run build

# Stage 2: Serve the application using nginx (non-root)
FROM nginx:alpine

# Set the maintainer information
LABEL maintainer="Your Name <your.email@example.com>"

# Copy the React build output to nginx's HTML directory
COPY --from=build /app/build /usr/share/nginx/html

EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"]

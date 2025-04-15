# Step 1: Use an official Node.js image as the base image
FROM node:16 AS build

# Step 2: Set the working directory in the container
WORKDIR /app

# Step 3: Copy package.json and package-lock.json (if available)
COPY package.json package-lock.json ./ 

# Step 4: Install dependencies
RUN npm install

# Step 5: Copy the rest of the application code
COPY . . 

# Step 6: Build the React app for production
RUN npm run build

# Step 7: Use a lightweight server to serve the built app (using `nginx` in this case)
FROM nginx:alpine

# Step 8: Copy the build output from the previous image to the Nginx public folder
COPY --from=build /app/build /usr/share/nginx/html

# Step 9: Copy the custom nginx configuration into the default location
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Step 10: Expose port 80 (default port for HTTP)
EXPOSE 80

# Step 11: Run Nginx to serve the app
CMD ["nginx", "-g", "daemon off;"]

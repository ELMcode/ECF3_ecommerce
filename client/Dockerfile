# Use Node.js 14 (LTS Fermium) for compatibility with older React projects
FROM node:14

# Create working directory
WORKDIR /app

# Copy dependencies
COPY package.json package-lock.json ./

# Install dependencies
RUN npm install

# Copy rest of the files
COPY . .

# Build the application in dev mode
RUN npm run build_dev

# Use Nginx to serve the built application
FROM nginx:alpine

# Copy built files to Nginx directory
COPY --from=0 /app/build /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Command to run Nginx in foreground
CMD ["nginx", "-g", "daemon off;"]

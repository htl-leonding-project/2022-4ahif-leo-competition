# Stage 1: Build an Angular Docker Image
FROM node:16 as build
WORKDIR /app
COPY package*.json /app/
RUN npm install
COPY . /app
ARG configuration=production
RUN npm run build -- --output-path=./dist/out --configuration $configuration --base-href=/t.stuetz/leocompetition/

# Stage 2, use the compiled app, ready for production with Nginx
FROM nginx
COPY --from=build /app/dist/out/ /usr/share/nginx/html
COPY custom-nginx.conf /etc/nginx/conf.d/custom-nginx.conf

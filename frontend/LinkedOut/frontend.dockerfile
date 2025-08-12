FROM node:18 AS build

WORKDIR /app

COPY frontend/LinkedOut/package*.json ./
RUN npm install

COPY frontend/LinkedOut .

RUN npm run build --prod

FROM nginx:alpine

COPY --from=build /app/dist/linked-out /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
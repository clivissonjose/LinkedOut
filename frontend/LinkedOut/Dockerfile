FROM node:18 AS build

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

# Gera os arquivos de produção do Angular
RUN npm run build --prod

FROM nginx:alpine

# Copia a pasta correta da build Angular
#COPY --from=build /app/dist/linked-out /usr/share/nginx/html
COPY --from=build /app/dist/linked-out/browser /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]

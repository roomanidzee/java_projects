FROM node:latest

WORKDIR /usr/src/application

COPY . .

RUN npm install

ENTRYPOINT ["npm", "start"]

EXPOSE 8083
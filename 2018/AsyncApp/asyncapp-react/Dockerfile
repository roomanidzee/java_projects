FROM node:10.11.0-alpine
LABEL maintainer="Andrey Romanov <steampart@gmail.com>"

WORKDIR /usr/src/application

COPY . .

RUN npm install
RUN npm run build
RUN npm install serve

EXPOSE 5000

CMD ["npx", "serve", "-s", "build"]
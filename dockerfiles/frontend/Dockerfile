FROM node:10-alpine

RUN mkdir -p web
RUN chown -R node:node web
WORKDIR web
COPY ./web/package*.json ./
USER node

RUN npm install

COPY --chown=node:node web .

EXPOSE 8080
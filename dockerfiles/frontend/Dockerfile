FROM node:21-alpine

WORKDIR /web
COPY ./web/package.json .
COPY ./web ./
RUN npm install


COPY ./web .

EXPOSE 5173

CMD ["npm", "run", "dev"]
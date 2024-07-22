# syntax=docker/dockerfile:1

ARG NODE_VERSION=20.15.0

FROM node:${NODE_VERSION}-alpine AS base

WORKDIR /usr/src/app

RUN --mount=type=bind,source=package.json,target=package.json \
    --mount=type=bind,source=package-lock.json,target=package-lock.json \
    --mount=type=cache,target=/root/.npm \
    npm ci

COPY . .

RUN npm run build

FROM nginx:latest AS final

ENV NODE_ENV=production

COPY package.json .

COPY --from=base /usr/src/app/dist/frontend/browser /usr/share/nginx/html

EXPOSE 80

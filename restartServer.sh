#!/usr/bin/env bash

docker-compose -f ./Docker/docker-compose.yml kill
mvn clean
mvn install
docker-compose -f Docker/docker-compose.yml up -d

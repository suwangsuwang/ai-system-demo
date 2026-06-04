#!/bin/bash

set -e

echo "============================"
echo "1. build backend"
echo "============================"

cd ../backend
mvn clean package -DskipTests

echo "============================"
echo "2. build docker image"
echo "============================"

docker build -t springboot-demo:v2 .

echo "============================"
echo "3. restart compose"
echo "============================"

cd ../deploy
docker compose down
docker compose up -d

echo "============================"
echo "4. check services"
echo "============================"

sleep 5

curl localhost:8089/api/v1/hello

echo ""
echo "DONE ✔"

#!/bin/bash

set -e

until curl -sSf "http://keycloak:9090/realms/myrealm/.well-known/openid-configuration" > /dev/null; do
  echo "Keycloak not ready, retrying in 5s..."
  sleep 5
done

echo "Keycloak is ready, starting app..."
exec java -jar /app/llamabot.jar

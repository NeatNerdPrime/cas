#!/bin/bash

# while sleep 9m; do echo -e '\n=====[ Gradle build is still running ]====='; done &

echo "Running Grouper docker container"
COMPOSE_FILE=./ci/tests/grouper/docker-compose.yml
test -f $COMPOSE_FILE || COMPOSE_FILE=docker-compose.yml
docker compose -f $COMPOSE_FILE down >/dev/null 2>/dev/null || true
docker compose -f $COMPOSE_FILE up -d
docker compose -f $COMPOSE_FILE logs &
echo "Waiting for Grouper server to come online..."
sleep 120
docker ps | grep "grouper"
retVal=$?
if [ $retVal == 0 ]; then
  max_attempts=5
  attempt=1
  until curl -k -L -u "GrouperSystem:@4HHXr6SS42@IHz2" --fail https://localhost:7443/grouper; do
    if [ $attempt -ge $max_attempts ]; then
      echo "Reached maximum attempts ($max_attempts). Exiting."
      docker ps | grep "grouper"
      docker logs grouper-core &
      docker logs grouper-ws &
      exit 1
    fi
    echo -n '.'
    sleep 2
    attempt=$((attempt + 1))
  done
  echo "Grouper docker container is now running"
else
    echo "Grouper docker container failed to start."
    exit $retVal
fi

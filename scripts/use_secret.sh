#!/bin/bash

# based on https://pythonspeed.com/articles/build-secrets-docker-compose/

# prerequisites:
#export DOCKER_BUILDKIT=1
#export COMPOSE_DOCKER_CLI_BUILD=1

set -euo pipefail
if [ -f /run/secrets/SECRET_TOKEN ]; then
   export SECRET_TOKEN=$(cat /run/secrets/SECRET_TOKEN)
fi

echo "Secret is: $SECRET_TOKEN"
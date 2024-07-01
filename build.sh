#!/bin/bash
set -e

# Set required env variables
if [[ "$OSTYPE" == "darwin"* ]]; then
	# macOS
	export MIDGARD_SCRIPT_FOLDER=$(dirname $(readlink -f "${BASH_SOURCE}"))
  export MIDGARD_ROOT_FOLDER="$(dirname "$MIDGARD_SCRIPT_FOLDER")"
else
	# Other OSs
	export MIDGARD_SCRIPT_FOLDER=$(dirname $(readlink -f "${BASH_SOURCE:-$0}"))
  export MIDGARD_ROOT_FOLDER="$(dirname "$MIDGARD_SCRIPT_FOLDER")"
  
  # Workaround for AZ CLI issues caused by ZScalar in WSL2
  # assuming Windows with WSL here..
  export REQUESTS_CA_BUNDLE=/etc/ssl/certs/ca-certificates.crt
fi

export MIDGARD_ASSETS_FOLDER="$MIDGARD_ROOT_FOLDER/assets"

# Load default environment variables from .env file
if [ -f $MIDGARD_ROOT_FOLDER/.env ]; then
  export $(echo $(cat $MIDGARD_ROOT_FOLDER/.env | sed 's/#.*//g'| xargs -L 1) | envsubst)
fi

yarn install && \
    yarn tsc && \
    yarn build:backend --config ../../app-config.yaml

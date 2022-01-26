#!/bin/bash

cd "$KIVAKIT_HOME"/tools/building/docker || exit

LOWERCASE_VERSION=$(echo "$KIVAKIT_VERSION" | tr '[:upper:]' '[:lower:]')

docker build \
    --progress=plain \
    --no-cache \
    --build-arg ENV_KIVAKIT_VERSION="$KIVAKIT_VERSION" \
    -t "jonathanlocke/kivakit:$LOWERCASE_VERSION" .

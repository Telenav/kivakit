#!/bin/bash

cd "$KIVAKIT_HOME"/tools/building/docker || exit

LOWERCASE_VERSION=$(echo "$KIVAKIT_VERSION" | tr '[:upper:]' '[:lower:]')

docker build \
    --progress=plain \
    --build-arg ENV_KIVAKIT_VERSION="$KIVAKIT_VERSION" \
    -t kivakit-"$LOWERCASE_VERSION" .

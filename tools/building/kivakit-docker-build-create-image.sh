#!/bin/bash

cd "$KIVAKIT_HOME"/tools/building/docker || exit

IMAGE_VERSION=$(echo "$KIVAKIT_VERSION" | tr '[:upper:]' '[:lower:]')

echo " "
echo "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓"
echo "┋"
echo "┋  Building Docker build environment image"
echo "┋"
echo "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"
echo " "

docker build \
    --progress=plain \
    --no-cache \
    --build-arg ENV_KIVAKIT_VERSION="$KIVAKIT_VERSION" \
    --tag "jonathanlocke/kivakit:$IMAGE_VERSION" \
    .

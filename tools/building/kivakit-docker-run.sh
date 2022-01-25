#!/bin/bash

cd "$KIVAKIT_HOME"/tools/building/docker || exit

LOWERCASE_VERSION=$(echo "$KIVAKIT_VERSION" | tr '[:upper:]' '[:lower:]')

docker run \
    -v "$KIVAKIT_WORKSPACE:/root/workspace" \
    -ti "kivakit-$LOWERCASE_VERSION:latest" \
    /bin/bash

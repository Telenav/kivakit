#!/bin/bash

cd "$KIVAKIT_HOME"/tools/building/docker || exit

if [ "$1" = "" ]; then
    VERSION=$(echo "$KIVAKIT_VERSION" | tr '[:upper:]' '[:lower:]')
else
    VERSION=$(echo "$1" | tr '[:upper:]' '[:lower:]')
fi

docker run \
    -v "$KIVAKIT_WORKSPACE:/host/workspace" \
    -ti "jonathanlocke/kivakit:$VERSION" \
    /bin/bash

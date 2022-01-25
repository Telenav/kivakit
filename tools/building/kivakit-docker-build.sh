#!/bin/bash

cd "$KIVAKIT_HOME"/tools/building/docker || exit

docker build --progress=plain --no-cache --build-arg KIVAKIT_VERSION="$KIVAKIT_VERSION" -t kivakit .

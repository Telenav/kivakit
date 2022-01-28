#!/bin/bash

IMAGE_VERSION=$(echo "$KIVAKIT_VERSION" | tr '[:upper:]' '[:lower:]')

docker push jonathanlocke/kivakit:$IMAGE_VERSION

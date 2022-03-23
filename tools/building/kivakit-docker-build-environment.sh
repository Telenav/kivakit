#!/bin/bash

cd "$KIVAKIT_HOME"/tools/building/docker || exit

if [ "$1" = "" ]; then
    IMAGE_VERSION=$(echo "$KIVAKIT_VERSION" | tr '[:upper:]' '[:lower:]')
else
    IMAGE_VERSION=$(echo "$1" | tr '[:upper:]' '[:lower:]')
fi

echo " "
echo "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓"
echo "┋"
echo "┋  Starting Docker build environment"
echo "┋"
echo "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"
echo " "


docker run \
    --volume "$KIVAKIT_WORKSPACE:/host/workspace" \
    --volume "$HOME/.m2:/host/.m2" \
    --volume "$HOME/.kivakit:/host/.kivakit" \
    --interactive --tty "jonathanlocke/kivakit:$IMAGE_VERSION" \
    /bin/bash

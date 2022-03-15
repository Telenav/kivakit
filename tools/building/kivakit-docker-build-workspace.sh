#!/bin/bash

source kivakit-library-functions.sh

help="[host|docker]"

WORKSPACE=$1

require_variable WORKSPACE "$help"

rm "$HOME"/.m2
rm "$HOME"/.kivakit

if [ "$WORKSPACE" = "host" ]; then

    WORKSPACE="/host/workspace"

    ln -s /host/.m2 "$HOME"/.m2
    ln -s /host/.kivakit "$HOME"/.kivakit

elif [ "$WORKSPACE" = "docker" ]; then

    WORKSPACE="/root/workspace"

    ln -s "$DEVELOPER"/.m2 "$HOME"/.m2
    ln -s "$DEVELOPER"/.kivakit "$HOME"/.kivakit

else

    usage "$help"

fi

export KIVAKIT_WORKSPACE="$WORKSPACE"

source "$HOME/.profile"

$SHELL

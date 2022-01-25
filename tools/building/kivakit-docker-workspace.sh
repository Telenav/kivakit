#!/bin/bash

source library-functions.sh
source kivakit-projects.sh

help="[host|container]"

workspace=$1

require_variable workspace "$help"

if [ "$workspace" = "host" ]; then
    workspace="/host/workspace"
elif [ "$workspace" = "container" ]; then
    workspace="/root/workspace"
else
    usage "$help"
fi

export KIVAKIT_WORKSPACE="$workspace"

source ~/.profile

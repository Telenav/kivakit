#!/bin/bash

source library-functions.sh
source kivakit-projects.sh

help="[host|container]"

workspace=$1

require_variable workspace "$help"

export KIVAKIT_WORKSPACE="$workspace" && bash -i

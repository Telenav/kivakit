#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source library-functions.sh
source library-build.sh
source kivakit-projects.sh

cd "$KIVAKIT_HOME"/superpom
mvn clean install

export ALLOW_CLEANING=true

for project_home in "${KIVAKIT_PROJECT_HOMES[@]}"; do

    build "$project_home" "$@"

    export ALLOW_CLEANING=false

done

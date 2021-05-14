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

build $KIVAKIT_WORKSPACE/cactus-build $@
build $KIVAKIT_WORKSPACE/lexakai-annotations $@

for project_home in "${KIVAKIT_PROJECT_HOMES[@]}"; do

    build $project_home $@

done

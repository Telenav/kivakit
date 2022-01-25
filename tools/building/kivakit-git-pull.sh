#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source "$KIVAKIT_WORKSPACE"/kivakit/tools/library/library-functions.sh
source kivakit-projects.sh

for project_home in "${KIVAKIT_PROJECT_HOMES[@]}"; do

    cd "$project_home" && echo "Updating $project_home" && git pull

done

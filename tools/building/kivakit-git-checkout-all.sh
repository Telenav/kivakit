#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source library-functions.sh
source kivakit-projects.sh

help="[branch]"

branch=$1

require_variable branch "$help"

for project_home in "${KIVAKIT_PROJECT_HOMES[@]}"; do

    cd "$project_home" && echo "Updating $project_home" && git checkout "$branch"

done


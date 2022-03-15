#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source kivakit-library-functions.sh
source kivakit-projects.sh

version="$1"

help="[version]"

require_variable version "[version]"

snapshot_version="${1%-SNAPSHOT}-SNAPSHOT"

for project_home in "${CACTUS_REPOSITORY_HOMES[@]}"; do

    update_version "$project_home" "$version"

done

for project_home in "${KIVAKIT_REPOSITORY_HOMES[@]}"; do

    update_version "$project_home" "$version"

done


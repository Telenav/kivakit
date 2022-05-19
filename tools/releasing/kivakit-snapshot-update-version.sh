#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source kivakit-library-functions.sh

version="$1"

export help="[version]"

require_variable version "[version]"

snapshot_version="${version%-SNAPSHOT}-SNAPSHOT"

for project_home in "${KIVAKIT_REPOSITORY_HOMES[@]}"; do

    update_version "$project_home" "$snapshot_version"

done


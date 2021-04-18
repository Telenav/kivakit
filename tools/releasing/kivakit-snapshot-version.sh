#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source library-functions.sh
source kivakit-projects.sh

project_home=$1
version="${2%-SNAPSHOT}-SNAPSHOT"

ARGUMENT_HELP="[version]"

version=$1

require_variable version

for project_home in "${KIVAKIT_PROJECT_HOMES[@]}"; do

    update_version $project_home $version

done

#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source library-functions.sh
source kivakit-projects.sh

for project_home in "${KIVAKIT_PROJECT_HOMES[@]}"; do

    project_name=$(project_name $project_home)

    lexakai -project-version=$KIVAKIT_VERSION -output-folder=$KIVAKIT_DATA_HOME/docs/lexakai/$project_name $project_home

done

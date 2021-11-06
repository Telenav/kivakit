#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source library-functions.sh
source kivakit-projects.sh

echo " "
echo "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Building Lexakai Documentation ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓"
echo "┋"
echo "┋   kivakit-version = ${KIVAKIT_VERSION}"
echo "┋     output-folder = ${KIVAKIT_ASSETS_HOME}/docs/$KIVAKIT_VERSION/lexakai"
echo "┋"
echo "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"
echo " "

for project_home in "${KIVAKIT_PROJECT_HOMES[@]}"; do

    project_name=$(project_name "$project_home")

    OUTPUT_FOLDER="$KIVAKIT_ASSETS_HOME/docs/$KIVAKIT_VERSION/lexakai/$project_name"

    lexakai -project-version="$KIVAKIT_VERSION" -output-folder="$OUTPUT_FOLDER" "$project_home"

done

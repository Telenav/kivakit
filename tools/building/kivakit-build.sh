#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source kivakit-library-functions.sh
source kivakit-library-build.sh
source kivakit-projects.sh

export PREBUILD_SCRIPT=kivakit-prebuild.sh

for project_home in "${KIVAKIT_PROJECT_HOMES[@]}"; do

    build "$project_home" "$@"

done

if [ -n "$BUILD_DOCUMENTATION" ]; then

    bash kivakit-build-documentation.sh

fi

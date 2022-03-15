#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source kivakit-library-functions.sh
source kivakit-projects.sh

help="[version]"

version=$1

require_variable version "$help"

for project_home in "${KIVAKIT_REPOSITORY_HOMES[@]}"; do

    git_flow_check_changes "$project_home"

done

for project_home in "${KIVAKIT_REPOSITORY_HOMES[@]}"; do

    git_flow_release_finish "$project_home" "$version"

done

echo " "
echo "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Release Ready to Deploy  ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓"
echo "┋"
echo "┋  Release Version: $version"
echo "┋"
echo "┋  Next Steps:"
echo "┋"
echo "┋  1. Run kivakit-build.sh deploy-ossrh"
echo "┋  2. Sign into OSSRH and release to Maven Central"
echo "┋"
echo "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"
echo " "

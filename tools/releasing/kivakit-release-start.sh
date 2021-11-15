#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source library-functions.sh
source kivakit-projects.sh

help="[version]"

version=$1

require_variable version "$help"

git_flow_release_start "$KIVAKIT_HOME" "$version"
git_flow_release_start "$KIVAKIT_EXAMPLES_HOME" "$version"
git_flow_release_start "$KIVAKIT_EXTENSIONS_HOME" "$version"

bash kivakit-release-update-version.sh "$version"

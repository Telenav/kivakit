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

update_version "$CACTUS_HOME" "$version"
update_version "$KIVAKIT_HOME" "$version"
update_version "$KIVAKIT_EXTENSIONS_HOME" "$version"
update_version "$KIVAKIT_EXAMPLES_HOME" "$version"

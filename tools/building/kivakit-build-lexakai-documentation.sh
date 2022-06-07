#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source kivakit-library-functions.sh

KIVAKIT_VERSION=$(project_version "$KIVAKIT_HOME")

echo " "
echo "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Building Lexakai Documentation ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓"
echo "┋"
echo "┋   project-version = ${KIVAKIT_VERSION}"
echo "┋     output-folder = ${KIVAKIT_ASSETS_HOME}/docs/$KIVAKIT_VERSION/lexakai"
echo "┋"
echo "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"
echo " "

cd "$KIVAKIT_WORKSPACE" || exit

# shellcheck disable=SC2154
git submodule foreach --quiet "[[ "\$path" == *-assets* ]] || [[ ! "\$path" == *kivakit* ]] || lexakai.sh -overwrite-resources=true -update-readme=true -output-folder=$KIVAKIT_ASSETS_HOME/docs/$KIVAKIT_VERSION/lexakai/\$name \$toplevel/\$path" || exit 1

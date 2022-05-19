#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source kivakit-library-functions.sh

echo " "
echo "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Building Lexakai Documentation ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓"
echo "┋"
echo "┋   kivakit-version = ${KIVAKIT_VERSION}"
echo "┋     output-folder = ${KIVAKIT_ASSETS_HOME}/docs/$KIVAKIT_VERSION/lexakai"
echo "┋"
echo "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"
echo " "

cd "$KIVAKIT_WORKSPACE" || exit

# shellcheck disable=SC2154
git submodule foreach "lexakai -project-version=$KIVAKIT_VERSION -output-folder=$KIVAKIT_ASSETS_HOME/docs/$KIVAKIT_VERSION/lexakai/$name $path || :"


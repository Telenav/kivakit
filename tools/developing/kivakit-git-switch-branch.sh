#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source kivakit-library-functions.sh

help="[branch-name]"

# shellcheck disable=SC2034
branch=$1

require_variable branch "$help"

cd "$KIVAKIT_WORKSPACE" || exit

git submodule foreach "git checkout $branch || :"
git submodule foreach "git checkout publish || :"


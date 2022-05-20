#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source kivakit-library-functions.sh

# shellcheck disable=SC2034
branch=$1

require_variable branch "[branch-name]"

cd "$KIVAKIT_WORKSPACE" || exit

git submodule foreach "[[ "$path" == *-assets* ]] || git checkout $branch || exit 1" || exit 1
git submodule foreach "[[ ! "$path" == *-assets* ]] || git checkout publish || exit 1" || exit 1



#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source kivakit-library-functions.sh

# shellcheck disable=SC2034
branch_name=$1

require_variable branch_name "[branch-name]"

if git_flow_check_all_repositories; then

    # shellcheck disable=SC2016
    repository_foreach 'git checkout --quiet $branch_name'
    repository_foreach 'git checkout --quiet publish'

else

    echo "Unable to switch to $branch_name branch"

fi

kivakit-git-pull.sh

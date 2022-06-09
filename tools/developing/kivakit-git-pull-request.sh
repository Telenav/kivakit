#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source kivakit-library-functions.sh

title=$1
body=$1

require_variable title "[title] [body]"
require_variable body "[title] [body]"

if git_flow_check_all_repositories; then

    cd "$TELENAV_WORKSPACE" || exit

    gh auth login --hostname github.com --with-token < ~/token.txt
    gh pr create --title "$title" --body "$body"

else

    echo "Unable to create pull request"

fi

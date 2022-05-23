#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source kivakit-library-functions.sh

git_user_name=$1
repository_name=$2

require_variable repository_name "[git-user-name] [repository-name]"
require_variable git_user_name "[git-user-name] [repository-name]"

if [ ! -d "$repository_name" ]; then

    echo "No repository named $repository_name in the current folder"
    exit 1

fi

if yes_no "WARNING: Overwriting repository $repository_name is NOT recoverable. Proceed? "; then

    echo "Removing .git folder"
    cd "$repository_name" || exit 1
    rm -rf .git

    echo "Initializing new git repository"
    git init
    git config http.postBuffer 1524288000

    echo "Adding origin https://$git_user_name@github.com/Telenav/$repository_name.git"
    git remote add origin "https://$git_user_name@github.com/Telenav/$repository_name.git"

    echo "Adding source files"
    git add --all .

    echo "Committing source files"
    git commit --all --quiet --message 'Initial commit'

    if yes_no "Push repository? "; then

        echo "Pushing to origin"
        git push --set-upstream origin master

    fi

fi

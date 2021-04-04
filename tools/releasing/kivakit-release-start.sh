#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

VERSION=$1

if [ -z "$VERSION" ]; then

    echo "Usage: kivakit-release-start.sh [version-number]"
    exit 0

fi

# Check out the develop branch
cd $KIVAKIT_HOME
git checkout develop

# then start a new release branch
git flow release start $VERSION

# switch to the release branch
git checkout release/$VERSION

# and update its version
bash kivakit-release-version.sh $VERSION

echo " "
echo "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Release Branch Created  ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓"
echo "┋"
echo "┋  VERSION: $VERSION"
echo "┋"
echo "┋  1. A new release branch 'release/$VERSION' has been created using git flow."
echo "┋  2. POM files and other version-related information in this branch has been updated to $VERSION."
echo "┋  3. When the release branch is FULLY READY, run kivakit-release-finish.sh to merge the branch into master."
echo "┋"
echo "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"
echo " "

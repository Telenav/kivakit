#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

VERSION=$1

cd $KIVAKIT_HOME
git checkout release/$VERSION
git flow release finish $VERSION
git push origin --tags

echo " "
echo "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Release Merged and Published  ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓"
echo "┋"
echo "┋  VERSION: $VERSION"
echo "┋"
echo "┋  1. The branch 'release/$VERSION' has been merged into master using git flow."
echo "┋  2. Artifacts from the release will be published shortly by Jenkins"
echo "┋"
echo "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"
echo " "

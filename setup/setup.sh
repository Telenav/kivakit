#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

if [ -z "$KIVAKIT_WORKSPACE" ]; then
    echo "You must set up your environment to continue setting up KivaKit."
    echo "See http://kivakit.org for details."
    exit 1
fi

#
# Check out required repositories
#

cd $KIVAKIT_WORKSPACE
git clone https://github.com/Telenav/kivakit-build.git
git clone https://github.com/Telenav/lexakai-annotations.git
git clone https://github.com/Telenav/kivakit-data.git
git clone https://github.com/Telenav/kivakit-extensions.git

#
# Initialize git flow for each project
#

cd $KIVAKIT_WORKSPACE/kivakit
git flow init -d
git checkout develop

cd $KIVAKIT_WORKSPACE/kivakit-build
git flow init -d
git checkout develop

cd $KIVAKIT_WORKSPACE/lexakai-annotations
cd lexakai-annotations
git flow init -d

cd $KIVAKIT_WORKSPACE/kivakit-data
git flow init -d
git checkout develop

cd $KIVAKIT_WORKSPACE/kivakit-extensions
git flow init -d
git checkout develop

#
# Build kivakit-build and lexakai-annotations (required to build all projects)
#

cd $KIVAKIT_WORKSPACE/kivakit-build
mvn clean install
cd $KIVAKIT_WORKSPACE/lexakai-annotations
mvn clean install

#
# Build
#

cd $KIVAKIT_HOME
kivakit-build.sh all clean

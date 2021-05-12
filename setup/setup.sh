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
# Check out related repositories
#

cd $KIVAKIT_WORKSPACE
git clone https://github.com/Telenav/kivakit-assets.git
git clone https://github.com/Telenav/kivakit-build.git
git clone https://github.com/Telenav/kivakit-extensions.git

#
# Initialize git flow
#

cd $KIVAKIT_WORKSPACE
git flow init -d
git checkout develop
cd $KIVAKIT_WORKSPACE/kivakit-assets
git flow init -d
git checkout develop
cd $KIVAKIT_WORKSPACE/kivakit-build
git flow init -d
git checkout develop
cd $KIVAKIT_WORKSPACE/kivakit-extensions
git flow init -d
git checkout develop

#
# Check out Lexakai annotations (required to build KivaKit)
#

cd $KIVAKIT_WORKSPACE
git clone https://github.com/Telenav/lexakai-annotations.git
cd lexakai-annotations
mvn clean install

#
# Build
#

cd $KIVAKIT_HOME
kivakit-build.sh all clean

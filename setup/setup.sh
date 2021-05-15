#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

echo " "
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Preparing for Setup ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo " "

if [ -z "$KIVAKIT_WORKSPACE" ]; then
    echo " "
    echo "Please set up your .profile before setting up KivaKit."
    echo "See http://kivakit.org for details."
    echo " "
    exit 1
fi

cd $KIVAKIT_WORKSPACE/kivakit
git checkout -q develop

if [ ! -e "$KIVAKIT_WORKSPACE/kivakit/setup.properties" ]; then
    echo " "
    echo "Please restart your shell before continuing KivaKit setup."
    echo "See http://kivakit.org for details."
    echo " "
    exit 1
fi

#
# Check out required repositories
#

echo " "
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Cloning Repositories ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo " "

cd $KIVAKIT_WORKSPACE
git clone https://github.com/Telenav/cactus-build.git
git clone https://github.com/Telenav/cactus-build-assets.git
git clone https://github.com/Telenav/lexakai-annotations.git
git clone https://github.com/Telenav/kivakit-extensions.git
git clone https://github.com/Telenav/kivakit-assets.git

#
# Initialize git flow for each project
#

echo " "
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Initializing Git Flow ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo " "

cd $KIVAKIT_WORKSPACE/cactus-build
git flow init -d 2>&1 /dev/null

if [ $(git flow config >/dev/null 2>&1) ]; then
    echo " "
    echo "Please install git flow before continuing KivaKit setup."
    echo "See http://kivakit.org for details."
    echo " "
    exit 1
fi

cd $KIVAKIT_WORKSPACE/cactus-build-assets
git flow init -d 2>&1 /dev/null

cd $KIVAKIT_WORKSPACE/lexakai-annotations
git flow init -d 2>&1 /dev/null

cd $KIVAKIT_WORKSPACE/kivakit
git flow init -d 2>&1 /dev/null

cd $KIVAKIT_WORKSPACE/kivakit-assets
git flow init -d 2>&1 /dev/null

cd $KIVAKIT_WORKSPACE/kivakit-extensions
git flow init -d 2>&1 /dev/null

#
# Build
#

echo " "
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Building Projects ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo " "

cd $KIVAKIT_HOME
kivakit-build.sh setup

echo " "
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Setup Complete ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo " "


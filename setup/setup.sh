#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

echo " "
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Preparing for Setup"
echo " "

#
# Ensure KIVAKIT_WORKSPACE variable is set
#

if [ -z "$KIVAKIT_WORKSPACE" ]; then

    echo " "
    echo "Please set up your .profile before setting up KivaKit."
    echo "See https://kivakit.org for details."
    echo " "

    exit 1
fi

#
# Ensure that KivaKit is on the develop branch
#

cd "$KIVAKIT_WORKSPACE"/kivakit

BRANCH="$(git rev-parse --abbrev-ref HEAD)"

if [[ "$BRANCH" != "develop" ]]; then
    
    echo " ";
    echo "Must checkout develop branch to setup KivaKit"
    echo " ";
    
    exit 1;    
fi

#
# Make sure that the shell has been restarted
#

if [ ! -e "$KIVAKIT_WORKSPACE/kivakit/setup.properties" ]; then
    
    date +setup-time=%Y.%m.%d-%I.%M%p > "$KIVAKIT_WORKSPACE"/kivakit/setup.properties

    echo " "
    echo "Please exit your shell program and start it up again before continuing KivaKit setup."
    echo "See https://kivakit.org for details."
    echo " "

    exit 1    
fi

#
# Clone required repositories
#

echo " "
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Cloning Repositories"
echo " "

cd "$KIVAKIT_WORKSPACE"

git clone https://github.com/Telenav/cactus-build-assets.git
git clone https://github.com/Telenav/kivakit-assets.git
git clone https://github.com/Telenav/cactus-build.git
git clone https://github.com/Telenav/lexakai-annotations.git
git clone https://github.com/Telenav/kivakit-extensions.git
git clone https://github.com/Telenav/kivakit-examples.git

#
# Initialize git for each project
#

echo " "
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Initializing Git"
echo " "

cd "$KIVAKIT_WORKSPACE"/cactus-build-assets
echo "Initializing $(pwd)"
git config pull.ff only

cd "$KIVAKIT_WORKSPACE"/kivakit-assets
echo "Initializing $(pwd)"
git config pull.ff only

cd "$KIVAKIT_WORKSPACE"/cactus-build
echo "Initializing $(pwd)"
git config pull.ff only
git flow init -d /dev/null 2>&1

if [ "$(git flow config >/dev/null 2>&1)" ]; then
    echo " "
    echo "Please install git flow before continuing KivaKit setup."
    echo "See https://kivakit.org for details."
    echo " "
    exit 1
fi

cd "$KIVAKIT_WORKSPACE"/lexakai-annotations
echo "Initializing $(pwd)"
git config pull.ff only
git flow init -d /dev/null 2>&1

cd "$KIVAKIT_WORKSPACE"/kivakit
echo "Initializing $(pwd)"
git config pull.ff only
git flow init -d /dev/null 2>&1

cd "$KIVAKIT_WORKSPACE"/kivakit-extensions
echo "Initializing $(pwd)"
git config pull.ff only
git flow init -d /dev/null 2>&1

cd "$KIVAKIT_WORKSPACE"/kivakit-examples
echo "Initializing $(pwd)"
git config pull.ff only
git flow init -d /dev/null 2>&1

#
# Build
#

echo " "
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Building Projects"
echo " "

cd "$KIVAKIT_HOME"
kivakit-build.sh setup

echo " "
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Setup Complete"
echo " "

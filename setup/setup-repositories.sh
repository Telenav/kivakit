#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

#
# Clone required repositories
#

echo " "
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Cloning Repositories"
echo " "

mkdir -p "$TELENAV_WORKSPACE" > /dev/null 2>&1
cd "$TELENAV_WORKSPACE" || exit

git clone https://github.com/Telenav/cactus-build-assets.git
git clone https://github.com/Telenav/cactus-build.git

git clone https://github.com/Telenav/lexakai-annotations.git

[ ! -d "TELENAV_WORKSPACE/kivakit" ] && git clone https://github.com/Telenav/kivakit.git
git clone --depth 1 https://github.com/Telenav/kivakit-assets.git
git clone https://github.com/Telenav/kivakit-extensions.git
git clone https://github.com/Telenav/kivakit-stuff.git
git clone https://github.com/Telenav/kivakit-examples.git

#
# Initialize git for each project
#

echo " "
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Initializing Git"
echo " "

initialize() {

    project_home=$1
    branch=$2

    cd "$project_home" || exit
    echo " "
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━┫ Initializing $(pwd)"
    echo " "
    git checkout "$branch"
    git config pull.ff only

    if [[ $branch == "develop" ]]; then

        git flow init -d /dev/null 2>&1

        if [ "$(git flow config >/dev/null 2>&1)" ]; then
            echo " "
            echo "Please install git flow before continuing setup."
            echo "See https://kivakit.org for details."
            echo " "
            exit 1
        fi

    fi
}

initialize "$TELENAV_WORKSPACE"/cactus-build-assets publish
initialize "$TELENAV_WORKSPACE"/kivakit-assets publish
initialize "$TELENAV_WORKSPACE"/cactus-build develop
initialize "$TELENAV_WORKSPACE"/lexakai-annotations develop
initialize "$TELENAV_WORKSPACE"/kivakit develop
initialize "$TELENAV_WORKSPACE"/kivakit-extensions develop
initialize "$TELENAV_WORKSPACE"/kivakit-stuff develop
initialize "$TELENAV_WORKSPACE"/kivakit-examples develop

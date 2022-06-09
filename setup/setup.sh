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
# Ensure TELENAV_WORKSPACE variable is set
#

if [ -z "$TELENAV_WORKSPACE" ]; then

    echo " "
    echo "Please set up your .profile before setting up KivaKit."
    echo "See https://kivakit.org for details."
    echo " "

    exit 1
fi

#
# Ensure that KivaKit is on the develop branch
#

cd "$TELENAV_WORKSPACE"/kivakit || exit

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

if [ ! -e "$TELENAV_WORKSPACE/kivakit/setup.properties" ]; then
    
    date +setup-time=%Y.%m.%d-%I.%M%p > "$TELENAV_WORKSPACE"/kivakit/setup.properties

    echo " "
    echo "Please exit your shell program and start it up again before continuing KivaKit setup."
    echo "See https://kivakit.org for details."
    echo " "

    exit 1    
fi

#
# Setup required repositories
#

echo " "
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Setting Up Repositories"
echo " "

"$TELENAV_WORKSPACE"/kivakit/setup/setup-repositories.sh

#
# Build
#

echo " "
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Building Projects"
echo " "

cd "$TELENAV_WORKSPACE"/kivakit || exit
kivakit-build.sh setup "$1"

echo " "
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Setup Complete"
echo " "

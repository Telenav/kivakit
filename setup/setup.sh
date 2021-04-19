#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

if [ -z "$KIVAKIT_HOME" ]; then
    echo "You must set up your environment to continue setting up KivaKit."
    echo "See http://kivakit.org for details."
    exit 1
fi

cd $KIVAKIT_WORKSPACE
git clone https://github.com/Telenav/kivakit-data.git
git clone https://github.com/Telenav/kivakit-build.git
git clone https://github.com/Telenav/kivakit-extensions.git

cd $KIVAKIT_HOME
git checkout develop
kivakit-build.sh all clean

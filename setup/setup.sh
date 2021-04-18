#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

if [ -z "$KIVAKIT_HOME" ]; then
    echo "You must set up your environment to use KivaKit."
    echo "See http://tinyurl.com/yxzhpdcp for details."
    exit 1
fi

cd $KIVAKIT_WORKSPACE
git clone git@github.com:Telenav/kivakit-data.git
git clone git@github.com:Telenav/kivakit-build.git
git clone git@github.com:Telenav/kivakit-extensions.git

cd $KIVAKIT_HOME
git checkout develop
kivakit-build.sh all clean

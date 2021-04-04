#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

if [ -z "$KIVAKIT_HOME" ]; then
    echo "You must set up your environment to use KivaKit."
    echo "See https://spaces.telenav.com:8443/x/GCBECw for details."
    exit 1
fi

KIVAKIT_VERSION=$(cat $KIVAKIT_HOME/project.properties | grep "project-version" | cut -d'=' -f2 | xargs echo)

PATTERN="darwin.*"
if [[ $OSTYPE =~ $PATTERN ]]; then
    echo Building for MacOS
    # find . -name \*.java | xargs perl -pi -e 's/\r\n/\n/g'
fi

cd $KIVAKIT_WORKSPACE
git clone git@github.com:Telenav/kivakit-data.git
git clone git@github.com:Telenav/kivakit-build.git
cd $KIVAKIT_HOME
git checkout develop
kivakit-build.sh all clean

#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

NEW_VERSION="${1%-SNAPSHOT}-SNAPSHOT"

if [ -z "$NEW_VERSION" ]; then

    echo "Usage: kivakit-snapshot.sh [new-version-number]"
    exit 0

else

    KIVAKIT_VERSION=$(cat $KIVAKIT_HOME/project.properties | grep "project-version" | cut -d'=' -f2 | xargs echo)

    echo " "
    echo "Updating KivaKit version from $KIVAKIT_VERSION to $NEW_VERSION"

    # Update POM versions
    update-version.pl $KIVAKIT_HOME $KIVAKIT_VERSION $NEW_VERSION

    echo "Updated"
    echo " "

fi

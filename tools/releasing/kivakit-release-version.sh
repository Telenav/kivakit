#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

NEW_VERSION=$1

if [ -z "$NEW_VERSION" ]; then

    echo "Usage: kivakit-release-version.sh [new-version-number]"
    exit 0

else

    CURRENT_VERSION=$(cat $KIVAKIT_HOME/project.properties | grep "project-version" | cut -d'=' -f2 | xargs echo)

    echo " "
    echo "Updating KivaKit version from $CURRENT_VERSION to $NEW_VERSION"

    # Update POM versions and .md files
    update-version.pl $KIVAKIT_HOME $CURRENT_VERSION $NEW_VERSION

    # Update project.properties file
    perl -pi -e "s/$CURRENT_VERSION/$NEW_VERSION/g" $KIVAKIT_HOME/project.properties

    echo "Updated"
    echo " "

fi

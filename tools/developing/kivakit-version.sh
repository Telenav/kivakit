#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

KIVAKIT_VERSION=$(cat $KIVAKIT_HOME/project.properties | grep "project-version" | cut -d'=' -f2 | xargs echo)

KIVAKIT_BUILD_NAME=$(cat $KIVAKIT_HOME/build.properties | grep "build-name" | cut -d'=' -f2 | xargs echo)
KIVAKIT_BUILD_NUMBER=$(cat $KIVAKIT_HOME/build.properties | grep "build-number" | cut -d'=' -f2 | xargs echo)
KIVAKIT_BUILD_DATE=$(cat $KIVAKIT_HOME/build.properties | grep "build-date" | cut -d'=' -f2 | xargs echo)

echo "KivaKit $KIVAKIT_VERSION (#$KIVAKIT_BUILD_NUMBER $KIVAKIT_BUILD_DATE \"$KIVAKIT_BUILD_NAME\")"

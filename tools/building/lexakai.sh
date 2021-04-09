#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

if [ -z "$KIVAKIT_PROJECT_HOME" ]; then
    KIVAKIT_PROJECT_HOME=$KIVAKIT_HOME
fi

PROJECT_VERSION=$(cat $KIVAKIT_PROJECT_HOME/project.properties | grep "project-version" | cut -d'=' -f2 | xargs echo)
KIVAKIT_VERSION=$(cat $KIVAKIT_HOME/project.properties | grep "project-version" | cut -d'=' -f2 | xargs echo)

# -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=1044

java -jar $KIVAKIT_HOME/tools/building/bin/lexakai-0.9.3.jar -project-version=$PROJECT_VERSION -update-readme=true $@

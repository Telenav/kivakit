#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

WORKSPACE=${1%/}

if [ -z "$WORKSPACE" ]; then
    echo "Usage: kivakit-workspace.sh [workspace-folder]"
    exit 1
fi

PATTERN="^/.*"
if [[ ! $WORKSPACE =~ $PATTERN ]]; then
    echo "Problem: workspace folder must be an absolute path"
    echo "Usage: kivakit-workspace.sh [workspace-folder]"
    exit 1
fi

KIVAKIT_VERSION=$(cat $KIVAKIT_HOME/project.properties | grep "project-version" | cut -d'=' -f2 | xargs echo)

if [ ! -z "$KIVAKIT_VERSION" ]; then
    INIT=$TMPDIR/init
    echo "export KIVAKIT_WORKSPACE=\"$WORKSPACE\"; launchctl setenv KIVAKIT_WORKSPACE $KIVAKIT_WORKSPACE;  source ~/.profile" >$INIT
    bash --init-file $INIT
else
    echo "'$WORKSPACE' is not a KivaKit workspace"
fi

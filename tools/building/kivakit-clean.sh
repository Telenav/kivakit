#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

cd $KIVAKIT_WORKSPACE

KIVAKIT_VERSION=$(cat $KIVAKIT_HOME/project.properties | grep "project-version" | cut -d'=' -f2 | xargs echo)

remove() {
    FILE=$1

    echo "Removing files matching $FILE"
    find . -name $FILE | xargs -r rm
}

if [ -d "$HOME/.kivakit/$KIVAKIT_VERSION" ]; then
    read -p "Remove ALL cached files in ~/.kivakit/$KIVAKIT_VERSION (y/n)? " -n 1 -r
    echo " "
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        echo "Removing all files in ~/.kivakit/$KIVAKIT_VERSION"
        rm -rf ~/.kivakit/$KIVAKIT_VERSION
    fi
fi

APPLICATIONS_FOLDER=$KIVAKIT_HOME/tools/applications/bin

read -p "Remove graphs from $APPLICATIONS_FOLDER (y/n)? " -n 1 -r
echo " "
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "Removing files matching $APPLICATIONS_FOLDER/*"
    rm $APPLICATIONS_FOLDER/*
fi

read -p "Remove temporary files (.DS_Store, .metadata, .classpath, .project, *.log, *.hprof, *~) from source tree (y/n)? " -n 1 -r
echo " "
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "Removing temporary files in $KIVAKIT_HOME"
    remove '.DS_Store'
    remove '.metadata'
    remove '.classpath'
    remove '.project'
    remove 'Err.log'
    remove 'Err.log.*'
    remove '*.hprof'
    remove '*~'
fi

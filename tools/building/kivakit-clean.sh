#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

cd $KIVAKIT_WORKSPACE

KIVAKIT_VERSION=$(cat $KIVAKIT_HOME/project.properties | grep "project-version" | cut -d'=' -f2 | xargs echo)

if [ -d "$HOME/.kivakit/$KIVAKIT_VERSION" ]; then
    read -p "┋ Remove ALL cached files in ~/.kivakit/$KIVAKIT_VERSION (y/n)? " -n 1 -r
    echo "┋ "
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        rm -rf ~/.kivakit/$KIVAKIT_VERSION
    fi
fi

read -p "┋ Remove temporary files (.DS_Store, .metadata, .classpath, .project, *.hprof, *~) from source tree (y/n)? " -n 1 -r
echo " "
echo "┋ "
if [[ $REPLY =~ ^[Yy]$ ]]; then
    find $KIVAKIT_HOME \( -name \.DS_Store -o -name \.metadata -o -name \.classpath -o -name \.project -o -name \*\.hprof -o -name \*~ \) | xargs rm
fi

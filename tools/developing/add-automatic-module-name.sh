#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

AUTOMATIC_MODULE_NAME=$1
JAR=$2

if [ -z "$AUTOMATIC_MODULE_NAME" ]; then
    echo "Usage: [automatic-module-name] [jar-file]"
    exit 1
fi

if [ -z "$JAR" ]; then
    echo "Usage: [automatic-module-name] [jar-file]"
    exit 1
fi

echo "Automatic-Module-Name: $AUTOMATIC_MODULE_NAME\n" >$TMPDIR/manifest.txt

jar --update --file $JAR --manifest=$TMPDIR/manifest.txt

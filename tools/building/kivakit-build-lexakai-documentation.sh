#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

cd $KIVAKIT_WORKSPACE

# Build .puml files and README.md indexes with Lexakai
bash lexakai.sh $KIVAKIT_HOME

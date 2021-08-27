#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source library-functions.sh
source kivakit-projects.sh

version="$1"

require_variable version "[version]"

snapshot_version="${1%-SNAPSHOT}-SNAPSHOT"

update_version $KIVAKIT_HOME $snapshot_version
update_version $KIVAKIT_EXTENSIONS_HOME $snapshot_version
update_version $KIVAKIT_EXAMPLES_HOME $snapshot_version

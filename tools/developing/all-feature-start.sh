#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source kivakit-library-functions.sh
source kivakit-projects.sh

help="[feature-name]"

feature_name=$1

require_variable feature_name "$help"

bash kivakit-feature-start.sh "$feature_name"
bash lexakai-feature-start.sh "$feature_name"
bash mesakit-feature-start.sh "$feature_name"

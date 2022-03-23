#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source kivakit-library-functions.sh
source kivakit-projects.sh

help="[version]"

version=$1

require_variable version "$help"

if [ "$KIVAKIT_VERSION" = "$version" ]; then

    echo " "
    echo "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Building Release  ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓"
    echo "┋"
    echo "┋  Release Version: $version"
    echo "┋"
    echo "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"
    echo " "

    if yes_no "Maven repository and .kivakit/$version folders must be removed to build a release. Remove them?"; then

        rm -rf ~/.m2/repository
        rm -rf ~/.kivakit/"$version"

    fi

    bash kivakit-build.sh deploy-local # single-threaded

    echo " "
    echo "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Release Built  ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓"
    echo "┋"
    echo "┋  1. The project and its documentation have been built"
    echo "┋  2. The project has been re-built from scratch and deployed to the local repository"
    echo "┋"
    echo "┋  Next Steps:"
    echo "┋"
    echo "┋  1. Check the release/$version branch carefully to make sure it's ready to go"
    echo "┋  2. Run kivakit-release-finish.sh $version"
    echo "┋"
    echo "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"
    echo " "

else

    echo " "
    echo "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Creating Release  ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓"
    echo "┋"
    echo "┋  Current Version: $KIVAKIT_VERSION"
    echo "┋  Release Version: $version"
    echo "┋"
    echo "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"

    if kivakit-release-start.sh "$version"; then

        echo " "
        echo "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Release Created  ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓"
        echo "┋"
        echo "┋  1. The branch release/$version has been created with git flow "
        echo "┋  2. Build files have been updated from $KIVAKIT_VERSION to $version"
        echo "┋"
        echo "┋  EXIT YOUR TERMINAL PROGRAM ENTIRELY and restart it, then:"
        echo "┋"
        echo "┋  1. Check the release"
        echo "|  2. Update change-log.md"
        echo "┋  3. Update codeflowers"
        echo "┋  4. Rerun this script: $(basename "$0") $1"
        echo "┋"
        echo "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"
        echo " "

    fi

fi


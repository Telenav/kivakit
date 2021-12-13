#!/bin/bash

cd "${KIVAKIT_EXTENSIONS_HOME}/kivakit-merged/" || exit

if [ ! -e "${HOME}/.m2/repository/com/telenav/kivakit/kivakit-merged" ]; then

    echo "Installing merged jars"

    cd "${KIVAKIT_EXTENSIONS_HOME}/kivakit-merged" || exit
    mvn --batch-mode clean install

fi

#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

#
# Environment
#

ROOT="$(pwd)"
BRANCH="${GITHUB_REF//refs\/heads\//}"
SUPERPOM_INSTALL="mvn --batch-mode --no-transfer-progress clean install"
BUILD="mvn -Dmaven.javadoc.skip=true -DKIVAKIT_DEBUG="!Debug" -P shade -P tools --no-transfer-progress --batch-mode clean install"
CLONE="git clone --branch "$BRANCH" --quiet"
DEPLOY="mvn -P attach-jars -P sign-artifacts -P shade -P tools --no-transfer-progress --batch-mode -Dgpg.passphrase=${{ secrets.OSSRH_GPG_SECRET_KEY_PASSWORD }} clean deploy"

if [[ "$1" == "deploy" ]]; then
    BUILD=$DEPLOY
fi

#
# Build kivakit
#

echo "Cloning kivakit"
cd "$ROOT"
$CLONE https://github.com/Telenav/kivakit.git

echo "Installing kivakit super POM"
cd "$ROOT"/kivakit/superpom
$SUPERPOM_INSTALL

echo "Building kivakit"
cd "$ROOT"/kivakit
$BUILD

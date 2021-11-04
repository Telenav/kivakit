#!/bin/bash

cd "${KIVAKIT_EXTENSIONS_HOME}/kivakit-merged-jars/"

if [ ! -e "${HOME}/.m2/repository/com/telenav/kivakit/kivakit-grpc-merged/${KIVAKIT_VERSION}/kivakit-grpc-merged-${KIVAKIT-VERSION}.jar" ]; then

    echo "Installing GRPC merged JAR"

    cd "${KIVAKIT_EXTENSIONS_HOME}/kivakit-merged-jars/kivakit-grpc-merged"
    mvn --batch-mode clean install

fi

if [ ! -e "${HOME}/.m2/repository/com/telenav/kivakit/kivakit-protostuff-merged/${KIVAKIT_VERSION}/kivakit-protostuff-merged-${KIVAKIT_VERSION}.jar" ]; then

    echo "Installing Protostuff merged JAR"

    cd "${KIVAKIT_EXTENSIONS_HOME}/kivakit-merged-jars/kivakit-protostuff-merged"
    mvn --batch-mode clean install

fi

if [ ! -e "${HOME}/.m2/repository/com/telenav/kivakit/kivakit-prometheus-merged/${KIVAKIT_VERSION}/kivakit-prometheus-merged-${KIVAKIT_VERSION}.jar" ]; then

    echo "Installing Prometheus merged JAR"

    cd "${KIVAKIT_EXTENSIONS_HOME}/kivakit-merged-jars/kivakit-prometheus-merged"
    mvn --batch-mode clean install

fi

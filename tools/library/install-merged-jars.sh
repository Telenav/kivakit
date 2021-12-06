#!/bin/bash

cd "${KIVAKIT_EXTENSIONS_HOME}/kivakit-merged/" || exit

if [ ! -e "${HOME}/.m2/repository/com/telenav/kivakit/kivakit-merged-grpc/${KIVAKIT_VERSION}/kivakit-merged-grpc-${KIVAKIT-VERSION}.jar" ]; then

    echo "Installing GRPC merged JAR"

    cd "${KIVAKIT_EXTENSIONS_HOME}/kivakit-merged/grpc" || exit
    mvn --batch-mode clean install

fi

if [ ! -e "${HOME}/.m2/repository/com/telenav/kivakit/kivakit-merged-protostuff/${KIVAKIT_VERSION}/kivakit-merged-protostuff-${KIVAKIT_VERSION}.jar" ]; then

    echo "Installing Protostuff merged JAR"

    cd "${KIVAKIT_EXTENSIONS_HOME}/kivakit-merged/protostuff" || exit
    mvn --batch-mode clean install

fi

if [ ! -e "${HOME}/.m2/repository/com/telenav/kivakit/kivakit-merged-prometheus/${KIVAKIT_VERSION}/kivakit-merged-prometheus-${KIVAKIT_VERSION}.jar" ]; then

    echo "Installing Prometheus merged JAR"

    cd "${KIVAKIT_EXTENSIONS_HOME}/kivakit-merged/prometheus" || exit
    mvn --batch-mode clean install

fi

if [ ! -e "${HOME}/.m2/repository/com/telenav/kivakit/kivakit-merged-zookeeper/${KIVAKIT_VERSION}/kivakit-merged-zookeeper-${KIVAKIT_VERSION}.jar" ]; then

    echo "Installing Zookeeper merged JAR"

    cd "${KIVAKIT_EXTENSIONS_HOME}/kivakit-merged/zookeeper" || exit
    mvn --batch-mode clean install

fi

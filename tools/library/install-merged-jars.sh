#!/bin/bash

cd "${KIVAKIT_EXTENSIONS_HOME}/kivakit-merged-jars/"

if [ ! -e "${HOME}/.m2/repository/com/telenav/kivakit/kivakit-grpc-merged/1.1.0/kivakit-grpc-merged-1.1.0.jar" ]; then

    echo "Installing GRPC merged JAR"

    mvn install:install-file \
        -DgroupId=com.telenav.kivakit \
        -DartifactId=kivakit-grpc-merged \
        -Dfile="${KIVAKIT_EXTENSIONS_HOME}"/kivakit-merged-jars/lib/kivakit-grpc-merged-1.1.0.jar \
        -Dversion=1.1.0 \
        -Dpackaging=jar

fi

if [ ! -e "${HOME}/.m2/repository/com/telenav/kivakit/kivakit-protostuff-merged/1.1.0/kivakit-protostuff-merged-1.1.0.jar" ]; then

    echo "Installing Protostuff merged JAR"

    mvn install:install-file \
        -DgroupId=com.telenav.kivakit \
        -DartifactId=kivakit-protostuff-merged \
        -Dfile="${KIVAKIT_EXTENSIONS_HOME}"/kivakit-merged-jars/lib/kivakit-protostuff-merged-1.1.0.jar \
        -Dversion=1.1.0 \
        -Dpackaging=jar

fi

if [ ! -e "${HOME}/.m2/repository/com/telenav/kivakit/kivakit-prometheus-merged/1.1.0/kivakit-prometheus-merged-1.1.0.jar" ]; then

    echo "Installing Prometheus merged JAR"

    mvn install:install-file \
        -DgroupId=com.telenav.kivakit \
        -DartifactId=kivakit-prometheus-merged \
        -Dfile="${KIVAKIT_EXTENSIONS_HOME}"/kivakit-merged-jars/lib/kivakit-prometheus-merged-1.1.0.jar \
        -Dversion=1.1.0 \
        -Dpackaging=jar

fi

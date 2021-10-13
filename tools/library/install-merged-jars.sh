#!/bin/bash

echo "Installing merged JAR files"
mvn install:install-file \
    -DgroupId=com.telenav.kivakit \
    -DartifactId=kivakit-protostuff-merged \
    -Dfile="${KIVAKIT_EXTENSIONS_HOME}"/kivakit-merged-jars/lib/kivakit-grpc-merged-1.1.0.jar \
    -Dversion=1.1.0 \
    -Dpackaging=jar

mvn install:install-file \
    -DgroupId=com.telenav.kivakit \
    -DartifactId=kivakit-protostuff-merged \
    -Dfile="${KIVAKIT_EXTENSIONS_HOME}"/kivakit-merged-jars/lib/kivakit-protostuff-merged-1.1.0.jar \
    -Dversion=1.1.0 \
    -Dpackaging=jar

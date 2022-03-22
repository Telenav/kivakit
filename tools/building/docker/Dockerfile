
FROM ubuntu

# Get KivaKit version

ARG ENV_KIVAKIT_VERSION

# Locations

ENV HOME=/root
ENV DEVELOPER=$HOME/developer
ENV M2_HOME="$DEVELOPER/apache-maven-3.8.4"
ENV JAVA_HOME="$DEVELOPER/jdk-17.0.2"
ENV KIVAKIT_VERSION=${ENV_KIVAKIT_VERSION}
ENV KIVAKIT_WORKSPACE=$HOME/workspace
ENV KIVAKIT_HOME="${KIVAKIT_WORKSPACE}/kivakit"
ENV PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/bin:$DEVELOPER/protoc/bin:$M2_HOME/bin:$JAVA_HOME/bin

RUN mkdir -p $DEVELOPER && \
    mkdir -p $JAVA_HOME && \
    mkdir -p $M2_HOME && \
    mkdir -p $KIVAKIT_HOME && \
    mkdir -p $DEVELOPER/.m2 && \
    mkdir -p $DEVELOPER/.kivakit && \
    ln -s $DEVELOPER/.m2 $HOME/.m2 && \
    ln -s $DEVELOPER/.kivakit $HOME/.kivakit

# Install OS tools

ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && \
    apt-get install -y git perl wget unzip emacs git-flow

# Install Java

RUN cd "$DEVELOPER" && \
    wget --no-verbose https://download.java.net/java/GA/jdk17.0.2/dfd4a8d0985749f896bed50d7138ee7f/8/GPL/openjdk-17.0.2_linux-x64_bin.tar.gz && \
    tar xvf openjdk-17.0.2_linux-x64_bin.tar.gz && \
    rm openjdk-17.0.2_linux-x64_bin.tar.gz

# Install protoc

RUN cd $DEVELOPER && \
    wget --no-verbose https://github.com/protocolbuffers/protobuf/releases/download/v3.15.8/protoc-3.15.8-linux-x86_64.zip && \
    unzip protoc-3.15.8-linux-x86_64.zip -d $DEVELOPER/protoc && \
    ln -s $DEVELOPER/protoc/bin/protoc /usr/bin/protoc && \
    rm protoc-3.15.8-linux-x86_64.zip

# Install maven

RUN cd "$DEVELOPER" && \
    wget --no-verbose https://archive.apache.org/dist/maven/maven-3/3.8.4/binaries/apache-maven-3.8.4-bin.tar.gz && \
    tar xvf apache-maven-3.8.4-bin.tar.gz && \
    rm apache-maven-3.8.4-bin.tar.gz

# Clone kivakit:develop

RUN cd "$KIVAKIT_WORKSPACE" && \
    git clone https://github.com/Telenav/kivakit.git && \
    cd "$KIVAKIT_HOME" && \
    git checkout develop

# Prepare to setup

RUN date +setup-time=%Y.%m.%d-%I.%M%p > ${KIVAKIT_HOME}/setup.properties

COPY profile $HOME/.profile

RUN bash -c "source $HOME/.profile && $KIVAKIT_HOME/setup/setup.sh docker"

COPY kivakit-entrypoint.sh $HOME/kivakit-entrypoint.sh
RUN chmod +x $HOME/kivakit-entrypoint.sh
ENTRYPOINT $HOME/kivakit-entrypoint.sh

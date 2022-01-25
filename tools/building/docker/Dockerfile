
FROM openjdk:17-alpine

ARG KIVAKIT_VERSION

ENV HOME=/root
ENV JAVA_HOME="/opt/openjdk-17"
ENV M2_HOME="$HOME/apache-maven-3.8.4"

ENV KIVAKIT_VERSION=${KIVAKIT_VERSION}
ENV KIVAKIT_WORKSPACE=$HOME/workspace
ENV KIVAKIT_HOME="${KIVAKIT_WORKSPACE}/kivakit"

RUN mkdir -p $KIVAKIT_HOME

ENV PATH=$PATH:/usr/bin

RUN apk update

RUN apk add git git-flow bash perl grep

RUN cd "$HOME" && \
    wget https://dlcdn.apache.org/maven/maven-3/3.8.4/binaries/apache-maven-3.8.4-bin.tar.gz && \
    tar xvf apache-maven-3.8.4-bin.tar.gz

RUN cd "$KIVAKIT_WORKSPACE" && git clone https://github.com/Telenav/kivakit.git
RUN cd "$KIVAKIT_HOME" && git checkout develop

RUN date +setup-time=%Y.%m.%d-%I.%M%p > ${KIVAKIT_HOME}/setup.properties

COPY profile $HOME/.profile

RUN bash -c "source $HOME/.profile && $KIVAKIT_HOME/setup/setup.sh"

COPY entrypoint.sh $HOME/entrypoint.sh
RUN chmod +x $HOME/entrypoint.sh
ENTRYPOINT $HOME/entrypoint.sh

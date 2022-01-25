#!/bin/bash

cd "$KIVAKIT_HOME"/tools/building/docker || exit

docker run -ti kivakit:latest /bin/bash

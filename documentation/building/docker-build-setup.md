# KivaKit - Docker Build Setup   <img src="https://www.kivakit.org/images/box-32.png" srcset="https://www.kivakit.org/images/box-32-2x.png 2x"/>

Docker makes it quick and easy to build KivaKit in any environment, without software setup hassles.

### Building KivaKit in Docker

Building KivaKit in Docker is a snap:

1. [Install docker](https://docs.docker.com/get-docker/)


2. If you have *NOT* [set up a local build](build-setup.md) already, choose a workspace:

       export KIVAKIT_WORKSPACE=~/workspaces/kivakit

   and check out a fresh set of KivaKit repositories:

       mkdir -p $KIVAKIT_WORKSPACE 
       cd $KIVAKIT_WORKSPACE
       git clone --branch develop https://github.com/Telenav/kivakit.git
       bash $KIVAKIT_WORKSPACE/setup/setup-repositories.sh


3. Next, launch the KivaKit build environment. If you have a local build set up, you can use the *kivakit-docker-run.sh* script. If you don't have a local build, set this variable to an image tag from [Docker Hub](https://hub.docker.com/repository/docker/jonathanlocke/kivakit):

       export KIVAKIT_BUILD_IMAGE=1.2.3-snapshot

   and launch the build environment like this:

       docker run \
           --volume "$KIVAKIT_WORKSPACE:/host/workspace" \
           --volume "$HOME/.m2:/host/.m2" \
           --volume "$HOME/.kivakit:/host/.kivakit" \
           --interactive --tty "jonathanlocke/kivakit:$KIVAKIT_BUILD_IMAGE" \
           /bin/bash

   > The volume mounts here make the host workspace ($KIVAKIT_WORKSPACE) and cache
   > folders ($HOME/.m2, $HOME/.kivakit) visible in Docker under /host. This makes it
   > possible for Docker to build your host workspace, which is useful when
   > working with an IDE.


4. The source code now can be built with:

       kivakit-build.sh

   and updated as desired with git.


5. Use the scripts in the table below to build KivaKit on your host or in Docker.


6. To switch to your host workspace (to work with an IDE):

       kivakit-docker-workspace.sh host

   Host locations:

    * KIVAKIT_WORKSPACE => /host/workspace
    * /root/.m2 => /host/.m2
    * /root/.kivakit => /host/.kivakit


7. To switch your workspace back to Docker:

       kivakit-docker-workspace.sh docker

   Docker locations:

    * KIVAKIT_WORKSPACE => /root/workspace
    * /root/.m2 => /root/developer/.m2
    * /root/.kivakit => /root/developer/.kivakit

### KivaKit Build Scripts

| KivaKit Script                      | Purpose                                   | 
|-------------------------------------|-------------------------------------------|
| kivakit-\[tab]                      | see available kivakit shell scripts       |
| kivakit-version.sh                  | show kivakit version                      |
| kivakit-build.sh                    | build kivakit                             |
| kivakit-git-pull.sh                 | pull changes **                           |
| kivakit-git-checkout.sh \[branch]   | check out the given branch **             |
| kivakit-docker-workspace.sh         | switch between host and docker workspaces |
| kivakit-feature-start.sh \[branch]  | start a feature branch **                 |
| kivakit-feature-finish.sh \[branch] | finish a feature branch **                |

** executes the command in each kivakit repository

# KivaKit - Docker Build Environment   <img src="https://www.kivakit.org/images/box-32.png" srcset="https://www.kivakit.org/images/box-32-2x.png 2x"/>

Docker makes it quick and easy to build KivaKit in any environment, without software setup hassles.

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Building KivaKit in Docker

Building KivaKit in Docker is a snap:

1. Install [Docker Desktop](https://docs.docker.com/get-docker/) and ensure that your home folder can be shared under Settings / Resources / File Sharing


2. If you have not already [set up a local build environment](host-build-environment.md) that you would prefer to use, check out a fresh set of KivaKit repositories by pasting this into a terminal window:

       export KIVAKIT_WORKSPACE=~/workspaces/kivakit
       mkdir -p $KIVAKIT_WORKSPACE 
       cd $KIVAKIT_WORKSPACE
       git clone --branch develop https://github.com/Telenav/kivakit.git
       bash $KIVAKIT_WORKSPACE/kivakit/setup/setup-repositories.sh


3. Next, launch the KivaKit build environment. If you are using build environment on your local host, you can use the *kivakit-docker-build.sh* script. If you don't have a local build, set this variable to an [Docker image tag](https://hub.docker.com/repository/docker/jonathanlocke/kivakit):

       export KIVAKIT_BUILD_IMAGE=1.2.3-snapshot

   then launch a Docker build environment like this:

       docker run \
           --volume "$KIVAKIT_WORKSPACE:/host/workspace" \
           --volume "$HOME/.m2:/host/.m2" \
           --volume "$HOME/.kivakit:/host/.kivakit" \
           --interactive --tty "jonathanlocke/kivakit:$KIVAKIT_BUILD_IMAGE" \
           /bin/bash

   > **NOTE:** The volume mounts here make the host workspace ($KIVAKIT_WORKSPACE) and cache
   > folders ($HOME/.m2, $HOME/.kivakit) visible in Docker under /host. This makes it
   > possible for Docker to build the workspace on your host, which is helpful when
   > working with an IDE.


4. Use the scripts in the table below to build KivaKit.


5. To switch from the Docker workspace (default) to your host workspace:

       kivakit-docker-build-workspace.sh host

   Host locations:

    * KIVAKIT_WORKSPACE => /host/workspace
    * /root/.m2 => /host/.m2
    * /root/.kivakit => /host/.kivakit


6. To switch your workspace back to Docker:

       kivakit-docker-build-workspace.sh docker

   Docker locations:

    * KIVAKIT_WORKSPACE => /root/workspace
    * /root/.m2 => /root/developer/.m2
    * /root/.kivakit => /root/developer/.kivakit

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### KivaKit Build Scripts

| KivaKit Script                      | Purpose                                   | 
|-------------------------------------|-------------------------------------------|
| kivakit-\[tab]                      | see available kivakit shell scripts       |
| kivakit-version.sh                  | show kivakit version                      |
| kivakit-build.sh                    | build kivakit                             |
| kivakit-git-pull.sh                 | pull changes **                           |
| kivakit-git-checkout.sh \[branch]   | check out the given branch **             |
| kivakit-docker-build-workspace.sh   | switch between host and docker workspaces |
| kivakit-feature-start.sh \[branch]  | start a feature branch **                 |
| kivakit-feature-finish.sh \[branch] | finish a feature branch **                |

** executes the command in each kivakit repository

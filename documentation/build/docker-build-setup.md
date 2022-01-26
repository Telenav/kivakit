# KivaKit - Docker Build Setup   <img src="https://www.kivakit.org/images/box-32.png" srcset="https://www.kivakit.org/images/box-32-2x.png 2x"/>

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Building KivaKit in Docker

Building KivaKit in Docker is a snap:

1. [Install docker](https://docs.docker.com/get-docker/)
2. In a shell window on your host:

       docker -it jonathanlocke/kivakit:[version]

   The [KivaKit Docker build environment image]( https://hub.docker.com/repository/docker/jonathanlocke/kivakit) of the specified version will launch with the *develop* branch checked out. The source code can be built with:

       kivakit-build.sh

   and updated as desired with git. The scripts *kivakit-git-pull.sh* and *kivakit-git-checkout.sh* conveniently operate across all kivakit repositories.

3. Use the scripts in the table below to build KivaKit on your host or in the Docker container. To switch your build workspace from the container (/root/workspace) to your host (/host/workspace), execute the command:

       kivakit-docker-workspace.sh host

   to switch back to the container workspace:

       kivakit-docker-workspace.sh container

   > **NOTE**
   >
   > To switch to the host workspace, it must be mounted as a volume in the container like this:
   >
   >     docker -v "$KIVAKIT_WORKSPACE:/host/workspace" [...]
   >
   > For convenience, the script *kivakit-docker-run.sh* launches docker with this volume mounted.

### KivaKit Scripts

| KivaKit Script                                    | Purpose                                      |
|---------------------------------------------------|----------------------------------------------|
| kivakit-\[tab\]                                   | see available kivakit shell scripts          |
| kivakit-version.sh                                | show kivakit version                         |
| kivakit-build.sh                                  | build kivakit                                |
| kivakit-git-pull.sh                               | pull changes **                              |
| kivakit-git-checkout.sh \[branch\]                | check out the given branch **                |
| kivakit-docker-workspace.sh \[host or container\] | switch between host and container workspaces |
| kivakit-feature-start.sh \[branch\]               | start a feature branch **                    |
| kivakit-feature-finish.sh \[branch\]              | finish a feature branch **                   |

** executes the command in each kivakit repository

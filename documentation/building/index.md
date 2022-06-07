# KivaKit - Building   <img src="https://telenav.github.io/telenav-assets/images/iconsgears-32.png" srcset="https://telenav.github.io/telenav-assets/images/iconsgears-32-2x.png 2x"/>

Whether you plan to use KivaKit or help develop it, this page can help get your build rolling.

<img src="https://telenav.github.io/telenav-assets/images/iconshorizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/png/separators/horizontal-line-512-2x.png 2x"/>

### Building KivaKit

KivaKit can be built by following these steps:

1. [Launch a Docker build environment](docker-build-environment.md) or [set one up on your host](host-build-environment.md)


2. Build KivaKit with *kivakit-build.sh*


3. Import the repositories *kivakit* and *kivakit-extensions* into [IntelliJ](https://www.jetbrains.com/idea/download/) as modules, refresh the Maven projects tab, and choose "Rebuild Project" from the "Build" menu.

<img src="https://telenav.github.io/telenav-assets/images/iconshorizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/png/separators/horizontal-line-128-2x.png 2x"/>

### Key Build Scripts

| Script                           | Purpose                                                                   |
|----------------------------------|---------------------------------------------------------------------------|
| *kivakit-version.sh*             | Shows the version of KivaKit you are building                             |
| *telenav-build.sh*               | Builds KivaKit using the given build type and build modifiers (see below) |
| *telenav-build-documentation.sh* | Builds Javadoc and Lexakai documentation into *kivakit-assets*            |

KivaKit scripts are named so that you can easily discover them with command-line completion.

To see what scripts are available, type "kivakit-" and hit TAB.

<img src="https://telenav.github.io/telenav-assets/images/iconshorizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/png/separators/horizontal-line-128-2x.png 2x"/>

### kivakit-build.sh

The *kivakit-build.sh* script takes a build-type parameter and zero or more build-modifier parameters. These parameters are translated into a particular set of maven switches and arguments. To see what build types are available, run *kivakit-build.sh help*:

    Usage: kivakit-build.sh [build-type] [build-modifiers]*
    
    Build types:
    
           [default] - compile, shade and run all tests
    
                 all - clean-all, compile, shade, run tests, build tools and javadoc
    
             compile - compile and shade (no tests)
    
        deploy-ossrh - clean-sparkling, compile, run tests, attach jars, build javadoc, sign artifacts and deploy to OSSRH
    
        deploy-local - clean-sparkling, compile, run tests, attach jars, build javadoc, sign artifacts and deploy to local Maven repository
    
               tools - compile, shade, run tests, build tools
    
                 dmg - compile, shade, run tests, build tools, build dmg
    
             javadoc - compile and build javadoc
    
    Build modifiers:
    
         attach-jars - attach source and javadoc jars to maven artifacts
    
               clean - prompt to remove cached and temporary files
    
           clean-all - prompt to remove cached and temporary files and kivakit artifacts from ~/.m2
    
    clean-sparkling - prompt to remove entire .m2 repository and all cached and temporary files
    
               debug - turn maven debug mode on
    
         debug-tests - stop in debugger on surefire tests
    
             dry-run - show maven command line but don't build
    
          no-javadoc - do not build javadoc
    
            no-tests - do not run tests
    
         quick-tests - run only quick tests
    
               quiet - build with minimal output
    
    single-threaded - build with only one thread
    
               tests - run all tests

<br/> 

<img src="https://telenav.github.io/telenav-assets/images/iconshorizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/png/separators/horizontal-line-512-2x.png 2x"/>

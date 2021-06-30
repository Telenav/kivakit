# KivaKit - Building   <img src="https://www.kivakit.org/images/gears-32.png" srcset="https://www.kivakit.org/images/gears-32-2x.png 2x"/>

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Building KivaKit

KivaKit can be built in [IntelliJ](https://www.jetbrains.com/idea/download/) or from the command line with Maven or the convenient scripts below.

### Key Build Scripts

Once you have completed the [Setup](setup.md) process, it is easy to build projects from the command line with **kivakit-build.sh**.

| Script | Purpose |
|--------|---------|
| *kivakit-build.sh* | Builds KivaKit using the givens build type and modifiers (see below) |
| *kivakit-build-documentation.sh* | Builds Javadoc and Lexakai documentation and uploads Javadoc to S3 |
| *kivakit-version.sh* | Shows the version of KivaKit you are building |

KivaKit scripts are named so that you can easily discover them with command-line completion.

To see what scripts are available, type "kivakit" and hit tab.

### Build Parameters

The **kivakit-build.sh** script takes a build-type parameter and zero or more build-modifier parameters. These parameters are translated into a particular set of maven switches and arguments. To see what build types are available, run *kivakit-build.sh help*:

Usage: kivakit-build.sh *[build-type] [build-modifiers]*

**Build Types**:

           [default] - compile, shade and run quick tests

                 all - all-clean, compile, shade, run tests, build tools and javadoc

               tools - compile, shade, run tests, build tools

             compile - compile and shade (no tests)

             javadoc - compile and build javadoc

**Build Modifiers**:

               clean - prompt to remove cached and temporary files

           all-clean - prompt to remove cached and temporary files and kivakit artifacts from ~/.m2

               debug - turn maven debug mode on

         debug-tests - stop in debugger on surefire tests

          no-javadoc - do not build javadoc

            no-tests - do not run tests

    single-threaded - build with only one thread

         quick-tests - run only quick tests

               quiet - build with minimal output

                show - show maven command line but don't build

           sparkling - prompt to remove entire .m2 repository and all cached and temporary files

               tests - run all tests

<br/> 

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

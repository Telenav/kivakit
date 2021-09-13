# KivaKit - Build Setup   <img src="https://www.kivakit.org/images/box-32.png" srcset="https://www.kivakit.org/images/box-32-2x.png 2x"/>

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

## Setting Up to Build KivaKit

Whether you plan to use KivaKit or help to develop it, this page will help you get rolling in 3 easy steps.

### Prerequisites

You will need these products, or compatible products, set up to begin:

1. Latest Git
2. Latest GitFlow. To install on macOS, install *Homebrew* and then use that to install git-flow:

       /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
       brew install git-flow

2. [Open JDK Java 16](https://adoptopenjdk.net/?variant=openjdk16&jvmVariant=hotspot) 

   > **IMPORTANT NOTE**: At this time, even though KivaKit targets Java 11 bytecode, it is not possible to build KivaKit under Java 11 due to a bug in the compiler ([JDK-8254557](https://bugs.openjdk.java.net/browse/JDK-8254557)). It is also not possible to build KivaKit's documentation under Java 12 due to a bug in Javadoc ([JDK-8212233](https://bugs.openjdk.java.net/browse/JDK-8212233)). Therefore, until these fixes are back-ported to Java 11, it is recommended to build KivaKit with **Java 16**, where both of these bugs have been fixed.
   >
   > 

3. Latest [Maven](https://maven.apache.org/download.cgi)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### 1. Checking Out the Project   <img src="https://www.kivakit.org/images/down-arrow-32.png" srcset="https://www.kivakit.org/images/down-arrow-32-2x.png 2x"/>

The KivaKit project will look roughly like this when we're done with the setup process:

    └── Workspace
        ├── kivakit
        │   ├── kivakit-application
        │   ├── kivakit-collections
        │   ├── kivakit-commandline
        │   └── [...]
        ├── kivakit-extensions
        │   ├── kivakit-data
        │   ├── kivakit-filesystems
        │   ├── kivakit-logs
        │   └── [...]
        └── kivakit-examples
            └── [...]

Notice how the *kivakit* project (**KIVAKIT_HOME**) is checked out in the IDE workspace called
*Workspace* (**KIVAKIT_WORKSPACE**).

To check out the *kivakit* project:

1. If you're running macOS, and you want to switch your shell from *zsh* to *bash*, type:

       chsh -s /bin/bash

2. Open a *bash* shell and go to your IDE workspace (the folder *Workspace* above)
3. Clone the *kivakit* git repository into your workspace

       cd Workspace 
       git clone https://github.com/Telenav/kivakit.git

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### 2. Setting Up Your Environment   <img src="https://www.kivakit.org/images/box-32.png" srcset="https://www.kivakit.org/images/box-32-2x.png 2x"/>

To configure your environment, you will need several environment variables set.

> Without the correct environment variables set, the setup script in Step 3 won't work.

### MacOS and UNIX    <img src="https://www.kivakit.org/images/bluebook-32.png" srcset="https://www.kivakit.org/images/bluebook-32-2x.png 2x"/>

On macOS or UNIX, you can use a sample *.profile* from KivaKit to configure your shell environment.

1. Install the KivaKit *profile* sample from the *kivakit* project as shown here (or merge it into your existing .profile):

       cp kivakit/setup/profile $HOME/.profile

2. Make sure that the **M2_HOME** and **JAVA_HOME** environment variables defined in the *.profile*
   point to your Maven and Java installations

       export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-16.0.2_7.jdk/Contents/Home 
       export M2_HOME=$HOME/Developer/apache-maven-3.6.3

3. Change the **KIVAKIT_WORKSPACE** variable to point to your IDE workspace (**Workspace**).

       export KIVAKIT_WORKSPACE=$HOME/Workspace

   > **KIVAKIT_WORKSPACE** must point to your *IDE workspace* **NOT** the *kivakit* project in the workspace

4. Exit your terminal program entirely (not just your shell window)

5. Restart the terminal program and open a new shell window

### Windows &nbsp;&nbsp; <img src="https://www.kivakit.org/images/window-32.png" srcset="https://www.kivakit.org/images/window-32-2x.png 2x"/>

On Windows, a UNIX-like environment with *bash* is required. It is recommended to install [Git for Windows](https://gitforwindows.org/), which will install both git, and a bash shell.

1. Install KivaKit *.profile* sample from the *kivakit* project as shown here
   (or merge it into your existing .profile):

       cp kivakit/setup/profile $HOME/.profile

2. Make sure that the **M2_HOME** and **JAVA_HOME** environment variables defined in the *.profile*
   point to your Maven and Java installations

       export JAVA_HOME=/c/Program\\ Files/Java/jdk-16.0.2_7  
       export M2_HOME=$HOME/Tools/apache-maven-3.6.3

3. Change the **KIVAKIT_WORKSPACE** variable to point to your IDE workspace (**$HOME/Workspace**).
   _Do not set KIVAKIT_HOME._

       export KIVAKIT_WORKSPACE=$HOME/Workspace

4. Set **KIVAKIT_WORKSPACE** and **KIVAKIT_HOME** in your system or account properties.

   > You must set KIVAKIT_WORKSPACE and KIVAKIT_HOME to UNIX-style paths such as /c/Users/jonathanl/Workspace

5. Exit your terminal program entirely (not just your shell window)

6. Restart the terminal program and open a new shell window

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### 3. Finishing the Job  <img src="https://www.kivakit.org/images/stars-32.png" srcset="https://www.kivakit.org/images/stars-32-2x.png 2x"/>

Once you have cloned the project into your workspace and set up your environment, you can complete your set up with one final command:

    $KIVAKIT_HOME/setup/setup.sh

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Done!   <img src="https://www.kivakit.org/images/rocket-32.png" srcset="https://www.kivakit.org/images/rocket-32-2x.png 2x"/>

Congratulations! You're set up and ready to build or help to develop KivaKit.

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Next Steps &nbsp; &nbsp;  <img src="https://www.kivakit.org/images/footprints-32.png" srcset="https://www.kivakit.org/images/footprints-32-2x.png 2x"/>

[I want to build KivaKit](building.md)

[I want to work on KivaKit](../developing/index.md)


<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

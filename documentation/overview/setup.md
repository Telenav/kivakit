# KivaKit - Setup   ![](../images/box-40.png)

![](../images/horizontal-line.png)

## Setting Up KivaKit

Whether you plan to use KivaKit or help to develop it, this page will help you get rolling in 3 easy steps.

### Prerequisites

You will need these products, or compatible products, set up to begin:

1. Latest Git
2. Latest [Java 12](https://www.oracle.com/java/technologies/javase/jdk12-archive-downloads.html)
3. Latest [Maven](https://maven.apache.org/download.cgi)

<br/>

![](../images/horizontal-line.png)

### 1. Checking Out the Project  ![](../images/down-arrow-32.png)

The KivaKit project will look roughly like this when we're done with the setup process:

> * Workspace
>   * kivakit
>     * kivakit-core

Notice how the *kivakit* project (**KIVAKIT_HOME**) is checked out in the IDE workspace called  
*Workspace* (**KIVAKIT_WORKSPACE**).

To check out the *kivakit* project:

1. If you're running macOS, and you want to switch your shell from *zsh* to *bash*, type:

       chsh -s /bin/bash

   If you don't like macOS complaining that you're not using *zsh*, add this line to your *.bash_profile*:

       export BASH_SILENCE_DEPRECATION_WARNING=1

2. Open a *bash* shell and go to your IDE workspace (the folder *Workspace* above)
3. Clone the *kivakit* git repository into your workspace
   
       cd Workspace 
       git clone ssh://git@bitbucket.telenav.com:7999/kivakit/kivakit.git

<br/>

![](../images/horizontal-line.png)

### 2. Setting Up Your Environment   ![](../images/box-40.png)

To configure your environment, you will need several environment variables set.

> Without the correct environment variables set, the setup script in Step 3 won't work.

### MacOS and UNIX    ![](../images/bluebook-32.png)

On macOS or UNIX, you can use a sample .profile* from KivaKit to configure your shell environment.

1. Install the KivaKit *profile* sample from the *kivakit* project as shown here (or merge  
   it into your existing .profile):

       cp kivakit/setup/mac/profile $HOME/.profile

2. Make sure that the **M2_HOME** and **JAVA_HOME** environment variables defined in the  
   *.profile* point to your Maven and Java installations

       export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-12.0.2.jdk/Contents/Home 
       export M2_HOME=$HOME/Developer/apache-maven-3.6.3

3. Change the **KIVAKIT_WORKSPACE** variable to point to your IDE workspace (**Workspace**).  
   _Do not set KIVAKIT_HOME._

       export KIVAKIT_WORKSPACE=$HOME/Workspace

   > **KIVAKIT_WORKSPACE** must point to your *IDE workspace* **NOT** the *kivakit* project in the workspace

4. Start a new shell or execute *source ~/.profile*

### Windows &nbsp;&nbsp; ![](../images/window-32.png)

On Windows, a UNIX-like environment with *bash* is required. It is recommended to install [Git for Windows](https://gitforwindows.org/),  
which will install both git, and a bash shell.

1. Install KivaKit *.profile* sample from the *kivakit* project exactly as shown here (or merge  
   it into your existing .profile):

       cp kivakit/setup/windows/profile $HOME/.profile

2. Make sure that the **M2_HOME** and **JAVA_HOME** environment variables defined in the *.profile*  
   point to your Maven and Java installations

       export JAVA_HOME=/c/Program\\ Files/Java/jdk-12.0.2 
       export M2_HOME=$HOME/Tools/apache-maven-3.6.3

3. Change the **KIVAKIT_WORKSPACE** variable to point to your IDE workspace (**$HOME/Workspace**).  
   _Do not set KIVAKIT_HOME._

       export KIVAKIT_WORKSPACE=$HOME/Workspace

4. Set **KIVAKIT_WORKSPACE** and **KIVAKIT_HOME** in your system or account properties.

   > You must set KIVAKIT_WORKSPACE and KIVAKIT_HOME to UNIX-style paths such as /c/Users/jonathanl/Workspace

5. Start a new shell or execute *source ~/.profile*

<br/>

![](../images/horizontal-line.png)

### 3. Finishing the Job  ![](../images/stars-48.png)

Once you have cloned the project into your workspace and set up your environment,  
you can complete your set up with one final command:

    $KIVAKIT_HOME/setup/setup.sh

<br/>

![](../images/horizontal-line.png)

### Done!   ![](../images/rocket-40.png)

Congratulations! You're set up and ready to build or help to develop KivaKit.

<br/>

![](../images/horizontal-line.png)

### Next Steps &nbsp; &nbsp;  ![](../images/footprints-40.png)

[I want to build KivaKit](building.md)

[I want to work on KivaKit](../developing/index.md)


<br/> 

![](../images/horizontal-line.png)

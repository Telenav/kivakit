## Releasing KivaKit &nbsp; <img src="https://www.kivakit.org/images/rocket-32.png" srcset="https://www.kivakit.org/images/rocket-32-2x.png 2x"/>

### Step-by-Step Instructions &nbsp; <img src="https://www.kivakit.org/images/footprints-32.png" srcset="https://www.kivakit.org/images/footprints-32-2x.png 2x"/>

This section documents how to release a new version of KivaKit, step by step.

In the text below *\[kivakit-version\]* refers to a [semantic versioning](https://semver.org) identifier, such
as 2.1.7 or 1.2.2-beta.

KivaKit adheres to the standard [Git Flow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow) branching model.

### 0. Releasing Cactus Build

Before building KivaKit, cactus-build must be released. 

> The version number of cactus-build must always be in sync with KivaKit

1. Execute the command:  

       cactus-release.sh [kivakit-version]

   The *cactus-release.sh* script will execute this sequence of scripts:

   * cactus-release-start.sh *[kivakit-version]*
   * cactus-release-update-version.sh *[kivakit-version]*
   * cactus-build.sh
   * cactus-build-documentation.sh
   * cactus-build.sh *deploy-local*

   If one of these script fails, you can continue the release process by running the remaining scripts by hand.


2. Double check the release branch
3. Execute the commands:

       cactus-release-finish.sh [version]
       cactus-build.sh deploy-ossrh

4. Sign into [OSSRH](https://s01.oss.sonatype.org) and release to Maven Central

### 1. Creating the Release Branch <img src="https://www.kivakit.org/images/branch-32.png" srcset="https://www.kivakit.org/images/branch-32-2x.png 2x"/>

Start a new release branch with the following command:

    kivakit-release-start.sh [kivakit-version]

This script does the following:

1. Creates the release branch *release/[kivakit-version\]* using git flow
2. Updates *$KIVAKIT_HOME/project.properties* file
3. Updates the version of all pom.xml files to *[kivakit-version]*

Search for and replace any stray version numbers from the previous revision.
 
> Quit and restart your terminal program (not just the shell window) to ensure your environment variables are updated.

### 2. Preparing the Release &nbsp; <img src="https://www.kivakit.org/images/box-32.png" srcset="https://www.kivakit.org/images/box-32-2x.png 2x"/>

Once the release branch has been created, several steps need to be performed manually to prepare the branch for publication.

#### 2.1 Preparing the Release Branch

Double check for stray references to the previous version in pom.xml files

#### 2.2 Updating Code Flowers

To update code flowers for the release

1. On the command line, execute:  

        mkdir -p $KIVAKIT_WORKSPACE/kivakit-assets/docs/$KIVAKIT_VERSION
        
1. Copy the *codeflowers* folder from a previous build into this folder
1. Inside the *codeflowers* folder, execute:

        ./kivakit-build-codeflowers.sh
        
1. Open *site/index.html* in an editor and insert the &lt;option&gt; HTML code that was output by the build process.

#### 2.3 Checking the Build

To ensure that the build will be accepted on Maven Central, run:

    kivakit-build.sh deploy-local
    
This will build all kivakit artifacts from scratch (answer 'y' to the prompt to remove all artifacts), including Javadoc and Lexakai documentation.

#### 2.4 Updating the Change Log

Examine the git history log and update the change-log.md file with any important changes in the release.

#### 2.5 Committing Final Changes

Commit any remaining changes to the release branch.

### 3. Publishing the Release &nbsp;  <img src="https://www.kivakit.org/images/stars-32.png" srcset="https://www.kivakit.org/images/stars-32-2x.png 2x"/>

The release is finished and merged into master with another script that uses git flow:

    kivakit-release-finish.sh [kivakit-version]

### 4. Pushing the Release to Maven Central

To push the release to OSSRH, run:

    kivakit-build.sh deploy-ossrh

The sign into OSSRH and push the build to Maven Central

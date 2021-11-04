## Releasing KivaKit &nbsp; <img src="https://www.kivakit.org/images/rocket-32.png" srcset="https://www.kivakit.org/images/rocket-32-2x.png 2x"/>

### Step-by-Step Instructions &nbsp; <img src="https://www.kivakit.org/images/footprints-32.png" srcset="https://www.kivakit.org/images/footprints-32-2x.png 2x"/>

This section documents how to release a new version of KivaKit, step by step.

In the text below *\[kivakit-version\]* refers to a [semantic versioning](https://semver.org) identifier, such
as 2.1.7 or 1.1.1-SNAPSHOT-beta.

KivaKit adheres to the standard [Git Flow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow) branching model.

### 0. Releasing Cactus Build

Before building KivaKit, cactus-build must be released. The version number should always be in sync with KivaKit.

1. Run cactus-release-start.sh [kivakit-version]
2. Run cactus-release-update-version.sh [kivakit-version]
3. Run cactus-build.sh
4. Run cactus-build-documentation.sh
5. Run cactus-build.sh deploy-local
6. Double check the release branch
7. Run cactus-release-finish.sh [kivakit-version]
8. Run cactus-build.sh deploy-ossrh
9. Sign into OSSRH and release to Maven Central

### 1. Creating the Release Branch <img src="https://www.kivakit.org/images/branch-32.png" srcset="https://www.kivakit.org/images/branch-32-2x.png 2x"/>

Start a new release branch with the following commands:

    kivakit-release-start.sh [kivakit-version]
    kivakit-release-update-version.sh [kivakit-version]

This script does the following:

1. Creates the release branch *release/[kivakit-version\]* using git flow
2. Updates *$KIVAKIT_HOME/project.properties* file
3. Updates the version of all pom.xml files to *[kivakit-version]*

Search for and replace any stray version numbers from the previous revision and restart your terminal program to ensure environment variables are updated.

### 2. Preparing the Release &nbsp; <img src="https://www.kivakit.org/images/box-32.png" srcset="https://www.kivakit.org/images/box-32-2x.png 2x"/>

Once the release branch has been created, several steps need to be performed manually to prepare the branch for publication.

#### 2.1 Prepare the Release Branch

1. Update references to cactus-build version in pom.xml files
2. Double check for stray references to the previous version in pom.xml files

#### 2.2 Building the Release

In order to ensure that the build will work on the build server, it is a good idea to completely clean out your maven repository and cache folders by building the project completely from scratch:

    kivakit-build.sh all sparkling

This will remove (after prompting) the following before building:

1. Maven repository *~/.m2*
2. KivaKit cache folder *~/.kivakit/\[kivakit-version\]*
3. Temporary files, logs, etc. in the source tree

#### 2.3 Building the Documentation

The following command will build Javadoc, UML diagrams and update project README.md indexes.

    kivakit-build-documentation.sh

#### 2.4 Updating Code Flowers

To publish code flowers for the build:

1. On the command line:  

        mkdir -p $KIVAKIT_WORKSPACE/kivakit-assets/docs/$KIVAKIT_VERSION/codeflowers
        cd $KIVAKIT_WORKSPACE/kivakit-assets/docs/$KIVAKIT_VERSION/codeflowers
        
1. Copy the *codeflowers* folder from a previous build into this folder
1. Inside the *codeflowers* folder:

        ./kivakit-build-codeflowers.sh
        
1. Open *site/index.html* in an editor and insert the &lt;option&gt; HTML code that was output by the build process.

#### 2.5 Check Deployment Build

To ensure that the build will be accepted on Maven Central, run:

    kivakit-build.sh deploy-local

#### 2.6 Commit Changes

Commit any changes to the release branch.

### 3. Finishing and Publishing the Release Branch &nbsp;  <img src="https://www.kivakit.org/images/stars-32.png" srcset="https://www.kivakit.org/images/stars-32-2x.png 2x"/>

The release is finished and merged into master with another script that uses git flow:

    kivakit-release-finish.sh [kivakit-version]

### 4. Pushing the Release to Maven Central

To push the release to OSSRH, run:

    kivakit-build.sh deploy-ossrh

The sign into OSSRH and push the build to Maven Central

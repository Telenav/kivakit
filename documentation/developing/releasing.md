# KivaKit - Releasing KivaKit &nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/rocket-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/rocket-32-2x.png 2x"/>

### Step-by-Step Instructions &nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/footprints-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/footprints-32-2x.png 2x"/>

This section documents how to release a new version of KivaKit, step by step.

In the text below *\[kivakit-version\]* refers to a [semantic versioning](https://semver.org) identifier, such as 2.1.7 or 1.4.0-beta.

KivaKit adheres to the standard [Git Flow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow) branching model.

### 0. Releasing Cactus Build (Optional)

Cactus build needs to be released only if it has been changed.

> The version number of *cactus-build* should always be in sync with *kivakit*

1. To prepare a release branch, and build the release:

       cactus-release.sh [kivakit-version]

2. Check that the release branch was created and that all version numbers were updated correctly in pom.xml and project.properties files.


3. To finalize the release:

       cactus-release-finish.sh [version]

4. To build the release for Maven Central

       cactus-build.sh deploy-ossrh

5. Sign in to [OSSRH](https://s01.oss.sonatype.org) and release to Maven Central

### 1. Preparing the KivaKit Release Branch <img src="https://telenav.github.io/telenav-assets/images/icons/branch-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/branch-32-2x.png 2x"/>

#### 1.1 Create a release branch

To create a release branch, and update version numbers:

    kivakit-release.sh [kivakit-version]

#### 1.2 Check the release

Check that version numbers in *pom.xml* and *project.properties* files were updated correctly.

#### 1.3 Update the change log

Examine the git history log of all four *kivakit** repositories, and update the *change-log.md* file with any important information about the release.

#### 1.4 Update the project code flowers

To update code flowers for the release

1. On the command line, execute:

        mkdir -p $KIVAKIT_WORKSPACE/kivakit-assets/docs/$KIVAKIT_VERSION

2. Copy the *codeflowers* folder from a previous build into this folder


3. Inside the *codeflowers* folder, execute:

        ./kivakit-build-codeflowers.sh

4. If there have been any projects added or removed since the last release, open *site/index.html* in an editor and insert the &lt;option&gt; HTML code that was output by the kivakit-build-codeflowers.sh.

#### 1.4 Build the release branch

To build the release branch, run the release script again:

    kivakit-release.sh [kivakit-version]

This will build all kivakit artifacts from scratch (answer 'y' to the prompt to remove all artifacts), including Javadoc and Lexakai documentation.

#### 1.5 Committing Final Changes

Commit any final changes to the release branch.

### 2. Publishing the Release &nbsp;  <img src="https://telenav.github.io/telenav-assets/images/icons/stars-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/stars-32-2x.png 2x"/>

#### 2.0 Merge the release branch into master

The release is finished and merged into master with:

    kivakit-release-finish.sh [kivakit-version]

#### 2.1 Update Docker build environment image

Switch to the develop branch, then a docker build environment image can be created with:

    kivakit-docker-build-create-image.sh

When the image has been built, it can be pushed to DockerHub with:

    kivakit-docker-build-push-image.sh

#### 2.2 Push the Release to OSSRH

To push the release to OSSRH, run:

    kivakit-build.sh deploy-ossrh

#### 2.3 Publish from OSSRH to Maven Central

The sign into [OSSRH](http://s01.oss.sonatype.org) and push the build to Maven Central.

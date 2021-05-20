################ VARIABLES ################################################################################################

export NORMAL='\033[0m'
export ATTENTION='\033[1;32m'

if [[ "$OSTYPE" == "darwin"* ]]; then
    export HISTCONTROL=ignoreboth:erasedups
fi

################ PROJECT ################################################################################################

property_value() {

    file=$1
    key=$2

    cat $file | grep "$key" | cut -d'=' -f2 | xargs echo
}

project_version() {

    project_home=$1
    project_properties=$project_home/project.properties

    echo $(property_value $project_properties project-version)
}

project_name() {
    project_home=$1
    echo $(basename -- "$project_home")
}

project_build() {

    project_home=$1

    build_properties=$project_home/build.properties

    if [ -e "$build_properties" ]; then

        build_name=$(property_value $build_properties build-name)
        build_number=$(property_value $build_properties build-number)
        build_date=$(property_value $build_properties build-date)

        echo "build #$build_number on $build_date '$build_name'"

    fi
}

showVersion() {

    project_home=$1
    project_name=$(project_name $project_home)
    project_version=$(project_version $project_home)

    echo -e "${ATTENTION}$project_name $project_version $(project_build $project_home)${NORMAL}"
}

################ CLEAN ################################################################################################

clean_cache() {

    cache=$2

    if [ -d "$cache" ]; then
        read -p "┋ Remove ALL cached files in $cache (y/n)? " -n 1 -r
        echo "┋ "
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            rm -rf $cache
        fi
    fi
}

clean_maven_repository() {

    project_home=$1
    name=$(basename -- "$project_home")

    if yes_no "Remove all $name artifacts from ~\.m2"; then

        rm -rf "~/.m2/repository/com/telenav/$name"

    fi
}

remove_maven_repository() {

    if yes_no "Remove ALL artifacts in ~\.m2"; then

        rm -rf ~/.m2

    fi
}

clean_temporary_files() {

    project_home=$1

    if yes_no "Remove temporary files (.DS_Store, .metadata, .classpath, .project, *.hprof, *~) from $project_home tree"; then

        find $project_home \( -name \.DS_Store -o -name \.metadata -o -name \.classpath -o -name \.project -o -name \*\.hprof -o -name \*~ \) | xargs rm

    fi
}

################ COMMAND LINE ################################################################################################

script() {
    echo $(basename -- "$0")
}

usage() {
    argument_help=$1
    echo "Usage: $(script) $argument_help"
    exit 1
}

require_variable() {

    variable=$1
    argument_help=$2

    if [[ -z "${!variable}" ]]; then

        usage $argument_help

    fi
}

require_folder() {

    variable=$1
    argument_help=$2

    if [[ ! -e "${!variable}" ]]; then

        echo "Folder '${!variable}' does not exist"
        exit 1

    fi
}

################ GIT ################################################################################################

git_flow_release_start() {

    project_home=$1
    project_name=$(basename $project_home)
    version=$2

    # Check out the develop branch
    cd $project_home
    git checkout develop

    # then start a new release branch
    git flow release start $version

    # switch to the release branch
    git checkout release/$version

    # and update its version
    bash $project_name-update-version.sh $version

    echo " "
    echo "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Release Branch Created  ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓"
    echo "┋"
    echo "┋  VERSION: $version"
    echo "┋"
    echo "┋  1. A new release branch 'release/$version' has been created using git flow."
    echo "┋  2. POM files and other version-related information in this branch has been updated to $version."
    echo "┋  3. When the release branch is FULLY READY, run the release finish script to merge the branch into master."
    echo "┋"
    echo "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"
    echo " "
}

git_flow_release_finish() {

    project_home=$1
    version=$2

    cd $project_home

    git checkout master
    git merge release/$version
    git flow release finish $version
    git push origin --tags

    echo " "
    echo "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Release Merged and Published  ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓"
    echo "┋"
    echo "┋  VERSION: $version"
    echo "┋"
    echo "┋  1. The branch 'release/$version' has been merged into master using git flow."
    echo "┋  2. Artifacts from the release will be published shortly by Jenkins"
    echo "┋"
    echo "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"
    echo " "
}

git_flow_feature_start() {

    project_home=$1
    feature_name=$2

    if yes_no "Start 'Feature-$feature_name' branch of $project_home"; then

        cd $project_home
        git-flow feature start Feature-$feature_name

    fi
}

git_flow_feature_finish() {

    project_home=$1
    feature_name=$2

    if yes_no "Finish 'Feature-$feature_name' branch of $project_home"; then
        cd $project_home
        git-flow feature finish Feature-$1
    fi
}

################ VERSIONING ################################################################################################

update_version() {

    project_home=$1
    new_version=$2

    old_version=$(project_version $project_home)

    echo " "
    echo "Updating $(project_name $project_home) version from $old_version to $new_version"

    # Update POM versions and .md files
    update-version.pl $project_home $old_version $new_version

    echo "Updated"
    echo " "
}

################ UTILITY ################################################################################################

append_path() {
    export PATH="$PATH:$1"
}

prepend_path() {
    export PATH="$1:$PATH"
}

source_project_profile() {

    project_name=$1

    common_profile="$KIVAKIT_WORKSPACE/${project_name}/tools/library/${project_name}-common-profile.sh"
    project_profile="$HOME/.${project_name}-profile"

    if test -e "$common_profile"; then
        source "$common_profile"
    fi

    if test -e "$project_profile"; then
        source "$project_profile"
    fi
}

system_variable() {

    variable=$1
    value=$2
    temporary="$TMPDIR/export.txt"

    echo "export $variable=\"$value\"" >$temporary
    source "$temporary"

    if is_mac; then
        launchctl setenv $variable "$value"
    fi
}

is_mac() {
    if [[ "$OSTYPE" == "darwin"* ]]; then
        true
    else
        false
    fi
}

lexakai() {

    lexakai_downloads="$HOME/.lexakai/downloads"
    lexakai_jar="${lexakai_downloads}/lexakai-${LEXAKAI_DOWNLOAD_VERSION}.jar"

    mkdir -p ${lexakai_downloads}

    if [ ! -e "$lexakai_jar" ]; then

        wget $LEXAKAI_URL --output-document=$lexakai_jar

    fi

    # -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=1044
    echo "java -jar $lexakai_jar -overwrite-resources=true -update-readme=true $@"
    java -jar $lexakai_jar -overwrite-resources=true -update-readme=true $@
}

yes_no() {

    if [ -z "${NO_PROMPT}" ]; then

        prompt=$1

        read -p "$prompt (y/n)? " -n 1 -r
        echo " "

        if [[ $REPLY =~ ^[Yy]$ ]]; then
            true
        else
            false
        fi

    else

        true

    fi
}

#####################################################################################################################

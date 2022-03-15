################ VARIABLES ################################################################################################

export NORMAL='\033[0m'
export ATTENTION='\033[1;32m'

if [[ "$OSTYPE" == "darwin"* ]]; then

    export HISTCONTROL=ignoreboth:erasedups

fi

################ PROJECT ################################################################################################

property_value()
{
    file=$1
    key=$2

    # shellcheck disable=SC2002
    cat "$file" | grep "$key" | cut -d'=' -f2 | xargs echo
}

project_version()
{
    project_home=$1
    project_properties=$project_home/project.properties

    # shellcheck disable=SC2046
    # shellcheck disable=SC2005
    echo $(property_value "$project_properties" project-version)
}

project_name()
{
    project_home=$1

    # shellcheck disable=SC2046
    # shellcheck disable=SC2005
    echo $(basename -- "$project_home")
}

project_build()
{
    project_home=$1

    build_properties=$project_home/build.properties

    if [ -e "$build_properties" ]; then

        build_name=$(property_value "$build_properties" build-name)
        build_number=$(property_value "$build_properties" build-number)
        build_date=$(property_value "$build_properties" build-date)

        branch_name=$(git_branch_name "$project_home")

        echo "$branch_name build #$build_number [$build_name] on $build_date"

    fi
}

showVersion()
{
    project_home=$1
    project_name=$(project_name "$project_home")
    project_version=$(project_version "$project_home")

    echo -e "${ATTENTION}$project_name $project_version $(project_build "$project_home")${NORMAL}"
}

################ CLEAN ################################################################################################

clean_cache() {

    if [[ "$ALLOW_CLEANING" == "true" ]]; then

        cache=$1

        if [ -d "$cache" ]; then

            if yes_no "┋ Remove ALL cached files in $cache"; then

                rm -rf "$cache"

            fi
        fi

    fi
}

clean_maven_repository() {

    project_home=$1
    name=$(basename -- "$project_home")

    if yes_no "┋ Remove all $name artifacts from $HOME/.m2/repository"; then

        rm -rf "$HOME/.m2/repository/com/telenav/$name"

    fi
}

remove_maven_repository() {

    if [ -d "$HOME/.m2/repository" ]; then

        if yes_no "┋ Remove ALL artifacts in $HOME/.m2/repository"; then

            rm -rf "$HOME/.m2/repository"

        fi

    fi
}

clean_temporary_files() {

    project_home=$1

    if yes_no "┋ Remove temporary files (.DS_Store, .metadata, .classpath, .project, *.hprof, *~) from $project_home tree"; then

        # shellcheck disable=SC2038
        find "$project_home" \( -name \.DS_Store -o -name \.metadata -o -name \.classpath -o -name \.project -o -name \*\.hprof -o -name \*~ \) | xargs rm

    fi
}

################ COMMAND LINE ################################################################################################

script() {

    # shellcheck disable=SC2046
    # shellcheck disable=SC2005
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

        usage "$argument_help"

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


git_flow_check_changes() {

    project_home=$1

    cd $project_home

    if [[  `git status --porcelain` ]]; then
        echo " "
        echo "Project contains changes that must be committed first: $project_home"
        echo " "
        exit 1
    fi
}

git_flow_init() {

    project_home=$1

    cd $project_home

    git_flow_check_changes $project_home

    git flow init -d /dev/null 2>&1

    if [ "$(git flow config >/dev/null 2>&1)" ]; then
        echo " "
        echo "Please install git flow and try again."
        echo "See https://kivakit.org for details."
        echo " "
        exit 1
    fi
}

git_flow_release_start() {

    project_home=$1
    version=$2

    echo " "
    echo "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Creating Release Branch  ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓"
    echo "┋"
    echo "┋  Creating $project_name git flow branch release/$version"
    echo "┋"
    echo "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"
    echo " "

    # Check out the develop branch
    cd "$project_home" || exit
    git checkout develop

    # then start a new release branch
    git flow release start "$version"

    # switch to the release branch
    git checkout release/"$version"

    # and update its version
    bash cactus-release-update-version.sh "$version"
}

git_branch_name()
{
    project_home=$1
    cd "$project_home" || exit
    branch_name=$(git rev-parse --abbrev-ref HEAD)
    echo "$branch_name"
}

git_flow_release_finish()
{
    project_home=$1
    version=$2

    cd "$project_home" || exit

    git checkout master
    git tag -a "$version" -m "$version"
    git checkout release/"$version"
    git flow merge finish "$version"

    echo " "
    echo "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Release Merged to Master  ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓"
    echo "┋"
    echo "┋  The branch 'release/$version' has been merged into master using git flow."
    echo "┋"
    echo "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"
    echo " "
}

git_flow_feature_start() {

    project_home=$1
    feature_name=$2

    if yes_no "Start '$feature_name' branch of $project_home"; then

        cd "$project_home" || exit
        git-flow feature start "$feature_name"

    fi
}

git_flow_feature_finish() {

    project_home=$1
    feature_name=$2

    if yes_no "Finish '$feature_name' branch of $project_home"; then
        # shellcheck disable=SC2164
        cd "$project_home"
        git-flow feature finish "$feature_name"
    fi
}

git_flow_hotfix_start() {

    project_home=$1
    feature_name=$2

    if yes_no "Start '$feature_name' branch of $project_home"; then

        cd "$project_home" || exit
        git-flow hotfix start "$feature_name"

    fi
}

git_flow_hotfix_finish() {

    project_home=$1
    feature_name=$2

    if yes_no "Finish '$feature_name' branch of $project_home"; then
        cd "$project_home" || exit
        git-flow hotfix finish "$feature_name"
    fi
}

################ VERSIONING ################################################################################################

update_version() {

    project_home=$1
    new_version=$2

    old_version=$(project_version "$project_home")

    echo " "
    echo "Updating $(project_name "$project_home") version from $old_version to $new_version"

    # Update POM versions and .md files
    update-version.pl "$project_home" "$old_version" "$new_version"

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
        # shellcheck disable=SC1090
        source "$common_profile"
    fi

    if test -e "$project_profile"; then
        # shellcheck disable=SC1090
        source "$project_profile"
    fi
}

system_variable() {

    variable=$1
    value=$2
    temporary="${TMPDIR%/}/export.txt"

    echo "export $variable=\"$value\"" >"$temporary"
    # shellcheck disable=SC1090
    source "$temporary"

    if is_mac; then
        launchctl setenv "$variable" "$value"
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

    lexakai_download_version="1.0.5"
    lexakai_download_name="lexakai-1.0.5.jar"

    lexakai_downloads="$HOME/.lexakai/downloads"

    if [[ "$lexakai_download_version" == *"SNAPSHOT"* ]]; then

        lexakai_snapshot_repository="https://s01.oss.sonatype.org/content/repositories/snapshots/com/telenav/lexakai/lexakai"
        lexakai_url="$lexakai_snapshot_repository/${lexakai_download_version}/${lexakai_download_name}"
        lexakai_jar="${lexakai_downloads}/${lexakai_download_name}"

    else

        lexakai_url="https://repo1.maven.org/maven2/com/telenav/lexakai/lexakai/${lexakai_download_version}/lexakai-${lexakai_download_version}.jar"
        lexakai_jar="${lexakai_downloads}/lexakai-${lexakai_download_version}.jar"

    fi

    mkdir -p "${lexakai_downloads}"

    if [ ! -e "$lexakai_jar" ]; then

        echo "$lexakai_jar doesn't exist"

        wget $lexakai_url --output-document="$lexakai_jar"

    fi

    # -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=1044
    echo "java -jar $lexakai_jar -overwrite-resources=true -update-readme=true $*"

    # shellcheck disable=SC2068
    java -jar "$lexakai_jar" -overwrite-resources=true -update-readme=true $@
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

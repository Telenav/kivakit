
source "$KIVAKIT_WORKSPACE"/kivakit/tools/library/kivakit-library-functions.sh

export BASH_SILENCE_DEPRECATION_WARNING=1
export MAVEN_OPTS="--add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/java.text=ALL-UNNAMED --add-opens=java.desktop/java.awt.font=ALL-UNNAMED"

system_variable KIVAKIT_HOME "$KIVAKIT_WORKSPACE/kivakit"
system_variable KIVAKIT_VERSION "$(project_version "$KIVAKIT_HOME")"
system_variable KIVAKIT_BUILD "$(project_build "$KIVAKIT_HOME")"
system_variable KIVAKIT_TOOLS "$KIVAKIT_HOME/tools"
system_variable KIVAKIT_JAVA_OPTIONS "-Xmx12g"

prepend_path "$M2_HOME/bin"
prepend_path "$JAVA_HOME/bin"

append_path "$KIVAKIT_TOOLS/building"
append_path "$KIVAKIT_TOOLS/developing"
append_path "$KIVAKIT_TOOLS/library"
append_path "$KIVAKIT_TOOLS/releasing"

system_variable KIVAKIT_ASSETS_HOME "$KIVAKIT_WORKSPACE/kivakit-assets"
system_variable KIVAKIT_EXTENSIONS_HOME "$KIVAKIT_WORKSPACE/kivakit-extensions"
system_variable KIVAKIT_STUFF_HOME "$KIVAKIT_WORKSPACE/kivakit-stuff"
system_variable KIVAKIT_EXAMPLES_HOME "$KIVAKIT_WORKSPACE/kivakit-examples"
system_variable KIVAKIT_CACHE_HOME "$HOME/.kivakit/$KIVAKIT_VERSION"
system_variable KIVAKIT_AGENT_JAR "$KIVAKIT_TOOLS/agent/kivakit-agent.jar"

source_project_profile "cactus-build"

echo " "
echo "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ KivaKit Environment ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓"
echo "┋"
echo "┋        KIVAKIT_WORKSPACE: $KIVAKIT_WORKSPACE"
echo "┋             KIVAKIT_HOME: $KIVAKIT_HOME"
echo "┋          KIVAKIT_VERSION: $KIVAKIT_VERSION"
echo "┋            KIVAKIT_BUILD: $KIVAKIT_BUILD"
echo "┋"
echo "┋       KIVAKIT_CACHE_HOME: $KIVAKIT_CACHE_HOME"
echo "┋      KIVAKIT_ASSETS_HOME: $KIVAKIT_ASSETS_HOME"
echo "┋  KIVAKIT_EXTENSIONS_HOME: $KIVAKIT_EXTENSIONS_HOME"
echo "┋       KIVAKIT_STUFF_HOME: $KIVAKIT_STUFF_HOME"
echo "┋    KIVAKIT_EXAMPLES_HOME: $KIVAKIT_EXAMPLES_HOME"
echo "┋"
echo "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"
echo " "

# shellcheck disable=SC2034
SHOW_SYSTEM_ENVIRONMENT=false

source_project_profile "mesakit"
source_project_profile "lexakai"

date +setup-time=%Y.%m.%d-%I.%M%p > "$KIVAKIT_WORKSPACE"/kivakit/setup.properties

cd "$KIVAKIT_WORKSPACE" || exit

echo " "
echo "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ System Environment ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓"
echo "┋"
echo "┋                     Java: $JAVA_HOME"
echo "┋                    Maven: $M2_HOME"
perl -e 'print "┋                     Path: " . join("\n┋                           ", split(":", $ENV{"PATH"})) . "\n"'
echo "┋"
echo "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"
echo " "


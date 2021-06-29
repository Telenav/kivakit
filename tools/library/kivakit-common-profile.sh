
source $KIVAKIT_WORKSPACE/kivakit/tools/library/library-functions.sh

export BASH_SILENCE_DEPRECATION_WARNING=1
export MAVEN_OPTS="--add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/java.text=ALL-UNNAMED --add-opens=java.desktop/java.awt.font=ALL-UNNAMED"

system_variable KIVAKIT_HOME "$KIVAKIT_WORKSPACE/kivakit"
system_variable KIVAKIT_VERSION "$(project_version $KIVAKIT_HOME)"
system_variable KIVAKIT_BUILD "$(project_build $KIVAKIT_HOME)"
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
system_variable KIVAKIT_EXAMPLES_HOME "$KIVAKIT_WORKSPACE/kivakit-examples"
system_variable KIVAKIT_CACHE_HOME "$HOME/.kivakit/$KIVAKIT_VERSION"
system_variable KIVAKIT_AGENT_JAR "$KIVAKIT_TOOLS/agent/kivakit-agent.jar"

source $KIVAKIT_TOOLS/library/kivakit-projects.sh

source_project_profile "cactus-build"

echo " "
echo "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ KivaKit Environment ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓"
echo "┋"
echo -e "┋        KIVAKIT_WORKSPACE: ${ATTENTION}$KIVAKIT_WORKSPACE${NORMAL}"
echo -e "┋             KIVAKIT_HOME: ${ATTENTION}$KIVAKIT_HOME${NORMAL}"
echo -e "┋          KIVAKIT_VERSION: ${ATTENTION}$KIVAKIT_VERSION${NORMAL}"
echo -e "┋            KIVAKIT_BUILD: ${ATTENTION}$KIVAKIT_BUILD${NORMAL}"
echo "┋"
echo "┋       KIVAKIT_CACHE_HOME: $KIVAKIT_CACHE_HOME"
echo "┋      KIVAKIT_ASSETS_HOME: $KIVAKIT_ASSETS_HOME"
echo "┋  KIVAKIT_EXTENSIONS_HOME: $KIVAKIT_EXTENSIONS_HOME"
echo "┋    KIVAKIT_EXAMPLES_HOME: $KIVAKIT_EXAMPLES_HOME"
echo "┋"
echo "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"
echo " "

SHOW_SYSTEM_ENVIRONMENT=false

source_project_profile "mesakit"
source_project_profile "lexakai"

date +setup-time=%Y.%m.%d-%I.%M%p > $KIVAKIT_WORKSPACE/kivakit/setup.properties

cd $KIVAKIT_WORKSPACE

echo " "
echo "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ System Environment ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓"
echo "┋"
echo "┋                     Java: $JAVA_HOME"
echo "┋                    Maven: $M2_HOME"
perl -e 'print "┋                     Path: " . join("\n┋                           ", split(":", $ENV{"PATH"})) . "\n"'
echo "┋"
echo "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"
echo " "


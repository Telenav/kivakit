source $KIVAKIT_WORKSPACE/kivakit/tools/library/library-functions.sh

system_variable LEXAKAI_VERSION 0.9.4
system_variable KIVAKIT_BUILD_METADATA_VERSION 0.9.5-alpha
system_variable KIVAKIT_JAVA_OPTIONS "-Xmx12g"

system_variable KIVAKIT_HOME "$KIVAKIT_WORKSPACE/kivakit"
system_variable KIVAKIT_VERSION "$(project_version $KIVAKIT_HOME)"
system_variable KIVAKIT_BUILD "$(project_build $KIVAKIT_HOME)"
system_variable KIVAKIT_TOOLS "$KIVAKIT_HOME/tools"

prepend_path "$M2_HOME/bin"
prepend_path "$JAVA_HOME/bin"

append_path "$KIVAKIT_TOOLS/building"
append_path "$KIVAKIT_TOOLS/developing"
append_path "$KIVAKIT_TOOLS/library"
append_path "$KIVAKIT_TOOLS/releasing"

system_variable KIVAKIT_BUILD_HOME "$KIVAKIT_WORKSPACE/kivakit-build"
system_variable KIVAKIT_DATA_HOME "$KIVAKIT_WORKSPACE/kivakit-data"
system_variable KIVAKIT_EXTENSIONS_HOME "$KIVAKIT_WORKSPACE/kivakit-extensions"
system_variable KIVAKIT_CACHE_HOME "$HOME/.kivakit/$KIVAKIT_VERSION"
system_variable KIVAKIT_AGENT_JAR "$KIVAKIT_TOOLS/agent/kivakit-agent.jar"

source $KIVAKIT_TOOLS/library/kivakit-projects.sh

echo " "
echo "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ KivaKit Environment ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓"
echo "┋"
echo -e "┋        KIVAKIT_WORKSPACE: ${ATTENTION}$KIVAKIT_WORKSPACE${NORMAL}"
echo -e "┋             KIVAKIT_HOME: ${ATTENTION}$KIVAKIT_HOME${NORMAL}"
echo -e "┋          KIVAKIT_VERSION: ${ATTENTION}$KIVAKIT_VERSION${NORMAL}"
echo -e "┋            KIVAKIT_BUILD: ${ATTENTION}$KIVAKIT_BUILD${NORMAL}"
echo "┋"
echo "┋       KIVAKIT_CACHE_HOME: $KIVAKIT_CACHE_HOME"
echo "┋       KIVAKIT_BUILD_HOME: $KIVAKIT_BUILD_HOME"
echo "┋        KIVAKIT_DATA_HOME: $KIVAKIT_DATA_HOME"
echo "┋  KIVAKIT_EXTENSIONS_HOME: $KIVAKIT_EXTENSIONS_HOME"
echo "┋"
echo "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"
echo " "

cd $KIVAKIT_HOME

if [ -f "$HOME/.mesakit-profile" ]; then

    source ~/.mesakit-profile

fi

if [ -f "$HOME/.lexakai-profile" ]; then

    source ~/.lexakai-profile

fi

echo " "
echo "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ System Environment ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓"
echo "┋"
echo "┋                     Java: $JAVA_HOME"
echo "┋                    Maven: $M2_HOME"
perl -e 'print "┋                     Path: " . join("\n┋                           ", split(":", $ENV{"PATH"})) . "\n"'
echo "┋"
echo "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"
echo " "

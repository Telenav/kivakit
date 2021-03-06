////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core;

import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.core.lexakai.DiagramProject;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.nio.file.Path;

/**
 * This class defines a KivaKit {@link Project}. It cannot be constructed with the new operator since it has a private
 * constructor. To access the singleton instance of this class, call {@link Project#resolveProject(Class)}, or use
 * {@link com.telenav.kivakit.core.project.ProjectTrait#project(Class)}.
 *
 * <p>
 * Information about KivaKit, including the home folder, the cache folder and the framework version. Since {@link
 * KivaKit} is a {@link Project} it inherits that functionality as well.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see Project
 * @see Path
 */
@UmlClassDiagram(diagram = DiagramProject.class)
@LexakaiJavadoc(complete = true)
public class KivaKit extends Project
{
    /**
     * @return The cache folder for KivaKit
     */
    public Path cacheFolderPath()
    {
        var version = projectVersion();
        if (version != null)
        {
            return Path.of(System.getProperty("user.home"), ".kivakit", version.toString());
        }
        Ensure.fail("Unable to get version for cache folder");
        return null;
    }

    /**
     * The easiest way to set KIVAKIT_HOME for Eclipse and other applications is to put this line in your .profile:
     *
     * <pre>
     *  launchctl setenv KIVAKIT_HOME $KIVAKIT_HOME
     * </pre>
     *
     * @return Path to KivaKit home if it's available in the environment or as a system property.
     */
    public Path homeFolderPath()
    {
        var home = systemProperty("KIVAKIT_HOME");
        if (home == null)
        {
            return null;
        }
        return Path.of(home);
    }
}

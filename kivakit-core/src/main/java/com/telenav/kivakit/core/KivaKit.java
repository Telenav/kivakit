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

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramProject;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.project.Project;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.nio.file.Path;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.path.StringPath.parseStringPath;

/**
 * This class defines a KivaKit {@link Project}. It cannot be constructed with the new operator since it has a private
 * constructor. To access the singleton instance of this class, call {@link Project#resolveProject(Class)}, or use
 * {@link com.telenav.kivakit.core.project.ProjectTrait#project(Class)}.
 *
 * <p>
 * Information about KivaKit, including the home folder, the cache folder and the framework version. Since
 * {@link KivaKit} is a {@link Project} it inherits that functionality as well.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see Project
 * @see Path
 */
@UmlClassDiagram(diagram = DiagramProject.class)
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class KivaKit extends Project
{
    /**
     * @return The cache folder for KivaKit
     */
    public StringPath cacheFolderPath()
    {
        var version = projectVersion();
        if (version != null)
        {
            return parseStringPath(this, System.getProperty("user.home"), ".kivakit", version.toString());
        }
        fail("Unable to get version for cache folder");
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
    public StringPath homeFolderPath()
    {
        var home = systemPropertyOrEnvironmentVariable("KIVAKIT_HOME");
        if (home == null)
        {
            return null;
        }
        return parseStringPath(this, home, "/");
    }
}

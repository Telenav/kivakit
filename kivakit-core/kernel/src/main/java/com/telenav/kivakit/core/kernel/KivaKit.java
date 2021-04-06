////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel;

import com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.language.vm.JavaVirtualMachine;
import com.telenav.kivakit.core.kernel.project.Project;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramProject;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.nio.file.Path;

/**
 * Information about KivaKit, including the home folder, the cache folder and the framework version.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("UseOfSystemOutOrSystemErr")
@UmlClassDiagram(diagram = DiagramProject.class)
public class KivaKit extends Project
{
    private static final Lazy<KivaKit> singleton = Lazy.of(KivaKit::new);

    public static KivaKit get()
    {
        return singleton.get();
    }

    protected KivaKit()
    {
    }

    /**
     * @return The cache folder for KivaKit
     */
    public Path cacheFolderPath()
    {
        final var version = version();
        if (version != null)
        {
            return Path.of(System.getProperty("user.home"), ".kivakit", version.toString());
        }
        return Ensure.fail("Unable to get version for cache folder");
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
        final var home = JavaVirtualMachine.property("KIVAKIT_HOME");
        if (home == null)
        {
            return null;
        }
        return Path.of(home);
    }
}

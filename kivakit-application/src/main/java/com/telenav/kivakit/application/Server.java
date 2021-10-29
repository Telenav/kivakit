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

package com.telenav.kivakit.application;

import com.telenav.kivakit.application.project.lexakai.diagrams.DiagramApplication;
import com.telenav.kivakit.kernel.project.Project;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Subclass of {@link Application} for use by servers.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramApplication.class)
@LexakaiJavadoc(complete = true)
public abstract class Server extends Application
{
    protected Server(Project project)
    {
        super(project);
    }
}

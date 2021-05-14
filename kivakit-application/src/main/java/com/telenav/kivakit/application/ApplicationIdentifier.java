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

package com.telenav.kivakit.application;

import com.telenav.kivakit.application.project.lexakai.diagrams.DiagramApplication;
import com.telenav.kivakit.kernel.language.values.identifier.StringIdentifier;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A unique string identifier for a KivaKit {@link Application}.
 *
 * @author jonathanl (shibo)
 */
@Schema(description = "A unique identifier for a KivaKit application, provided by the Application base class",
        example = "ServiceRegistryServer")
@UmlClassDiagram(diagram = DiagramApplication.class)
@UmlExcludeSuperTypes
@LexakaiJavadoc(complete = true)
public class ApplicationIdentifier extends StringIdentifier
{
    public ApplicationIdentifier(final String identifier)
    {
        super(identifier);
    }

    protected ApplicationIdentifier()
    {
    }
}

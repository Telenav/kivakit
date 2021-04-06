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

package com.telenav.kivakit.data.formats.library.xml;

import com.telenav.kivakit.core.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.data.formats.library.xml.project.lexakai.diagrams.DiagramXml;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramXml.class)
public class OpenTag implements Matcher<XmlNode>
{
    private final String name;

    public OpenTag(final String name)
    {
        this.name = name;
    }

    @Override
    public boolean matches(final XmlNode node)
    {
        return node.isOpenTag() && node.name().equals(name);
    }
}

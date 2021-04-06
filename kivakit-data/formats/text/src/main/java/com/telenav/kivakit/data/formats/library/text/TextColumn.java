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

package com.telenav.kivakit.data.formats.library.text;

import com.telenav.kivakit.core.kernel.interfaces.naming.Named;
import com.telenav.kivakit.data.formats.library.text.project.lexakai.diagrams.DiagramText;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramText.class)
public class TextColumn implements Named
{
    private final int index;

    private final String name;

    /**
     * @param name The name identifying the column.
     * @param index the zero-indexed location of the column within the row.
     */
    public TextColumn(final String name, final int index)
    {
        this.name = name;
        this.index = index;
    }

    @Override
    public String name()
    {
        return name;
    }

    int getIndex()
    {
        return index;
    }
}

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

package com.telenav.kivakit.kernel.data.conversion.string.enumeration;

import com.telenav.kivakit.kernel.data.conversion.string.collection.BaseListConverter;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramDataConversionOther;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Converts lists of enum values to and from comma-separated strings.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataConversionOther.class)
@LexakaiJavadoc(complete = true)
public class EnumListConverter<T extends Enum<T>> extends BaseListConverter<T>
{
    public EnumListConverter(final Listener listener, final Class<T> enumType)
    {
        super(listener, new EnumConverter<>(listener, enumType), DEFAULT_DELIMITER);
    }
}
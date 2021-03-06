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

package com.telenav.kivakit.conversion.core.language;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.lexakai.DiagramConversionLanguage;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Converts between {@link Class} objects and fully-qualified class names.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionLanguage.class)
@LexakaiJavadoc(complete = true)
public class ClassConverter extends BaseStringConverter<Class<?>>
{
    /**
     * @param listener The listener to hear any conversion issues
     */
    public ClassConverter(Listener listener)
    {
        super(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Class<?> onToValue(String value)
    {
        try
        {
            return Class.forName(value);
        }
        catch (ClassNotFoundException e)
        {
            problem(e, "Cannot load class: $", value);
            return null;
        }
    }
}

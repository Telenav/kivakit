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
import com.telenav.kivakit.core.string.CaseFormat;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;

/**
 * Converts between strings and enum values. Lower case hyphenated values are accepted. For example, the enum value
 * max-value is equivalent to MAX_VALUE.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionLanguage.class)
public class EnumConverter<T extends Enum<T>> extends BaseStringConverter<T>
{
    /** The type of enum */
    private final Class<T> enumType;

    /**
     * @param listener The listener to hear any conversion issues
     * @param enumType The enum type to convert
     */
    public EnumConverter(Listener listener, Class<T> enumType)
    {
        super(listener);
        ensureNotNull(enumType);
        this.enumType = enumType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected T onToValue(String value)
    {
        return Enum.valueOf(enumType, CaseFormat.lowerHyphenToUpperUnderscore(value));
    }
}

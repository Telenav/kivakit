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

package com.telenav.kivakit.kernel.data.conversion.string.collection;

import com.telenav.kivakit.kernel.data.conversion.string.StringConverter;
import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramDataConversionOther;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * A {@link StringConverter} that converts to and from a collection of values separated by the given delimiter. The type
 * parameter specifies the type of collection to use. For examples, see {@link BaseSetConverter} and {@link
 * BaseListConverter}.
 *
 * @author jonathanl (shibo)
 * @see BaseListConverter
 * @see BaseSetConverter
 */
@UmlClassDiagram(diagram = DiagramDataConversionOther.class)
public abstract class BaseCollectionConverter<T> extends BaseStringConverter<T>
{
    /** The default delimiter to use if no delimiter is specified */
    protected static final String DEFAULT_DELIMITER = ",";

    /** The string that is used to separate collection elements */
    private final String delimiter;

    /**
     * @param listener The conversion listener
     * @param delimiter The separator between collection elements
     */
    protected BaseCollectionConverter(final Listener listener, final String delimiter)
    {
        super(listener);
        this.delimiter = ensureNotNull(delimiter);
    }

    /**
     * @return The delimiter used to separate values
     */
    public String delimiter()
    {
        return delimiter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final T onConvertToObject(final String value)
    {
        return onConvertToObject(StringList.split(value, delimiter()));
    }

    /**
     * @return A collection from the given list of strings
     */
    protected abstract T onConvertToObject(StringList list);

    /**
     * {@inheritDoc}
     */
    @Override
    protected final String onConvertToString(final T value)
    {
        return onConvertToStringList(value).join(delimiter);
    }

    /**
     * @return A list of strings from the given collection
     */
    protected abstract StringList onConvertToStringList(T value);
}

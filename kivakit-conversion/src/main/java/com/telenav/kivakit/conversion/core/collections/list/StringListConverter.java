package com.telenav.kivakit.conversion.core.collections.list;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.lexakai.DiagramConversionOther;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.identifier.Identifier;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Converts to and from {@link Identifier}
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
@UmlClassDiagram(diagram = DiagramConversionOther.class)
public class StringListConverter extends BaseStringConverter<StringList>
{
    private final String delimiter;

    public StringListConverter(Listener listener)
    {
        this(listener, ",");
    }

    public StringListConverter(Listener listener, String delimiter)
    {
        super(listener);
        this.delimiter = delimiter;
    }

    @Override
    protected String onToString(final StringList strings)
    {
        return strings.join();
    }

    @Override
    protected StringList onToValue(final String value)
    {
        return StringList.split(value, delimiter);
    }
}

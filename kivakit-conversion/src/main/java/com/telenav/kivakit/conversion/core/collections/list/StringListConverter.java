package com.telenav.kivakit.conversion.core.collections.list;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionOther;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Converts to and from a {@link StringList}
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionOther.class)
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class StringListConverter extends BaseStringConverter<StringList>
{
    /** The delimiter to separate items in the list */
    private final String delimiter;

    /**
     * @param listener The listener to report problems to
     */
    public StringListConverter(Listener listener)
    {
        this(listener, ",");
    }

    /**
     * @param listener The listener to report problems to
     * @param delimiter The delimiter to separate items in the list
     */
    public StringListConverter(Listener listener, String delimiter)
    {
        super(listener);
        this.delimiter = delimiter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String onToString(StringList strings)
    {
        return strings.join();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected StringList onToValue(String value)
    {
        return StringList.split(value, delimiter);
    }
}

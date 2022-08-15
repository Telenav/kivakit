package com.telenav.kivakit.conversion.core.locale;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionOther;
import com.telenav.kivakit.core.locale.LanguageIsoCode;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Converts to and from a {@link LanguageIsoCode}. Both ISO2 and ISO3 values are supported.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
@UmlClassDiagram(diagram = DiagramConversionOther.class)
public class LanguageIsoCodeConverter extends BaseStringConverter<LanguageIsoCode>
{
    public LanguageIsoCodeConverter(Listener listener)
    {
        super(listener);
    }

    @Override
    protected String onToString(LanguageIsoCode value)
    {
        return value.iso2Code();
    }

    @Override
    protected LanguageIsoCode onToValue(String value)
    {
        return value.length() == 2
                ? LanguageIsoCode.forIso2Code(value)
                : LanguageIsoCode.forIso3Code(value);
    }
}

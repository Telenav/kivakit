package com.telenav.kivakit.conversion.core.locale;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionOther;
import com.telenav.kivakit.core.locale.LocaleLanguage;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.locale.LocaleLanguage.languageForIso2Code;
import static com.telenav.kivakit.core.locale.LocaleLanguage.languageForIso3Code;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;

/**
 * Converts to and from a {@link LocaleLanguage}. Both ISO2 and ISO3 values are supported.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionOther.class)
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class LocaleLanguageConverter extends BaseStringConverter<LocaleLanguage>
{
    public LocaleLanguageConverter(Listener listener)
    {
        super(listener, LocaleLanguage.class);
    }

    public LocaleLanguageConverter()
    {
        this(throwingListener());
    }

    @Override
    protected String onToString(LocaleLanguage value)
    {
        return value.iso2Code();
    }

    @Override
    protected LocaleLanguage onToValue(String value)
    {
        if (value.length() == 2)
        {
            return languageForIso2Code(value);
        }
        if (value.length() == 3)
        {
            return languageForIso3Code(value);
        }
        problem("Invalid locale language: $", value);
        return null;
    }
}

package com.telenav.kivakit.conversion.core.locale;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionOther;
import com.telenav.kivakit.core.locale.LocaleLanguage;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Converts to and from a {@link LocaleLanguage}. Both ISO2 and ISO3 values are supported.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionOther.class)
@ApiQuality(stability = API_STABLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class LocaleLanguageConverter extends BaseStringConverter<LocaleLanguage>
{
    public LocaleLanguageConverter(Listener listener)
    {
        super(listener);
    }

    @Override
    protected String onToString(LocaleLanguage value)
    {
        return value.iso2Code();
    }

    @Override
    protected LocaleLanguage onToValue(String value)
    {
        return value.length() == 2
                ? LocaleLanguage.languageForIso2Code(value)
                : LocaleLanguage.languageForIso3Code(value);
    }
}

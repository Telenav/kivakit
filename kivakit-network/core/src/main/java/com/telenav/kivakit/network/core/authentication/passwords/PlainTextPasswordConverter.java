package com.telenav.kivakit.network.core.authentication.passwords;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.network.core.authentication.Password;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Converts passwords to and from {@link Password} objects.
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class PlainTextPasswordConverter extends BaseStringConverter<Password>
{
    public PlainTextPasswordConverter(Listener listener)
    {
        super(listener, Password.class);
    }

    @Override
    protected Password onToValue(String value)
    {
        return PlainTextPassword.parsePlainTextPassword(this, value);
    }
}

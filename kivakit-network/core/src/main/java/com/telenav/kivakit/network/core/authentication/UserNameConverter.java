package com.telenav.kivakit.network.core.authentication;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Converts {@link UserName} objects to and from strings.
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class UserNameConverter extends BaseStringConverter<UserName>
{
    public UserNameConverter(Listener listener)
    {
        super(listener, UserName.class);
    }

    @Override
    protected UserName onToValue(String value)
    {
        return UserName.parseUserName(this, value);
    }
}

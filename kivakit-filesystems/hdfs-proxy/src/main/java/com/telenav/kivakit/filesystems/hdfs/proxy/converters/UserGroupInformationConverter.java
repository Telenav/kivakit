package com.telenav.kivakit.filesystems.hdfs.proxy.converters;

import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import org.apache.hadoop.security.UserGroupInformation;

/**
 * @author jonathanl (shibo)
 */
public class UserGroupInformationConverter extends BaseStringConverter<UserGroupInformation>
{
    public UserGroupInformationConverter(final Listener listener)
    {
        super(listener);
    }

    @Override
    protected UserGroupInformation onConvertToObject(final String value)
    {
        return UserGroupInformation.createRemoteUser(value);
    }
}

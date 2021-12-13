package com.telenav.kivakit.serialization.json;

import com.telenav.kivakit.kernel.messaging.Repeater;

public interface GsonFactorySource extends Repeater
{
    GsonFactory gsonFactory();
}

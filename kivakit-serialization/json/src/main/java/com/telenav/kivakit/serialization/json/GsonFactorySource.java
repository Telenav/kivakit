package com.telenav.kivakit.serialization.json;

import com.telenav.kivakit.core.messaging.Repeater;

public interface GsonFactorySource extends Repeater
{
    GsonFactory gsonFactory();
}

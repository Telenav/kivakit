package com.telenav.kivakit.serialization.gson.factory;

import com.telenav.kivakit.conversion.core.language.primitive.BooleanConverter;
import com.telenav.kivakit.conversion.core.time.DurationConverter;
import com.telenav.kivakit.conversion.core.time.FrequencyConverter;
import com.telenav.kivakit.conversion.core.value.BytesConverter;
import com.telenav.kivakit.conversion.core.value.ConfidenceConverter;
import com.telenav.kivakit.conversion.core.value.CountConverter;
import com.telenav.kivakit.conversion.core.value.LevelConverter;
import com.telenav.kivakit.conversion.core.value.MaximumConverter;
import com.telenav.kivakit.conversion.core.value.MinimumConverter;
import com.telenav.kivakit.conversion.core.value.PercentConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.Frequency;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.core.value.count.Minimum;
import com.telenav.kivakit.core.value.level.Confidence;
import com.telenav.kivakit.core.value.level.Level;
import com.telenav.kivakit.core.value.level.Percent;

public class CoreGsonFactory extends BaseGsonFactory
{
    public CoreGsonFactory(Listener listener)
    {
        super(listener);

        addConvertingSerializer(Boolean.class, new BooleanConverter(listener));
        addConvertingSerializer(Bytes.class, new BytesConverter(listener));
        addConvertingSerializer(Confidence.class, new ConfidenceConverter(listener));
        addConvertingSerializer(Count.class, new CountConverter(listener));
        addConvertingSerializer(Duration.class, new DurationConverter(listener));
        addConvertingSerializer(Frequency.class, new FrequencyConverter(listener));
        addConvertingSerializer(Level.class, new LevelConverter(listener));
        addConvertingSerializer(Maximum.class, new MaximumConverter(listener));
        addConvertingSerializer(Minimum.class, new MinimumConverter(listener));
        addConvertingSerializer(Percent.class, new PercentConverter(listener));

        ignoreField("objectName");
    }
}

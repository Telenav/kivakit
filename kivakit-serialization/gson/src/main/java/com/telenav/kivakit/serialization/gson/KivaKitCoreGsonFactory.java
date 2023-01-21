package com.telenav.kivakit.serialization.gson;

import com.google.gson.Gson;
import com.telenav.kivakit.conversion.core.language.ClassConverter;
import com.telenav.kivakit.conversion.core.language.PatternConverter;
import com.telenav.kivakit.conversion.core.time.duration.DurationConverter;
import com.telenav.kivakit.conversion.core.time.frequency.FrequencyConverter;
import com.telenav.kivakit.conversion.core.time.kivakit.KivaKitLocalDateTimeConverter;
import com.telenav.kivakit.conversion.core.time.utc.IsoDateTimeConverter;
import com.telenav.kivakit.conversion.core.time.zone.ZoneIdConverter;
import com.telenav.kivakit.conversion.core.value.BytesConverter;
import com.telenav.kivakit.conversion.core.value.ConfidenceConverter;
import com.telenav.kivakit.conversion.core.value.CountConverter;
import com.telenav.kivakit.conversion.core.value.EstimateConverter;
import com.telenav.kivakit.conversion.core.value.LevelConverter;
import com.telenav.kivakit.conversion.core.value.MaximumConverter;
import com.telenav.kivakit.conversion.core.value.MinimumConverter;
import com.telenav.kivakit.conversion.core.value.PercentConverter;
import com.telenav.kivakit.conversion.core.value.VersionConverter;

import java.time.ZoneId;

/**
 * Factory for creating {@link Gson} serializers that can convert common types in <i>kivakit-core</i>
 *
 * @author Jonathan Locke
 */
public class KivaKitCoreGsonFactory extends BaseGsonFactory
{
    public KivaKitCoreGsonFactory()
    {
        addSerializer(new BytesConverter());
        addSerializer(new ClassConverter());
        addSerializer(new ConfidenceConverter());
        addSerializer(new CountConverter());
        addSerializer(new DurationConverter());
        addSerializer(new EstimateConverter());
        addSerializer(new FrequencyConverter());
        addSerializer(new IsoDateTimeConverter());
        addSerializer(new KivaKitLocalDateTimeConverter(ZoneId.systemDefault()));
        addSerializer(new LevelConverter());
        addSerializer(new MaximumConverter());
        addSerializer(new MinimumConverter());
        addSerializer(new PatternConverter());
        addSerializer(new PercentConverter());
        addSerializer(new VersionConverter());
        addSerializer(new ZoneIdConverter());

        ignoreField("objectName");
    }
}

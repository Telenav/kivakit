package com.telenav.kivakit.commandline;

import com.telenav.kivakit.conversion.core.language.EnumConverter;
import com.telenav.kivakit.conversion.core.language.IdentityConverter;
import com.telenav.kivakit.conversion.core.language.PatternConverter;
import com.telenav.kivakit.conversion.core.language.primitive.BooleanConverter;
import com.telenav.kivakit.conversion.core.language.primitive.DoubleConverter;
import com.telenav.kivakit.conversion.core.language.primitive.IntegerConverter;
import com.telenav.kivakit.conversion.core.language.primitive.LongConverter;
import com.telenav.kivakit.conversion.core.time.DurationConverter;
import com.telenav.kivakit.conversion.core.time.LocalDateConverter;
import com.telenav.kivakit.conversion.core.time.LocalDateTimeConverter;
import com.telenav.kivakit.conversion.core.value.BytesConverter;
import com.telenav.kivakit.conversion.core.value.CountConverter;
import com.telenav.kivakit.conversion.core.value.MaximumConverter;
import com.telenav.kivakit.conversion.core.value.MinimumConverter;
import com.telenav.kivakit.conversion.core.value.PercentConverter;
import com.telenav.kivakit.conversion.core.value.QuantizableConverter;
import com.telenav.kivakit.conversion.core.value.VersionConverter;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.string.CaseFormat;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.LocalTime;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.core.value.count.Minimum;
import com.telenav.kivakit.core.value.level.Percent;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.core.vm.JavaVirtualMachine;
import com.telenav.kivakit.interfaces.factory.MapFactory;
import com.telenav.kivakit.interfaces.numeric.Quantizable;

import java.util.regex.Pattern;

import static com.telenav.kivakit.core.ensure.Ensure.illegalArgument;

public class SwitchParsers
{
    public static SwitchParser.Builder<Boolean> booleanSwitchParser(Listener listener, String name, String description)
    {
        return SwitchParser.builder(Boolean.class)
                .name(name)
                .converter(new BooleanConverter(listener))
                .description(description);
    }

    public static SwitchParser.Builder<Bytes> bytesSwitchParser(Listener listener, String name, String description)
    {
        return SwitchParser.builder(Bytes.class)
                .name(name)
                .converter(new BytesConverter(listener))
                .description(description);
    }

    public static SwitchParser.Builder<Count> countSwitchParser(Listener listener, String name, String description)
    {
        return SwitchParser.builder(Count.class)
                .name(name)
                .converter(new CountConverter(listener))
                .description(description);
    }

    public static SwitchParser.Builder<Double> doubleSwitchParser(Listener listener, String name, String description)
    {
        return SwitchParser.builder(Double.class)
                .name(name)
                .converter(new DoubleConverter(listener))
                .description(description);
    }

    public static SwitchParser.Builder<Duration> durationSwitchParser(Listener listener, String name,
                                                                      String description)
    {
        return SwitchParser.builder(Duration.class)
                .name(name)
                .description(description)
                .converter(new DurationConverter(listener));
    }

    public static <E extends Enum<E>> SwitchParser.Builder<E> enumSwitchParser(Listener listener,
                                                                               String name,
                                                                               String description,
                                                                               Class<E> type)
    {
        if (type.isEnum())
        {
            var options = new StringList();
            for (var option : type.getEnumConstants())
            {
                options.add(CaseFormat.upperUnderscoreToLowerHyphen(option.name()));
            }
            var help = description + "\n\n" + options.bulleted(4) + "\n";
            return SwitchParser.builder(type)
                    .name(name)
                    .converter(new EnumConverter<>(listener, type))
                    .description(help);
        }

        return illegalArgument("Not an enum: " + type);
    }

    public static SwitchParser.Builder<Integer> integerSwitchParser(Listener listener, String name, String description)
    {
        return SwitchParser.builder(Integer.class)
                .name(name)
                .converter(new IntegerConverter(listener))
                .description(description);
    }

    public static SwitchParser.Builder<LocalTime> localDateSwitchParser(Listener listener, String name,
                                                                        String description)
    {
        return SwitchParser.builder(LocalTime.class)
                .name(name)
                .description(description)
                .converter(new LocalDateConverter(listener));
    }

    public static SwitchParser.Builder<LocalTime> localDateTimeSwitchParser(Listener listener, String name,
                                                                            String description)
    {
        return SwitchParser.builder(LocalTime.class)
                .name(name)
                .description(description)
                .converter(new LocalDateTimeConverter(listener));
    }

    public static SwitchParser.Builder<Long> longSwitchParser(Listener listener, String name, String description)
    {
        return SwitchParser.builder(Long.class)
                .name(name)
                .converter(new LongConverter(listener))
                .description(description);
    }

    public static SwitchParser.Builder<Maximum> maximumSwitchParser(Listener listener, String name, String description)
    {
        return SwitchParser.builder(Maximum.class)
                .name(name)
                .converter(new MaximumConverter(listener))
                .description(description);
    }

    public static SwitchParser.Builder<Minimum> minimumSwitchParser(Listener listener, String name, String description)
    {
        return SwitchParser.builder(Minimum.class)
                .name(name)
                .converter(new MinimumConverter(listener))
                .description(description);
    }

    public static SwitchParser.Builder<Pattern> patternSwitchParser(Listener listener, String name, String description)
    {
        return SwitchParser.builder(Pattern.class)
                .name(name)
                .converter(new PatternConverter(listener))
                .description(description);
    }

    public static SwitchParser.Builder<Percent> percentSwitchParser(Listener listener, String name, String description)
    {
        return SwitchParser.builder(Percent.class)
                .name(name)
                .converter(new PercentConverter(listener))
                .description(description);
    }

    public static <T extends Quantizable> SwitchParser.Builder<T> quantizableSwitchParser(Listener listener,
                                                                                          String name,
                                                                                          String description,
                                                                                          Class<T> type,
                                                                                          MapFactory<Long, T> factory)
    {
        return SwitchParser.builder(type)
                .name(name)
                .description(description)
                .converter(new QuantizableConverter<>(listener, factory));
    }

    public static SwitchParser.Builder<String> stringSwitchParser(Listener listener, String name, String description)
    {
        return SwitchParser.builder(String.class)
                .name(name)
                .converter(new IdentityConverter(listener))
                .description(description);
    }

    public static SwitchParser<Count> threadCountSwitchParser(Listener listener, Count maximum)
    {
        var defaultThreads = maximum.minimize(JavaVirtualMachine.local().processors());
        return countSwitchParser(listener, "threads", "Number of threads to use (default is " + defaultThreads + ")")
                .optional()
                .defaultValue(defaultThreads)
                .build();
    }

    public static SwitchParser.Builder<Version> versionSwitchParser(Listener listener, String name, String description)
    {
        return SwitchParser.builder(Version.class)
                .name(name)
                .converter(new VersionConverter(listener))
                .description(description);
    }
}

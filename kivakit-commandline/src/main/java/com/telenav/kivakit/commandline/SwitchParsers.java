package com.telenav.kivakit.commandline;

import com.telenav.kivakit.annotations.code.CodeQuality;
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
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.ensure.Ensure.illegalArgument;

/**
 * Switch parser builder factories for common data types.
 *
 * <p><b>Primitives</b></p>
 *
 * <ul>
 *     <li>{@link #booleanSwitchParser(Listener, String, String)}</li>
 *     <li>{@link #integerSwitchParser(Listener, String, String)}</li>
 *     <li>{@link #longSwitchParser(Listener, String, String)}</li>
 *     <li>{@link #doubleSwitchParser(Listener, String, String)}</li>
 * </ul>
 *
 * <p><b>Values</b></p>
 *
 * <ul>
 *     <li>{@link #bytesSwitchParser(Listener, String, String)}</li>
 *     <li>{@link #countSwitchParser(Listener, String, String)}</li>
 *     <li>{@link #enumSwitchParser(Listener, String, String, Class)}</li>
 *     <li>{@link #maximumSwitchParser(Listener, String, String)}</li>
 *     <li>{@link #minimumSwitchParser(Listener, String, String)}</li>
 *     <li>{@link #patternSwitchParser(Listener, String, String)}</li>
 *     <li>{@link #percentSwitchParser(Listener, String, String)}</li>
 *     <li>{@link #stringSwitchParser(Listener, String, String)}</li>
 *     <li>{@link #threadCountSwitchParser(Listener, Count)}</li>
 *     <li>{@link #versionSwitchParser(Listener, String, String)}</li>
 * </ul>
 *
 * <p><b>Time</b></p>
 *
 * <ul>
 *     <li>{@link #durationSwitchParser(Listener, String, String)}</li>
 *     <li>{@link #localDateSwitchParser(Listener, String, String)}</li>
 *     <li>{@link #localDateTimeSwitchParser(Listener, String, String)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class SwitchParsers
{
    public static SwitchParser.Builder<Boolean> booleanSwitchParser(@NotNull Listener listener,
                                                                    @NotNull String name,
                                                                    @NotNull String description)
    {
        return SwitchParser.switchParserBuilder(Boolean.class)
                .name(name)
                .converter(new BooleanConverter(listener))
                .description(description);
    }

    public static SwitchParser.Builder<Bytes> bytesSwitchParser(@NotNull Listener listener,
                                                                @NotNull String name,
                                                                @NotNull String description)
    {
        return SwitchParser.switchParserBuilder(Bytes.class)
                .name(name)
                .converter(new BytesConverter(listener))
                .description(description);
    }

    public static SwitchParser.Builder<Count> countSwitchParser(@NotNull Listener listener,
                                                                @NotNull String name,
                                                                @NotNull String description)
    {
        return SwitchParser.switchParserBuilder(Count.class)
                .name(name)
                .converter(new CountConverter(listener))
                .description(description);
    }

    public static SwitchParser.Builder<Double> doubleSwitchParser(@NotNull Listener listener,
                                                                  @NotNull String name,
                                                                  @NotNull String description)
    {
        return SwitchParser.switchParserBuilder(Double.class)
                .name(name)
                .converter(new DoubleConverter(listener))
                .description(description);
    }

    public static SwitchParser.Builder<Duration> durationSwitchParser(@NotNull Listener listener,
                                                                      @NotNull String name,
                                                                      @NotNull String description)
    {
        return SwitchParser.switchParserBuilder(Duration.class)
                .name(name)
                .description(description)
                .converter(new DurationConverter(listener));
    }

    public static <E extends Enum<E>> SwitchParser.Builder<E> enumSwitchParser(@NotNull Listener listener,
                                                                               @NotNull String name,
                                                                               @NotNull String description,
                                                                               @NotNull Class<E> type)
    {
        if (type.isEnum())
        {
            var options = new StringList();
            for (var option : type.getEnumConstants())
            {
                options.add(CaseFormat.upperUnderscoreToLowerHyphen(option.name()));
            }
            var help = description + "\n\n" + options.bulleted(4) + "\n";
            return SwitchParser.switchParserBuilder(type)
                    .name(name)
                    .converter(new EnumConverter<>(listener, type))
                    .description(help);
        }

        return illegalArgument("Not an enum: " + type);
    }

    public static SwitchParser.Builder<Integer> integerSwitchParser(@NotNull Listener listener,
                                                                    @NotNull String name,
                                                                    @NotNull String description)
    {
        return SwitchParser.switchParserBuilder(Integer.class)
                .name(name)
                .converter(new IntegerConverter(listener))
                .description(description);
    }

    public static SwitchParser.Builder<LocalTime> localDateSwitchParser(@NotNull Listener listener,
                                                                        @NotNull String name,
                                                                        @NotNull String description)
    {
        return SwitchParser.switchParserBuilder(LocalTime.class)
                .name(name)
                .description(description)
                .converter(new LocalDateConverter(listener));
    }

    public static SwitchParser.Builder<LocalTime> localDateTimeSwitchParser(@NotNull Listener listener,
                                                                            @NotNull String name,
                                                                            @NotNull String description)
    {
        return SwitchParser.switchParserBuilder(LocalTime.class)
                .name(name)
                .description(description)
                .converter(new LocalDateTimeConverter(listener));
    }

    public static SwitchParser.Builder<Long> longSwitchParser(@NotNull Listener listener,
                                                              @NotNull String name,
                                                              @NotNull String description)
    {
        return SwitchParser.switchParserBuilder(Long.class)
                .name(name)
                .converter(new LongConverter(listener))
                .description(description);
    }

    public static SwitchParser.Builder<Maximum> maximumSwitchParser(@NotNull Listener listener,
                                                                    @NotNull String name,
                                                                    @NotNull String description)
    {
        return SwitchParser.switchParserBuilder(Maximum.class)
                .name(name)
                .converter(new MaximumConverter(listener))
                .description(description);
    }

    public static SwitchParser.Builder<Minimum> minimumSwitchParser(@NotNull Listener listener,
                                                                    @NotNull String name,
                                                                    @NotNull String description)
    {
        return SwitchParser.switchParserBuilder(Minimum.class)
                .name(name)
                .converter(new MinimumConverter(listener))
                .description(description);
    }

    public static SwitchParser.Builder<Pattern> patternSwitchParser(@NotNull Listener listener,
                                                                    @NotNull String name,
                                                                    @NotNull String description)
    {
        return SwitchParser.switchParserBuilder(Pattern.class)
                .name(name)
                .converter(new PatternConverter(listener))
                .description(description);
    }

    public static SwitchParser.Builder<Percent> percentSwitchParser(@NotNull Listener listener,
                                                                    @NotNull String name,
                                                                    @NotNull String description)
    {
        return SwitchParser.switchParserBuilder(Percent.class)
                .name(name)
                .converter(new PercentConverter(listener))
                .description(description);
    }

    public static SwitchParser.Builder<String> stringSwitchParser(@NotNull Listener listener,
                                                                  @NotNull String name,
                                                                  @NotNull String description)
    {
        return SwitchParser.switchParserBuilder(String.class)
                .name(name)
                .converter(new IdentityConverter(listener))
                .description(description);
    }

    public static SwitchParser<Count> threadCountSwitchParser(@NotNull Listener listener,
                                                              @NotNull Count maximum)
    {
        var defaultThreads = maximum.minimize(JavaVirtualMachine.javaVirtualMachine().processors());
        return countSwitchParser(listener, "threads", "Number of threads to use (default is " + defaultThreads + ")")
                .optional()
                .defaultValue(defaultThreads)
                .build();
    }

    public static SwitchParser.Builder<Version> versionSwitchParser(@NotNull Listener listener,
                                                                    @NotNull String name,
                                                                    @NotNull String description)
    {
        return SwitchParser.switchParserBuilder(Version.class)
                .name(name)
                .converter(new VersionConverter(listener))
                .description(description);
    }
}

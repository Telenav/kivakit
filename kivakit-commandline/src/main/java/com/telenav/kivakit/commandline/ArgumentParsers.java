package com.telenav.kivakit.commandline;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.conversion.core.language.IdentityConverter;
import com.telenav.kivakit.conversion.core.language.primitive.BooleanConverter;
import com.telenav.kivakit.conversion.core.language.primitive.IntegerConverter;
import com.telenav.kivakit.conversion.core.language.primitive.LongConverter;
import com.telenav.kivakit.conversion.core.value.VersionConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.version.Version;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.commandline.ArgumentParser.argumentParserBuilder;

/**
 * Argument parsers builder factories for common data types.
 *
 * <p><b>Primitives</b></p>
 *
 * <ul>
 *     <li>{@link #booleanArgumentParser(Listener, String)}</li>
 *     <li>{@link #integerArgumentParser(Listener, String)}</li>
 *     <li>{@link #longArgumentParser(Listener, String)}</li>
 * </ul>
 *
 * <p><b>Values</b></p>
 *
 * <ul>
 *     <li>{@link #stringArgumentParser(Listener, String)}</li>
 *     <li>{@link #versionArgumentParser(Listener, String)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class ArgumentParsers
{
    public static ArgumentParser.Builder<Boolean> booleanArgumentParser(Listener listener, String description)
    {
        return argumentParserBuilder(Boolean.class)
                .converter(new BooleanConverter(listener))
                .description(description);
    }

    public static ArgumentParser.Builder<Integer> integerArgumentParser(Listener listener, String description)
    {
        return argumentParserBuilder(Integer.class)
                .converter(new IntegerConverter(listener))
                .description(description);
    }

    public static ArgumentParser.Builder<Long> longArgumentParser(Listener listener, String description)
    {
        return argumentParserBuilder(Long.class)
                .converter(new LongConverter(listener))
                .description(description);
    }

    public static ArgumentParser.Builder<String> stringArgumentParser(Listener listener, String description)
    {
        return argumentParserBuilder(String.class)
                .converter(new IdentityConverter(listener))
                .description(description);
    }

    public static ArgumentParser.Builder<Version> versionArgumentParser(Listener listener, String description)
    {
        return argumentParserBuilder(Version.class)
                .converter(new VersionConverter(listener))
                .description(description);
    }
}

package com.telenav.kivakit.conversion.core.value;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.project.lexakai.DiagramConversionValue;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.regex.Pattern;

/**
 * Converts to and from {@link Bytes}. Valid suffixes are (case-insensitive):
 *
 * <ul>
 *     <li>b - bytes</li>
 *     <li>k - kilobytes</li>
 *     <li>m - megabytes</li>
 *     <li>g - gigabytes</li>
 *     <li>t - terabytes</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
@UmlClassDiagram(diagram = DiagramConversionValue.class)
public class BytesConverter extends BaseStringConverter<Bytes>
{
    /** Pattern for string parsing. */
    private static final Pattern PATTERN = Pattern.compile("([0-9]+([.,][0-9]+)?)\\s*(|K|M|G|T)B?",
            Pattern.CASE_INSENSITIVE);

    public BytesConverter(Listener listener)
    {
        super(listener);
    }

    @Override
    protected Bytes onToValue(String value)
    {
        var matcher = PATTERN.matcher(value);

        // Valid input?
        if (matcher.matches())
        {
            // Get double precision value
            var scalar = Double.parseDouble(Strings.replaceAll(matcher.group(1), ",", ""));

            // Get units specified
            var units = matcher.group(3);

            if ("".equalsIgnoreCase(units))
            {
                return Bytes.bytes(scalar);
            }
            else if ("K".equalsIgnoreCase(units))
            {
                return Bytes.kilobytes(scalar);
            }
            else if ("M".equalsIgnoreCase(units))
            {
                return Bytes.megabytes(scalar);
            }
            else if ("G".equalsIgnoreCase(units))
            {
                return Bytes.gigabytes(scalar);
            }
            else if ("T".equalsIgnoreCase(units))
            {
                return Bytes.terabytes(scalar);
            }
            else
            {
                problem("Unrecognized units: ${debug}", value);
                return null;
            }
        }
        else
        {
            problem("Unable to parse: ${debug}", value);
            return null;
        }
    }
}

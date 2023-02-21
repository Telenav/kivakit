package com.telenav.kivakit.resource;

import com.telenav.kivakit.core.collections.list.StringList;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Holds scheme information from a URI:
 *
 * <pre>[scheme ":"]* ["//" authority]? [path] ["?" query]? ["#" fragment]?</pre>
 *
 * @author Jonathan Locke
 */
public class UriSchemes extends StringList
{
    private static final Pattern SCHEME_PATTERN = Pattern.compile("(?<scheme>[A-Za-z0-9]+):(?<rest>.*)");

    @NotNull
    public static UriSchemes uriScheme(String scheme)
    {
        var schemes = uriSchemes();
        schemes.add(scheme);
        return schemes;
    }

    @NotNull
    public static UriSchemes uriSchemes()
    {
        return new UriSchemes();
    }

    @NotNull
    public static UriSchemes uriSchemes(String path)
    {
        var schemes = new UriSchemes();
        while (true)
        {
            var matcher = SCHEME_PATTERN.matcher(path);
            if (matcher.lookingAt())
            {
                schemes.add(matcher.group("scheme"));
                path = matcher.group("rest");
            }
            else
            {
                break;
            }
        }
        return schemes;
    }

    protected UriSchemes()
    {
    }

    protected UriSchemes(UriSchemes that)
    {
        super(that);
    }

    @Override
    public UriSchemes copy()
    {
        return new UriSchemes(this);
    }

    public boolean isFile()
    {
        return size() == 1 && contains("file");
    }

    @Override
    public String toString()
    {
        return isEmpty() ? "" : join(":") + ":";
    }
}

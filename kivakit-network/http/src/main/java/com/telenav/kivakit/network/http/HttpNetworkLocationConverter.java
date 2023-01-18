package com.telenav.kivakit.network.http;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.network.core.NetworkPath;
import com.telenav.kivakit.network.core.QueryParameters;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Converts to and from {@link HttpNetworkLocation}
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class HttpNetworkLocationConverter extends BaseStringConverter<HttpNetworkLocation>
{
    public HttpNetworkLocationConverter(Listener listener)
    {
        super(listener, HttpNetworkLocation.class);
    }

    @Override
    protected HttpNetworkLocation onToValue(String value)
    {
        try
        {
            // NOTE: This code is very similar to the code in HttpNetworkLocationConverter
            var uri = new URI(value);
            var url = uri.toURL();
            var location = new HttpNetworkLocation(NetworkPath.networkPath(this, uri));
            var query = url.getQuery();
            if (query != null)
            {
                location.queryParameters(QueryParameters.parseQueryParameters(this, query));
            }
            location.reference(url.getRef());
            return location;
        }
        catch (URISyntaxException | MalformedURLException e)
        {
            problem(e, "Bad network location ${debug}", value);
            return null;
        }
    }
}

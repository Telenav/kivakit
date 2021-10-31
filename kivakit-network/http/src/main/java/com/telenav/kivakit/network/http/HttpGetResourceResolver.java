package com.telenav.kivakit.network.http;

import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.network.http.secure.SecureHttpNetworkLocation;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceIdentifier;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResourceService;
import com.telenav.kivakit.resource.spi.ResourceResolver;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;

/**
 * Resolves {@link ResourceIdentifier}s that are file paths into file {@link Resource}s.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceService.class)
@LexakaiJavadoc(complete = true)
public class HttpGetResourceResolver implements ResourceResolver
{
    @Override
    public boolean accepts(final ResourceIdentifier resourceIdentifier)
    {
        var identifier = resourceIdentifier.identifier();
        return identifier.startsWith("https:") || identifier.startsWith("http:");
    }

    @Override
    public Resource resolve(Listener listener, ResourceIdentifier resourceIdentifier)
    {
        var identifier = resourceIdentifier.identifier();
        if (identifier.startsWith("http:"))
        {
            return new HttpNetworkLocation.Converter(listener).convert(identifier).get();
        }
        if (identifier.startsWith("https:"))
        {
            return new SecureHttpNetworkLocation.Converter(listener).convert(identifier).get();
        }

        return fail("Internal error: should not be possible");
    }
}
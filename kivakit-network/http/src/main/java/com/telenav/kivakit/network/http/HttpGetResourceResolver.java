package com.telenav.kivakit.network.http;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.network.http.secure.SecureHttpNetworkLocation;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceIdentifier;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceService;
import com.telenav.kivakit.resource.spi.ResourceResolver;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.fail;

/**
 * Resolves {@link ResourceIdentifier}s that are file paths into file {@link Resource}s.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceService.class)
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class HttpGetResourceResolver implements ResourceResolver
{
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accepts(@NotNull ResourceIdentifier resourceIdentifier)
    {
        var identifier = resourceIdentifier.identifier();
        return identifier.startsWith("https:") || identifier.startsWith("http:");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resource resolve(@NotNull ResourceIdentifier resourceIdentifier)
    {
        var identifier = resourceIdentifier.identifier();
        if (identifier.startsWith("http:"))
        {
            var location = new HttpNetworkLocation.Converter(this).convert(identifier);
            if (location != null)
            {
                return location.get();
            }
        }
        if (identifier.startsWith("https:"))
        {
            var location = new SecureHttpNetworkLocation.Converter(this).convert(identifier);
            if (location != null)
            {
                return location.get();
            }
        }

        return fail("Internal error: should not be possible");
    }
}

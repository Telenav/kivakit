////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.spi;

import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.Debug;
import com.telenav.kivakit.core.resource.ResourceFolder;
import com.telenav.kivakit.core.resource.ResourceFolderIdentifier;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourceService;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

@UmlClassDiagram(diagram = DiagramResourceService.class)
public class ResourceFolderResolverServiceLoader
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Debug DEBUG = new Debug(LOGGER);

    @UmlAggregation
    private static List<ResourceFolderResolver> resolvers;

    public static ResourceFolder resolve(final ResourceFolderIdentifier identifier)
    {
        for (final var factory : resolvers())
        {
            if (factory.accepts(identifier))
            {
                return factory.resolve(identifier);
            }
        }

        return fail("Invalid resource identifier '$'", identifier);
    }

    private static synchronized List<ResourceFolderResolver> resolvers()
    {
        if (resolvers == null)
        {
            resolvers = new ArrayList<>();
            for (final var resolver : ServiceLoader.load(ResourceFolderResolver.class))
            {
                DEBUG.trace("Loaded resource factory '${class}'", resolver.getClass());
                resolvers.add(resolver);
            }
        }
        return resolvers;
    }
}

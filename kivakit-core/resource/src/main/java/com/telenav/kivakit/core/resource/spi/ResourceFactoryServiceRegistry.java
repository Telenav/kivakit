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
import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.core.resource.ResourceIdentifier;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourceService;
import com.telenav.kivakit.core.resource.resources.packaged.PackageResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

@UmlClassDiagram(diagram = DiagramResourceService.class)
public class ResourceFactoryServiceRegistry
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Debug DEBUG = new Debug(LOGGER);

    private static final PackageResource.Factory FACTORY = new PackageResource.Factory();

    @UmlAggregation
    private static List<ResourceFactoryService> services;

    public static Resource forIdentifier(final ResourceIdentifier identifier)
    {
        if (FACTORY.accepts(identifier))
        {
            return FACTORY.newResource(identifier);
        }

        for (final var factory : services())
        {
            if (factory.accepts(identifier))
            {
                return factory.newResource(identifier);
            }
        }

        return fail("Invalid resource identifier '$'", identifier);
    }

    private static synchronized List<ResourceFactoryService> services()
    {
        if (services == null)
        {
            services = new ArrayList<>();
            for (final var service : ServiceLoader.load(ResourceFactoryService.class))
            {
                DEBUG.trace("Loaded resource factory '${class}'", service.getClass());
                services.add(service);
            }
        }
        return services;
    }
}

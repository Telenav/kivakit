////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.filesystem.loader;

import com.telenav.kivakit.core.filesystem.local.LocalFileSystemService;
import com.telenav.kivakit.core.filesystem.spi.FileSystemService;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.Debug;
import com.telenav.kivakit.core.resource.path.FilePath;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramFileSystemService;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

@UmlClassDiagram(diagram = DiagramFileSystemService.class)
@UmlNotPublicApi
public class FileSystemServiceLoader
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Debug DEBUG = new Debug(LOGGER);

    private static final LocalFileSystemService LOCAL = new LocalFileSystemService();

    @UmlAggregation(label = "loads")
    private static List<FileSystemService> services;

    public static FileSystemService fileSystem(final FilePath path)
    {
        assert path != null;
        for (final var service : services())
        {
            if (service.accepts(path))
            {
                return service;
            }
        }
        if (LOCAL.accepts(path))
        {
            return LOCAL;
        }
        return fail("No file system service understands '$'", path);
    }

    private static synchronized List<FileSystemService> services()
    {
        if (services == null)
        {
            services = new ArrayList<>();
            for (final var service : ServiceLoader.load(FileSystemService.class))
            {
                DEBUG.trace("Loaded file system '${class}'", service.getClass());
                services.add(service);
            }
        }
        return services;
    }
}

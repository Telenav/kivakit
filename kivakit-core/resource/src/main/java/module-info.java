////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

import com.telenav.kivakit.core.filesystem.File;
import com.telenav.kivakit.core.filesystem.Folder;
import com.telenav.kivakit.core.filesystem.spi.FileSystemService;
import com.telenav.kivakit.core.resource.resources.packaged.Package;
import com.telenav.kivakit.core.resource.resources.packaged.PackageResource;
import com.telenav.kivakit.core.resource.spi.ResourceFolderResolver;
import com.telenav.kivakit.core.resource.spi.ResourceResolver;

open module kivakit.core.resource
{
    requires transitive kivakit.core.commandline;
    requires transitive kivakit.core.serialization.core;
    requires transitive kivakit.core.collections;

    uses FileSystemService;
    uses ResourceResolver;
    uses ResourceFolderResolver;

    provides ResourceResolver with File.Resolver, PackageResource.Resolver;
    provides ResourceFolderResolver with Folder.Resolver, Package.Resolver;

    exports com.telenav.kivakit.core.filesystem.spi;
    exports com.telenav.kivakit.core.filesystem;

    exports com.telenav.kivakit.core.resource;
    exports com.telenav.kivakit.core.resource.compression;
    exports com.telenav.kivakit.core.resource.compression.archive;
    exports com.telenav.kivakit.core.resource.compression.codecs;
    exports com.telenav.kivakit.core.resource.path;
    exports com.telenav.kivakit.core.resource.project;
    exports com.telenav.kivakit.core.resource.reading;
    exports com.telenav.kivakit.core.resource.resources.jar.launcher;
    exports com.telenav.kivakit.core.resource.resources.other;
    exports com.telenav.kivakit.core.resource.resources.packaged;
    exports com.telenav.kivakit.core.resource.resources.streamed;
    exports com.telenav.kivakit.core.resource.resources.string;
    exports com.telenav.kivakit.core.resource.spi;
    exports com.telenav.kivakit.core.resource.store;
    exports com.telenav.kivakit.core.resource.writing;
}

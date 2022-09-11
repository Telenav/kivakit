////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystem.spi.FileSystemService;
import com.telenav.kivakit.resource.packages.Package;
import com.telenav.kivakit.resource.packages.PackageResource;
import com.telenav.kivakit.resource.spi.ResourceFolderResolver;
import com.telenav.kivakit.resource.spi.ResourceResolver;

open module kivakit.resource
{
    uses FileSystemService;
    uses ResourceResolver;
    uses ResourceFolderResolver;

    provides ResourceResolver with File.Resolver, PackageResource.Resolver;
    provides ResourceFolderResolver with Folder.Resolver, Package.Resolver;

    // KivaKit
    requires transitive kivakit.commandline;
    requires transitive kivakit.collections;

    // Lexakai
    requires lexakai.annotations;

    // Java
    requires java.prefs;

    // Module exports
    exports com.telenav.kivakit.filesystem.spi;
    exports com.telenav.kivakit.filesystem;
    exports com.telenav.kivakit.launcher;
    exports com.telenav.kivakit.properties;
    exports com.telenav.kivakit.resource.compression.archive;
    exports com.telenav.kivakit.resource.compression.codecs;
    exports com.telenav.kivakit.resource.compression;
    exports com.telenav.kivakit.resource.packages;
    exports com.telenav.kivakit.resource.reading;
    exports com.telenav.kivakit.resource.resources;
    exports com.telenav.kivakit.resource.serialization;
    exports com.telenav.kivakit.resource.spi;
    exports com.telenav.kivakit.resource.writing;
    exports com.telenav.kivakit.resource;

    // Lexakai
    exports com.telenav.kivakit.resource.internal.lexakai; // to kivakit.network.http;
}

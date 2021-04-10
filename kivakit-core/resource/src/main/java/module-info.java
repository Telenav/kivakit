////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
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
    exports com.telenav.kivakit.core.resource.writing;
}

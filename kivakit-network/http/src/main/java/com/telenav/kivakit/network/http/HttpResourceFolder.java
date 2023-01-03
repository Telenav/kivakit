package com.telenav.kivakit.network.http;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.Extension;
import com.telenav.kivakit.resource.FileName;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.ResourceFolderIdentifier;
import com.telenav.kivakit.resource.ResourceList;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.ResourcePathed;
import com.telenav.kivakit.resource.writing.WritableResource;
import org.jetbrains.annotations.NotNull;

import java.net.URI;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.network.core.NetworkAccessConstraints.defaultNetworkAccessConstraints;

/**
 * A simple {@link ResourceFolder} abstraction for HTTP resources.
 *
 * @author jonathanl (shibo)
 */
public class HttpResourceFolder extends BaseRepeater implements
        ResourceFolder<HttpResourceFolder>
{
    private final HttpNetworkLocation location;

    public HttpResourceFolder(URI uri)
    {
        this.location = new HttpNetworkLocation(uri);
    }

    public HttpResourceFolder(HttpNetworkLocation location)
    {
        this.location = location;
    }

    @Override
    public boolean delete()
    {
        return unsupported();
    }

    @Override
    public boolean exists()
    {
        return unsupported();
    }

    @Override
    public HttpResourceFolder folder(String path)
    {
        return new HttpResourceFolder(location.child(path));
    }

    @Override
    public ObjectList<HttpResourceFolder> folders()
    {
        return unsupported();
    }

    @Override
    public boolean isMaterialized()
    {
        return false;
    }

    public HttpNetworkLocation location()
    {
        return location;
    }

    @Override
    public ResourceFolder<?> newFolder(@NotNull ResourcePath relativePath)
    {
        return unsupported();
    }

    @Override
    public ResourceFolder<?> parent()
    {
        var parent = location.parent();
        return parent == null
                ? null
                : new HttpResourceFolder(parent);
    }

    @Override
    public ResourcePath path()
    {
        return location.resource().path();
    }

    @Override
    public boolean renameTo(@NotNull ResourceFolder<?> folder)
    {
        return unsupported();
    }

    @Override
    public Resource resource(@NotNull ResourcePathed name)
    {
        return new HttpGetResource(location.child(name.toString()), defaultNetworkAccessConstraints());
    }

    @Override
    public ResourceFolderIdentifier resourceFolderIdentifier()
    {
        return new ResourceFolderIdentifier(location.toString());
    }

    @Override
    public ResourceList resources(@NotNull Matcher<ResourcePathed> matcher)
    {
        return unsupported();
    }

    @Override
    public WritableResource temporaryFile(@NotNull FileName baseName, @NotNull Extension extension)
    {
        return unsupported();
    }
}

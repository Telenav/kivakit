package com.telenav.kivakit.resource;

import com.telenav.kivakit.resource.resources.packaged.Package;
import com.telenav.kivakit.resource.resources.packaged.PackageResource;

public interface ResourceTrait
{
    /**
     * @return The resource at the given path relative to this component's class
     */
    default Resource packageResource(final String path)
    {
        return PackageResource.of(getClass(), path);
    }

    default Package relativePackage(final String path)
    {
        return Package.of(getClass(), path);
    }
}

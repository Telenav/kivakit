package com.telenav.kivakit.resource;

import com.telenav.kivakit.kernel.messaging.Repeater;
import com.telenav.kivakit.resource.resources.packaged.Package;
import com.telenav.kivakit.resource.resources.packaged.PackageResource;

public interface ResourceTrait extends Repeater
{
    /**
     * @return The resource at the given path relative to this component's class
     */
    default Resource packageResource(String path)
    {
        return PackageResource.packageResource(this, getClass(), path);
    }

    default Package relativePackage(String path)
    {
        return Package.packageFrom(this, getClass(), path);
    }
}

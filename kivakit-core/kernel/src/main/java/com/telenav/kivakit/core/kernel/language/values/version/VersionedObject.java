////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.values.version;

import com.telenav.kivakit.core.kernel.KivaKit;
import com.telenav.kivakit.core.kernel.interfaces.naming.NamedObject;
import com.telenav.kivakit.core.kernel.language.values.name.Name;

/**
 * An object of a particular version. Used in kryo serialization. For example FieldArchive and ZipArchive both have
 * methods that serialize objects along with their versions using this object. This can ensure that all values written
 * to the archive have versions, for future backwards compatibility
 *
 * @author jonathanl (shibo)
 */
public class VersionedObject<T> implements NamedObject, Versioned
{
    private final Version version;

    private final T object;

    public VersionedObject(final Version version, final T object)
    {
        this.version = version;
        this.object = object;
    }

    public VersionedObject(final T object)
    {
        this(KivaKit.get().version(), object);
    }

    /**
     * @return The object
     */
    public T get()
    {
        return object;
    }

    @Override
    public String objectName()
    {
        return Name.synthetic(object);
    }

    @Override
    public String toString()
    {
        return objectName();
    }

    /**
     * @return The version of the object
     */
    @Override
    public Version version()
    {
        return version;
    }
}

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

package com.telenav.kivakit.kernel.language.values.version;

import com.telenav.kivakit.kernel.KivaKit;
import com.telenav.kivakit.kernel.interfaces.naming.NamedObject;
import com.telenav.kivakit.kernel.language.values.name.Name;

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
        this(KivaKit.get().projectVersion(), object);
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

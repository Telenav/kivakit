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

package com.telenav.kivakit.core.version;

import com.telenav.kivakit.core.language.Hash;
import com.telenav.kivakit.core.language.Objects;
import com.telenav.kivakit.interfaces.naming.NamedObject;

/**
 * An object of a particular version. Used in kryo serialization. For example FieldArchive and ZipArchive both have
 * methods that serialize objects along with their versions using this object. This can ensure that all values written
 * to the archive have versions, for future backwards compatibility
 *
 * @author jonathanl (shibo)
 */
public class VersionedObject<T> implements
        NamedObject,
        Versioned
{
    private T object;

    private Version version;

    public VersionedObject()
    {
    }

    public VersionedObject(T object, Version version)
    {
        this.version = version;
        this.object = object;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof VersionedObject)
        {
            var that = (VersionedObject<?>) object;
            return Objects.equalPairs(this.object, that.object, this.version, that.version);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Hash.many(this.object, this.version);
    }

    /**
     * @return The object
     */
    public T object()
    {
        return object;
    }

    @Override
    public String objectName()
    {
        return NamedObject.syntheticName(object);
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

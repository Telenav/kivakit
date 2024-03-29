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

package com.telenav.kivakit.interfaces.naming;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.interfaces.internal.lexakai.DiagramNaming;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static java.lang.System.identityHashCode;

/**
 * An object with a <i>programmatic</i> name. An object that is a {@link NamedObject} differs from an object that is
 * {@link Named} in that its name is meant for use by programmers and not end users. If {@link #objectName()} is not
 * implemented, a synthetic name for the object is created with {@link #syntheticName(Object)}.
 * <p>
 * By default, {@link NamedObject}s are not {@link Nameable} unless the object implementing this interface allows it.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramNaming.class)
@TypeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public interface NamedObject extends Nameable
{
    /**
     * Returns true if the given name is synthetic
     */
    static boolean isSyntheticName(String name)
    {
        return name != null && name.startsWith("object:");
    }

    /**
     * Returns returns a synthetic name for the given object
     */
    static String syntheticName(Object object)
    {
        return "object:" + object.getClass().getName().replaceAll("\\$", ".")
                + ":" + Integer.toString(identityHashCode(object), 16);
    }

    /**
     * Returns true if this object has a synthetic name
     */
    default boolean hasSyntheticName()
    {
        return isSyntheticName(objectName());
    }

    /**
     * Returns the name of this object for use in programming and debugging. If this method is not overridden, the name
     * will be the simple class name in hyphenated form followed by this object's identity hash code in hexadecimal.
     */
    default String objectName()
    {
        return syntheticName(this);
    }

    /**
     * Sets the name of this object
     *
     * @param name The object's new name
     */
    default void objectName(String name)
    {
    }
}

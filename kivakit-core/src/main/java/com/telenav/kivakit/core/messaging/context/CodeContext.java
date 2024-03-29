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

package com.telenav.kivakit.core.messaging.context;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramThread;
import com.telenav.kivakit.core.language.packaging.PackageReference;
import com.telenav.kivakit.interfaces.value.Source;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.reflect.Method;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.language.Classes.classForName;
import static com.telenav.kivakit.core.language.Classes.simpleName;
import static com.telenav.kivakit.core.language.packaging.PackageReference.packageReference;

/**
 * Information about a location in code, including the host and class. Line numbers are not available.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramThread.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class CodeContext
{
    private static Source<String> hostResolver = () -> "localhost";

    /**
     * Sets the resolver to use to find hosts
     *
     * @param resolver The host resolver
     */
    public static void hostResolver(Source<String> resolver)
    {
        hostResolver = resolver;
    }

    /** The class */
    private transient Class<?> type;

    /** The full name of the class */
    private String fullTypeName;

    /** The short name of the class */
    private String typeName;

    /** The host */
    private final String host = hostResolver.get();

    public CodeContext(Class<?> type)
    {
        this.type = type;
        fullTypeName = type.getName();
        typeName = simpleName(type);
    }

    public CodeContext(Method callerOf)
    {
        if (callerOf != null)
        {
            type = callerOf.getDeclaringClass();
            fullTypeName = type.getName();
            typeName = simpleName(type);
        }
    }

    public CodeContext(String locationName)
    {
        type = null;
        fullTypeName = locationName;
        typeName = locationName;
    }

    protected CodeContext()
    {
    }

    /**
     * Returns the full name of the code context's type
     */
    public String fullTypeName()
    {
        return fullTypeName;
    }

    /**
     * Returns code context's host
     */
    public String host()
    {
        return host;
    }

    /**
     * Returns the package for the code context's type
     */
    public PackageReference packagePath()
    {
        return packageReference(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return typeName();
    }

    /**
     * Returns the code context class
     */
    public Class<?> type()
    {
        if (type == null)
        {
            type = classForName(fullTypeName);
        }
        return type;
    }

    /**
     * Returns the simple type name for this code context
     */
    public String typeName()
    {
        return typeName;
    }
}

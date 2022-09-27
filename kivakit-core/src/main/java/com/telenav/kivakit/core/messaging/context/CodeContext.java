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

import com.telenav.kivakit.core.language.Classes;
import com.telenav.kivakit.core.language.module.PackageReference;
import com.telenav.kivakit.core.internal.lexakai.DiagramThread;
import com.telenav.kivakit.interfaces.value.Source;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.reflect.Method;

/**
 * Information about a location in code, including the host and class. Line numbers are not available.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramThread.class)
public class CodeContext
{
    private static Source<String> hostResolver = () -> "localhost";

    public static void hostResolver(Source<String> resolver)
    {
        hostResolver = resolver;
    }

    private transient Class<?> type;

    private String fullTypeName;

    private String typeName;

    private final String host = hostResolver.get();

    public CodeContext(Class<?> type)
    {
        this.type = type;
        fullTypeName = type.getName();
        typeName = Classes.simpleName(type);
    }

    public CodeContext(Method callerOf)
    {
        if (callerOf != null)
        {
            type = callerOf.getDeclaringClass();
            fullTypeName = type.getName();
            typeName = Classes.simpleName(type);
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

    public String fullTypeName()
    {
        return fullTypeName;
    }

    public String host()
    {
        return host;
    }

    public PackageReference packagePath()
    {
        return PackageReference.packageReference(type);
    }

    @Override
    public String toString()
    {
        return typeName();
    }

    public Class<?> type()
    {
        if (type == null)
        {
            type = Classes.classForName(fullTypeName);
        }
        return type;
    }

    public String typeName()
    {
        return typeName;
    }
}

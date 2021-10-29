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

package com.telenav.kivakit.kernel.language.threading.context;

import com.telenav.kivakit.kernel.interfaces.value.Source;
import com.telenav.kivakit.kernel.language.paths.PackagePath;
import com.telenav.kivakit.kernel.language.reflection.Method;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.types.Classes;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageThread;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Information about a location in code, including the host and class. Line numbers are not available.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageThread.class)
public class CodeContext
{
    private static Source<String> hostResolver = () -> "localhost";

    public static void hostResolver(Source<String> resolver)
    {
        hostResolver = resolver;
    }

    private transient Class<?> type;

    private String fullTypeName;

    @KivaKitIncludeProperty
    private String typeName;

    @KivaKitIncludeProperty
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
            type = callerOf.typeClass();
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

    public PackagePath packagePath()
    {
        return PackagePath.packagePath(type);
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
            type = Classes.forName(fullTypeName);
        }
        return type;
    }

    public String typeName()
    {
        return typeName;
    }
}

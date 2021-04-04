////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.threading.context;

import com.telenav.kivakit.core.kernel.interfaces.value.Source;
import com.telenav.kivakit.core.kernel.language.paths.PackagePath;
import com.telenav.kivakit.core.kernel.language.reflection.Method;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.types.Classes;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageThread;
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

    public static void hostResolver(final Source<String> resolver)
    {
        hostResolver = resolver;
    }

    private transient Class<?> type;

    private String fullTypeName;

    @KivaKitIncludeProperty
    private String typeName;

    @KivaKitIncludeProperty
    private final String host = hostResolver.get();

    public CodeContext(final Class<?> type)
    {
        this.type = type;
        fullTypeName = type.getName();
        typeName = Classes.simpleName(type);
    }

    public CodeContext(final Method callerOf)
    {
        if (callerOf != null)
        {
            type = callerOf.type();
            fullTypeName = type.getName();
            typeName = Classes.simpleName(type);
        }
    }

    public CodeContext(final String locationName)
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

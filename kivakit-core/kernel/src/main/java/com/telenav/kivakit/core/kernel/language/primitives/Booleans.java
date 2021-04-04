////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.primitives;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguagePrimitive;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramLanguagePrimitive.class)
public class Booleans
{
    public static boolean isFalse(final String string)
    {
        return "false".equalsIgnoreCase(string) || "f".equalsIgnoreCase(string) || "no".equalsIgnoreCase(string)
                || "disabled".equalsIgnoreCase(string);
    }

    public static boolean isTrue(final String string)
    {
        return "true".equalsIgnoreCase(string) || "t".equalsIgnoreCase(string) || "yes".equalsIgnoreCase(string)
                || "enabled".equalsIgnoreCase(string);
    }
}

package com.telenav.kivakit.core.language.reflection.property;

import com.telenav.kivakit.core.internal.lexakai.DiagramReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * The naming convention for getters and setters
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramReflection.class)
public enum PropertyNamingConvention
{
    /** The x() and x(Value) naming convention used in KivaKit */
    KIVAKIT,

    /** The getX() and setX(Value) naming convention that is used in Java Beans */
    JAVA_BEANS,

    /** Both KivaKit and Java Beans naming conventions are acceptable */
    KIVAKIT_AND_JAVA_BEANS
}

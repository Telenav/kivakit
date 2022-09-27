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
    KIVAKIT_PROPERTY_NAMING,

    /** The getX() and setX(Value) naming convention that is used in Java Beans */
    JAVA_BEANS_NAMING,

    /** Both KivaKit and Java Beans naming conventions are acceptable */
    ANY_NAMING_CONVENTION
}

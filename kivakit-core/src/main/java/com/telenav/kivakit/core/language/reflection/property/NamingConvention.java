package com.telenav.kivakit.core.language.reflection.property;

import com.telenav.kivakit.core.project.lexakai.DiagramReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * The naming convention for getters and setters
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramReflection.class)
public enum NamingConvention
{
    /**
     * The getX() and setX(Value) naming convention that is used in Java Beans
     */
    JAVA_BEANS,

    /**
     * The x() and x(Value) naming convention used in KivaKit
     */
    KIVAKIT
}

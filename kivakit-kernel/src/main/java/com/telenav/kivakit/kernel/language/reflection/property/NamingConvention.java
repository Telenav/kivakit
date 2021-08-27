package com.telenav.kivakit.kernel.language.reflection.property;

import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * The naming convention for getters and setters
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageReflection.class)
public enum NamingConvention
{
    /**
     * The getX() and setX(Value value) naming convention that is used in Java Beans
     */
    JAVA_BEANS,

    /**
     * The x() and x(Value value) naming convention used in KivaKit
     */
    KIVAKIT
}

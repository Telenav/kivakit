package com.telenav.kivakit.core.language.reflection.property;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_ENUM_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * The naming convention for getters and setters
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramReflection.class)
@ApiQuality(stability = STABLE_ENUM_EXPANDABLE,
            testing = TESTING_NOT_NEEDED,
            documentation = FULLY_DOCUMENTED)
public enum PropertyNamingConvention
{
    /** The x() and x(Value) naming convention used in KivaKit */
    KIVAKIT_PROPERTY_NAMING,

    /** The getX() and setX(Value) naming convention that is used in Java Beans */
    JAVA_BEANS_NAMING,

    /** Both KivaKit and Java Beans naming conventions are acceptable */
    ANY_NAMING_CONVENTION
}

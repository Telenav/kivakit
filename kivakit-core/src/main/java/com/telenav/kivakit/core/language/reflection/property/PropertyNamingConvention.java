package com.telenav.kivakit.core.language.reflection.property;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * The naming convention for getters and setters
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramReflection.class)
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public enum PropertyNamingConvention
{
    /** The x() and x(Value) naming convention used in KivaKit */
    KIVAKIT_PROPERTY_NAMING,

    /** The getX() and setX(Value) naming convention that is used in Java Beans */
    JAVA_BEANS_NAMING,

    /** Both KivaKit and Java Beans naming conventions are acceptable */
    ANY_NAMING_CONVENTION
}

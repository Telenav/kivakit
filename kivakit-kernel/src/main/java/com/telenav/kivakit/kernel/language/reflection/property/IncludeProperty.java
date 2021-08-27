package com.telenav.kivakit.kernel.language.reflection.property;

import com.telenav.kivakit.kernel.language.reflection.populator.KivaKitPropertyConverter;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Kinds of property methods and fields to include
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageReflection.class)
public enum IncludeProperty
{
    /** Include properties with public getters and setters */
    PUBLIC_METHODS,

    /** Include properties with non-public getters and setters */
    NON_PUBLIC_METHODS,

    /** Include fields and methods marked with {@link KivaKitIncludeProperty} */
    INCLUDED_FIELDS_AND_METHODS,

    /** Include fields marked with {@link KivaKitIncludeProperty} */
    INCLUDED_FIELDS,

    /** Include fields marked with {@link KivaKitPropertyConverter} */
    CONVERTED_FIELDS_AND_METHODS
}

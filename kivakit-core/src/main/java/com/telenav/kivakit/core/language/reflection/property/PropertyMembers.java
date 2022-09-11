package com.telenav.kivakit.core.language.reflection.property;

import com.telenav.kivakit.core.internal.lexakai.DiagramReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Kinds of property methods and fields
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramReflection.class)
public enum PropertyMembers
{
    /** Include properties with public getters and setters */
    PUBLIC_METHODS,

    /** Include properties with non-public getters and setters */
    NON_PUBLIC_METHODS,

    /** Include fields and methods marked with {@link KivaKitIncludeProperty} */
    INCLUDED_FIELDS_AND_METHODS,

    /** Include fields marked with {@link KivaKitIncludeProperty} */
    INCLUDED_FIELDS,

    /** Include fields marked with @KivaKitPropertyConverter */
    CONVERTED_FIELDS_AND_METHODS
}

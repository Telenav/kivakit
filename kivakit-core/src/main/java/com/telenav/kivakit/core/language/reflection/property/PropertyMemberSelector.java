package com.telenav.kivakit.core.language.reflection.property;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Different kinds of property methods and fields.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramReflection.class)
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public enum PropertyMemberSelector
{
    /** Include all fields and methods as properties */
    ALL_FIELDS_AND_METHODS,

    /** Include properties with public getters and setters */
    PUBLIC_METHODS,

    /** Include properties with non-public getters and setters */
    NON_PUBLIC_METHODS,

    /** Include fields and methods marked with {@link KivaKitIncludeProperty} */
    KIVAKIT_ANNOTATION_INCLUDED_FIELDS_AND_METHODS,

    /** Include fields marked with {@link KivaKitIncludeProperty} */
    KIVAKIT_ANNOTATION_INCLUDED_FIELDS,

    /** Include fields marked with @KivaKitPropertyConverter */
    KIVAKIT_CONVERTED_FIELDS_AND_METHODS
}

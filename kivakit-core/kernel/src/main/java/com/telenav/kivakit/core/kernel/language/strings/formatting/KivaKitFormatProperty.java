////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.strings.formatting;

import com.telenav.kivakit.core.kernel.language.strings.conversion.AsString;
import com.telenav.kivakit.core.kernel.language.strings.conversion.StringFormat;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageString;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
@UmlClassDiagram(diagram = DiagramLanguageString.class)
public @interface KivaKitFormatProperty
{
    /**
     * The format to use for the annotated property. If "toString()" is specified then the {@link #toString()} method is
     * called, otherwise, the format value is used to convert the object to a string using {@link
     * AsString#asString(StringFormat)}, where the purpose is case insensitive.
     */
    String format() default "ALL";
}

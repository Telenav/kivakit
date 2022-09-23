////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.language.object;

import com.telenav.kivakit.core.internal.lexakai.DiagramString;
import com.telenav.kivakit.interfaces.string.StringFormattable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.telenav.kivakit.interfaces.string.StringFormattable.Format.TEXT;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
@UmlClassDiagram(diagram = DiagramString.class)
public @interface KivaKitFormatProperty
{
    /**
     * The format to use for the annotated property. If "toString()" is specified then the {@link #toString()} method is
     * called, otherwise, the format value is used to convert the object to a string using {@link
     * StringFormattable#asString(StringFormattable.Format)}, where the purpose is case-insensitive.
     */
    StringFormattable.Format format() default TEXT;
}

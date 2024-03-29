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

package com.telenav.kivakit.conversion.core.language.object;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.Converter;
import com.telenav.kivakit.conversion.StringConverter;
import com.telenav.kivakit.conversion.core.language.IdentityConverter;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversion;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies the KivaKit {@link Converter} to use when populating the annotated field or method. For example:
 *
 * <pre>
 * {@literal @}ConvertedProperty(DurationConverter.class)
 *  Duration timeToLaunch;</pre>
 *
 * @author jonathanl (shibo)
 */
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
@UmlClassDiagram(diagram = DiagramConversion.class)
@TypeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public @interface ConvertedProperty
{
    /**
     * Returns the converter class to apply to this field or method
     */
    Class<? extends StringConverter> converter() default IdentityConverter.class;

    /**
     * Returns true when a property is optional
     */
    boolean optional() default false;

    /**
     * Returns the converter class to apply to this field or method
     */
    Class<? extends StringConverter> value() default IdentityConverter.class;
}

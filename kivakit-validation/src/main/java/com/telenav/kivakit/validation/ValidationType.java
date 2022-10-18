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

package com.telenav.kivakit.validation;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.value.name.Name;
import com.telenav.kivakit.validation.internal.lexakai.DiagramValidation;
import com.telenav.kivakit.validation.types.ValidateAll;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.HashSet;
import java.util.Set;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A kind of validation as restricted to a particular set of {@link Validatable} types.
 *
 * <p>
 * {@link ValidationType#validateAll()} specifies that values of all types should be validated. Calling
 * {@link #include(Class)} can specify one or more specific types of values to validate. An object which is
 * {@link Validatable} can then implement {@link Validatable#validator(ValidationType)} by returning different
 * {@link Validator} implementations depending on the type of {@link ValidationType} requested or by doing validation
 * conditionally depending on what {@link #shouldValidate(Class)} returns for one or more different target types.
 * </p>
 *
 * <p><b>Factory Methods</b></p>
 *
 * <ul>
 *     <li>{@link #validateAll()}</li>
 * </ul>
 *
 * <p><b>Filtering {@link Validatable}s</b></p>
 *
 * <ul>
 *     <li>{@link #include(Class)}</li>
 *     <li>{@link #exclude(Class)}</li>
 *     <li>{@link #shouldValidate(Class)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see Validatable
 * @see Validator
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramValidation.class)
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class ValidationType extends Name
{
    /**
     * Full validation of all relevant values
     */
    public static ValidationType validateAll()
    {
        return new ValidateAll();
    }

    /** What kinds of validate-ables to validate under this kind of validation */
    private final Set<Class<? extends Validatable>> toValidate = new HashSet<>();

    public ValidationType()
    {
    }

    /** Skip validation of the given type of value */
    public <T extends Validatable> ValidationType exclude(Class<T> type)
    {
        toValidate.remove(type);
        return this;
    }

    /** Ensure the given type of value */
    public <T extends Validatable> ValidationType include(Class<T> type)
    {
        toValidate.add(type);
        return this;
    }

    /**
     * Returns true if this validation should validate the given type
     */
    public <T extends Validatable> boolean shouldValidate(Class<T> type)
    {
        return toValidate.contains(type);
    }
}

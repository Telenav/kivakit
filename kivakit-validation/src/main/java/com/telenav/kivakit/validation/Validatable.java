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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.validation.internal.lexakai.DiagramValidation;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.validation.ValidationType.validateAll;

/**
 * An object that is {@link Validatable} can create a {@link Validator} for a given type of validation, represented by
 * {@link ValidationType}.
 *
 * <p><b>Validation</b></p>
 *
 * <ul>
 *     <li>{@link #isValid(Listener)} - Validates this object, broadcasting any problems to the given listener</li>
 * </ul>
 *
 * <p><b>Validators</b></p>
 *
 * <ul>
 *     <li>{@link #validator()} - The default Validator which validates all values ({@link ValidationType#validateAll()})</li>
 *     <li>{@link #validator(ValidationType)} - Validator for the given type of validation</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see Validator
 * @see ValidationType
 */
@UmlClassDiagram(diagram = DiagramValidation.class)
@UmlRelation(label = "how to validate", referent = ValidationType.class)
@UmlRelation(label = "provides", referent = Validator.class)
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public interface Validatable
{
    /**
     * Determines if this object is valid by using the default validator
     *
     * @see Validator#validate(Listener)
     */
    default boolean isValid(Listener listener)
    {
        return validator().validate(listener);
    }

    /**
     * Gets the {@link Validator} for this validatable that validates all values.
     *
     * @return A validator for full validation, if any. Although it cannot be final, this method should not be
     * overridden. Instead, override {@link #validator(ValidationType)}
     */
    default Validator validator()
    {
        return validator(validateAll());
    }

    /**
     * Produces a validator of the given type to validate this object
     *
     * @param type The type of validation to perform
     * @return A {@link Validator} instance
     */
    Validator validator(ValidationType type);
}

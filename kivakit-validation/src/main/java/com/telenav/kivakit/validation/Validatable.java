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

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.validation.project.lexakai.DiagramValidation;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

/**
 * An object that is {@link Validatable} can create a {@link Validator} for a given kind of {@link ValidationType}.
 *
 * @author jonathanl (shibo)
 * @see Validator
 * @see ValidationType
 */
@SuppressWarnings("SpellCheckingInspection")
@UmlClassDiagram(diagram = DiagramValidation.class)
@UmlRelation(label = "how to validate", referent = ValidationType.class)
@UmlRelation(label = "provides", referent = Validator.class)
public interface Validatable
{
    /**
     * Determines if this object is valid by using the default validator
     *
     * @see Validator#validate()
     */
    default boolean isValid()
    {
        return validator().validate();
    }

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
     * Gets the {@link Validator} for this validatable.
     *
     * @return A validator for full validation, if any. Although it cannot be final, this method should not be
     * overridden. Instead, override {@link #validator(ValidationType)}
     */
    default Validator validator()
    {
        return validator(ValidationType.VALIDATE_ALL);
    }

    /**
     * Produces a validator of the given type to validate this object
     *
     * @param type The type of validation to perform
     * @return A {@link Validator} instance
     */
    Validator validator(ValidationType type);
}

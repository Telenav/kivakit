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

package com.telenav.kivakit.kernel.data.validation;

import com.telenav.kivakit.kernel.language.values.name.Name;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramDataValidation;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.HashSet;
import java.util.Set;

/**
 * A kind of validation as restricted to a particular set of value types.
 *
 * <p>
 * {@link #VALIDATE_ALL} specifies that values of all types should be validated. Calling {@link #include(Class)} can
 * specify one or more specific types of values to validate. An object which is {@link Validatable} can then implement
 * {@link Validatable#validator(ValidationType)} by returning different {@link Validator} implementations depending on
 * the type of {@link ValidationType} requested or by doing validation conditionally depending on what {@link
 * #shouldValidate(Class)} returns for one or more different target types.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see Validatable
 * @see Validator
 */
@UmlClassDiagram(diagram = DiagramDataValidation.class)
public class ValidationType extends Name
{
    /**
     * Full validation of all relevant values
     */
    public static final ValidationType VALIDATE_ALL = new ValidationType("ALL")
    {
        @Override
        public boolean shouldValidate(Class<?> type)
        {
            return true;
        }
    };

    /** What kinds of values to validate under this kind of validation */
    private final Set<Class<?>> toValidate = new HashSet<>();

    /**
     * @param name A user-friendly name for this kind of validation
     */
    public ValidationType(String name)
    {
        super(name);
    }

    /** Skip validation of the given type of value */
    public ValidationType exclude(Class<?> type)
    {
        toValidate.remove(type);
        return this;
    }

    /** Ensure the given type of value */
    public ValidationType include(Class<?> type)
    {
        toValidate.add(type);
        return this;
    }

    /**
     * @return True if this validation should validate the given type
     */
    public boolean shouldValidate(Class<?> type)
    {
        return toValidate.contains(type);
    }
}

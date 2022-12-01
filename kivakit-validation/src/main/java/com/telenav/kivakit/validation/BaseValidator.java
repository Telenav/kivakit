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
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.messages.lifecycle.OperationHalted;
import com.telenav.kivakit.core.messaging.messages.status.Glitch;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.messages.status.Quibble;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.kivakit.core.string.Formatter;
import com.telenav.kivakit.core.thread.ReentrancyTracker;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.validation.internal.lexakai.DiagramValidation;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Collection;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.language.primitive.Ints.intIsBetweenExclusive;
import static com.telenav.kivakit.core.language.primitive.Ints.intIsBetweenInclusive;
import static com.telenav.kivakit.core.string.Strings.isNullOrBlank;
import static com.telenav.kivakit.core.thread.ReentrancyTracker.Reentrancy.ENTERED;
import static com.telenav.kivakit.core.thread.ReentrancyTracker.Reentrancy.REENTERED;
import static com.telenav.kivakit.core.value.name.Name.nameOf;

/**
 * A base implementation of {@link Validator} that provides convenience methods for reporting validation issues:
 * <p>
 * <b>Validation Issue Reporting</b>
 * <ul>
 *     <li>{@link #halt(String, Object...)} - Reports a fatal validation failure, halting further progress</li>
 *     <li>{@link #problem(String, Object...)} - Reports a non-fatal validation problem, causing data to be discarded but the process does not halt</li>
 *     <li>{@link #glitch(String, Object...)} - Reports a non-fatal and not very important validation problem where data is compromised but not discarded</li>
 *     <li>{@link #warning(String, Object...)} - Reports an issue that should ideally be corrected but is not fatal or a validation failure</li>
 *     <li>{@link #problemIf(boolean, String, Object...)} - Conditionally reports a problem</li>
 *     <li>{@link #glitchIf(boolean, String, Object...)} - Conditionally reports a glitch</li>
 *     <li>{@link #warningIf(boolean, String, Object...)} - Conditionally reports a warning</li>
 * </ul>
 * The base validator also assists in running other validators ({@link #validate(Validator)}, such as validators
 * from {@link Validatable} parent classes. The problem and warning methods make use of the variable argument
 * formatting as defined by {@link Formatter} in {@link Problem} and {@link Warning}. To use this class,
 * you need only implement {@link #onValidate()}.
 * <p>
 * <b>Example Validator</b>
 * <p>
 * This is roughly the validation method for Place:
 * <pre>
 * public Validator validator( ValidationType type)
 * {
 *     return new BaseValidator()
 *     {
 *         protected void onValidate()
 *         {
 *             // Ensure the superclass
 *             validate(Place.super.validator(type));
 *
 *             // then check for other problems
 *             problemIf(location() == null, "Location is missing");
 *             problemIf(isEmpty(name()), "Name is empty");
 *             problemIf(type() == null, "TimeFormat is missing");
 *             problemIf(isZero(population()), "Population is missing or zero");
 *         }
 *     };
 * }
 * </pre>
 * <b>Validation Results</b>
 * <p>
 * Once validation has occurred, the method {@link #isInvalid()} can be called to determine if any problems or glitches
 * (which are both are considered invalidating while warnings are not) were broadcast during the (possibly nested)
 * validation process. The state of {@link #isInvalid()} is used as the return value of {@link Validator#validate}
 * when the validator is used.
 * <p>
 * Subclasses of {@link BaseValidator} may be used to decorate problem reports with additional details by overriding
 * the {@link #problem(String, Object...)} and {@link #warning(String, Object...)} methods.
 * <p>
 * <b>Implementation Note</b>
 * <p>
 * Validation is a recursive, re-entrant process because validators can call other validators. One possible way to
 * propagate the status of validation would be to have a call chain of validators, each returning its status to
 * the prior level. This method results in a lot of boilerplate code creating and checking status objects. A
 * cleaner option, used here, is to keep thread-local statistics during validation and manage nested re-entry with
 * {@link ReentrancyTracker}.
 * <p>
 * Using a reentrancy tracker, thread-local statistics are reset only on the first entrance of a thread to perform
 * a validation operation. This allows statistics to accumulate during validation so that the final exit can
 * inspect the cumulative results of all sub-validations to see if the whole operation has ended in failure or not.
 * <p>
 * Status-by-step comments that detail this approach are {@link #validate(Listener)} below.
 *
 * <p><b>Reporting Issues</b></p>
 *
 * <ul>
 *     <li>{@link #addGlitch(String, Object...)}</li>
 *     <li>{@link #addProblem(String, Object...)}</li>
 *     <li>{@link #addQuibble(String, Object...)}</li>
 *     <li>{@link #addWarning(String, Object...)}</li>
 *     <li>{@link #glitch(String, Object...)}</li>
 *     <li>{@link #glitchIf(boolean, String, Object...)}</li>
 *     <li>{@link #halt(String, Object...)}</li>
 *     <li>{@link #problem(String, Object...)}</li>
 *     <li>{@link #problemIf(boolean, String, Object...)}</li>
 *     <li>{@link #problemIfNotInRangeExclusive(int, String, int, int)}</li>
 *     <li>{@link #problemIfNotInRangeInclusive(int, String, int, int)}</li>
 *     <li>{@link #problemIfNull(Object, String, Object...)}</li>
 *     <li>{@link #quibble(String, Object...)}</li>
 *     <li>{@link #quibbleIf(boolean, String, Object...)}</li>
 *     <li>{@link #warning(String, Object...)}</li>
 *     <li>{@link #warningIf(boolean, String, Object...)}</li>
 * </ul>
 *
 * <p><b>Validation</b></p>
 *
 * <ul>
 *     <li>{@link #isInvalid()}</li>
 *     <li>{@link #isValid()}</li>
 *     <li>{@link #issues()}</li>
 *     <li>{@link #onValidate()}</li>
 *     <li>{@link #shouldShowValidationReport()}</li>
 *     <li>{@link #validate(Listener)}</li>
 *     <li>{@link #validate(Validator)}</li>
 *     <li>{@link #validate(Validatable, ValidationType)}</li>
 *     <li>{@link #validationTarget()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see Validator
 * @see Problem
 * @see Glitch
 * @see Warning
 * @see ReentrancyTracker
 * @see ThreadLocal
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
@UmlClassDiagram(diagram = DiagramValidation.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public abstract class BaseValidator implements Validator
{
    /** Track if we are validating within a validation */
    private static final ReentrancyTracker reentrancy = new ReentrancyTracker();

    /** Per-thread validation statistics */
    private static final ThreadLocal<ValidationIssues> threadLocalIssues = ThreadLocal.withInitial(ValidationIssues::new);

    /** The listener while validation is going on */
    private Listener listener;

    /**
     * Any parent validator for chaining
     */
    private Validator parent;

    protected BaseValidator()
    {
    }

    protected BaseValidator(Validator parent)
    {
        this.parent = parent;
    }

    /**
     * Returns true if the validation in progress is invalid
     */
    public boolean isInvalid()
    {
        return !issues().isValid();
    }

    /**
     * Returns true if this validator is valid
     */
    public boolean isValid()
    {
        return !isInvalid();
    }

    /**
     * Returns the validation issues for the validation that occurred on this thread
     */
    public ValidationIssues issues()
    {
        return threadLocalIssues.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean validate(Listener listener)
    {
        try
        {
            // Enter this method,
            var reentrancy = BaseValidator.reentrancy.enter();

            // and if we are entering for the first time,
            if (reentrancy == ENTERED)
            {
                // clear the issues for this thread.
                issues().clear();
            }

            // Save the listener parameter for reporting issues during validation.
            this.listener = listener;

            // Make a copy of the thread's issues before validating,
            var issuesBeforeValidation = issues().copy();

            // then validate any parents,
            validateParent();

            // and call the subclass onValidate() method (which may make calls to problem or glitch methods, causing invalidity).
            onValidate();

            // Count the number of problems, glitches and warnings that occurred during validation.
            var problems = issues().count(Problem.class);
            var glitches = issues().count(Glitch.class);
            var warnings = issues().count(Warning.class);

            // If we have not reentered the method, we're done validating,
            if (reentrancy != REENTERED)
            {
                // so we reset the listener for cleanliness,
                this.listener = null;

                // and if a validation report is desired,
                if (shouldShowValidationReport())
                {
                    // we broadcast a short summary of the validation results.
                    listener.information("Validated $ ($ problems, $ glitches, $ warnings)",
                            nameOf(validationTarget()), problems, glitches, warnings);
                }
            }

            // Finally, we're valid at this level of reentrancy if validation didn't change the number of problems or glitches.
            return issuesBeforeValidation.count(Problem.class).equals(problems) &&
                    issuesBeforeValidation.count(Glitch.class).equals(glitches);
        }
        finally
        {
            // Exit one level of reentrancy.
            reentrancy.exit();
        }
    }

    /**
     * Records a {@link Glitch} with the given message without broadcasting it
     */
    protected Glitch addGlitch(String message, Object... parameters)
    {
        return addIssueIfNotNull(new Glitch(message, parameters));
    }

    /**
     * Records a {@link Problem} with the given message without broadcasting it
     */
    protected Problem addProblem(String message, Object... parameters)
    {
        return addIssueIfNotNull(new Problem(message, parameters));
    }

    /**
     * Records a {@link Quibble} with the given message without broadcasting it
     */
    protected Quibble addQuibble(String message, Object... parameters)
    {
        return addIssueIfNotNull(new Quibble(message, parameters));
    }

    /**
     * Records a {@link Warning} with the given message without broadcasting it
     */
    protected Warning addWarning(String message, Object... parameters)
    {
        return addIssueIfNotNull(new Warning(message, parameters));
    }

    /**
     * Broadcasts a {@link Glitch} with the given message
     */
    protected Glitch glitch(String message, Object... parameters)
    {
        return addIssueIfNotNull(listener.glitch(message, parameters));
    }

    /**
     * Broadcasts a {@link Glitch} with the given message if the invalid parameter is true
     */
    protected final Glitch glitchIf(boolean invalid, String message, Object... parameters)
    {
        if (invalid)
        {
            return glitch(message, parameters);
        }
        return null;
    }

    /**
     * Broadcasts a {@link OperationHalted} with the given message
     */
    protected OperationHalted halt(String message, Object... parameters)
    {
        return listener.halted(message, parameters);
    }

    /**
     * Convenience method for checking collections
     *
     * @return True if the collection is null or empty
     */
    protected final boolean isEmpty(Collection<?> collection)
    {
        return collection == null || collection.isEmpty();
    }

    /**
     * Convenience method for checking strings
     *
     * @return True if the value is null or empty
     */
    protected final boolean isEmpty(String value)
    {
        return isNullOrBlank(value);
    }

    /**
     * Convenience method for checking counts
     *
     * @return True if the count is null or empty
     */
    protected final boolean isZero(Count count)
    {
        return count == null || count.isZero();
    }

    /**
     * This method is implemented by the subclass. It may call {@link #validate(Validator)} on its superclass, and it
     * must call {@link #problemIf(boolean, String, Object...)}, {@link #warningIf(boolean, String, Object...)},
     * {@link #problem(String, Object...)} or {@link #warning(String, Object...)} if there are any validation issues to
     * report. If the problem methods are not called then the validation is valid.
     */
    protected abstract void onValidate();

    /**
     * Broadcasts a {@link Problem} with the given message
     */
    protected Problem problem(String message, Object... parameters)
    {
        return addIssueIfNotNull(listener.problem(message, parameters));
    }

    /**
     * Broadcasts a {@link Problem} with the given message if the invalid parameter is true
     *
     * @return True if there was a problem
     */
    protected final Problem problemIf(boolean invalid, String message, Object... parameters)
    {
        if (invalid)
        {
            return problem(message, parameters);
        }
        return null;
    }

    /**
     * Broadcasts a {@link Problem} if the given value is not in range
     */
    protected void problemIfNotInRangeExclusive(int value, String name, int minimum, int maximum)
    {
        problemIf(!intIsBetweenExclusive(value, minimum, maximum), "Invalid " + name);
    }

    /**
     * Broadcasts a {@link Problem} if the given value is not in range
     */
    protected void problemIfNotInRangeInclusive(int value, String name, int minimum, int maximum)
    {
        problemIf(!intIsBetweenInclusive(value, minimum, maximum), "Invalid " + name);
    }

    /**
     * Broadcasts a {@link Problem} if the given object is null
     */
    protected final Problem problemIfNull(Object object, String message, Object... parameters)
    {
        return problemIf(object == null, message, parameters);
    }

    /**
     * Broadcasts a {@link Quibble} with the given message
     */
    protected Quibble quibble(String message, Object... parameters)
    {
        return addIssueIfNotNull(listener.quibble(message, parameters));
    }

    /**
     * Broadcasts a {@link Quibble} with the given message if the invalid parameter is true
     */
    protected final Quibble quibbleIf(boolean invalid, String message, Object... parameters)
    {
        if (invalid)
        {
            return quibble(message, parameters);
        }
        return null;
    }

    /**
     * Returns true if this validator should report results when it completes
     */
    protected boolean shouldShowValidationReport()
    {
        return false;
    }

    /**
     * Re-enters validation by calling the given validator. Calling the validator's {@link #validate(Listener)} method
     * directly may result in an inconsistent state.
     */
    protected final boolean validate(Validator validator)
    {
        reentrancy.enter();
        try
        {
            if (validator != null)
            {
                return validator.validate(listener);
            }
            else
            {
                problem("Null validator");
                return false;
            }
        }
        finally
        {
            reentrancy.exit();
        }
    }

    /**
     * Validates the given {@link Validatable} using the given type of validation
     *
     * @param validatable The object to validate
     * @param type The type of validation to apply
     */
    protected boolean validate(Validatable validatable, ValidationType type)
    {
        if (validatable != null)
        {
            return validate(validatable.validator(type));
        }
        else
        {
            problem("Null validatable");
            return false;
        }
    }

    /**
     * Returns the name of what was validated when reporting validation results
     */
    protected String validationTarget()
    {
        return "object";
    }

    /**
     * Broadcasts a {@link Warning} with the given message
     */
    protected Warning warning(String message, Object... parameters)
    {
        return addIssueIfNotNull(listener.warning(message, parameters));
    }

    /**
     * Broadcasts a {@link Warning} with the given message if the invalid parameter is true
     */
    protected Warning warningIf(boolean invalid, String message, Object... parameters)
    {
        if (invalid)
        {
            return warning(message, parameters);
        }
        return null;
    }

    private <T extends Message> T addIssueIfNotNull(T issue)
    {
        if (issue != null)
        {
            issues().add(issue);
        }
        return issue;
    }

    /**
     * Validates any parent {@link Validatable} passed to the constructor
     */
    private void validateParent()
    {
        if (parent != null)
        {
            parent.validate(listener);
        }
    }
}

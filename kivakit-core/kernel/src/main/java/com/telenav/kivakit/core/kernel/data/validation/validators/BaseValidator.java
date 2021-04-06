////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.data.validation.validators;

import com.telenav.kivakit.core.kernel.data.validation.Validatable;
import com.telenav.kivakit.core.kernel.data.validation.Validator;
import com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.core.kernel.messaging.messages.MessageFormatter;
import com.telenav.kivakit.core.kernel.messaging.messages.lifecycle.OperationHalted;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Quibble;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Warning;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.strings.Strings;
import com.telenav.kivakit.core.kernel.language.threading.status.ReentrancyTracker;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataValidation;

import java.util.Collection;

/**
 * A base implementation of {@link Validator} that provides convenience methods for reporting validation issues:
 * <p>
 * <b>Validation Issue Reporting</b>
 * <ul>
 *     <li>{@link #fatal(String, Object...)} - Reports a fatal validation failure, halting further progress via {@link Ensure#fail(String, Object...)}</li>
 *     <li>{@link #problem(String, Object...)} - Reports a non-fatal validation problem, causing data to be discarded but the process does not halt</li>
 *     <li>{@link #quibble(String, Object...)} - Reports a non-fatal and not very important validation problem where data is compromised but not discarded</li>
 *     <li>{@link #warning(String, Object...)} - Reports an issue that should ideally be corrected but is not fatal or a validation failure</li>
 *     <li>{@link #fatalIf(boolean, String, Object...)} - Conditionally reports a fatal problem</li>
 *     <li>{@link #problemIf(boolean, String, Object...)} - Conditionally reports a problem</li>
 *     <li>{@link #quibbleIf(boolean, String, Object...)} - Conditionally reports a quibble</li>
 *     <li>{@link #warningIf(boolean, String, Object...)} - Conditionally reports a warning</li>
 * </ul>
 * The base validator also assists in running other validators ({@link #validate(Validator)}, such as validators
 * from {@link Validatable} parent classes. The problem and warning methods make use of the variable argument
 * formatting as defined by {@link MessageFormatter} in {@link Problem} and {@link Warning}. To use this class,
 * you need only implement {@link #onValidate()}.
 * <p>
 * <b>Example Validator</b>
 * <p>
 * This is roughly the validation method for Place:
 * <pre>
 * public Validator validator(final ValidationType type)
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
 * Once validation has occurred, the method {@link #isInvalid()} can be called to determine if any problems or quibbles
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
 * the prior level. This method results in a lot of boiler plate code creating and checking status objects. A
 * cleaner option, used here, is to keep thread-local statistics during validation and manage nested re-entry with
 * {@link ReentrancyTracker}.
 * <p>
 * Using a reentrancy tracker, thread-local statistics are reset only on the first entrance of a thread to perform
 * a validation operation. This allows statistics to accumulate during validation so that the final exit can
 * inspect the cumulative results of all sub-validations to see if the whole operation has ended in failure or not.
 * <p>
 * Status-by-step comments that detail this approach are {@link #validate(Listener)} below.
 *
 * @author jonathanl (shibo)
 * @see Ensure
 * @see Validator
 * @see Problem
 * @see Quibble
 * @see Warning
 * @see ReentrancyTracker
 * @see ThreadLocal
 */
@UmlClassDiagram(diagram = DiagramDataValidation.class)
public abstract class BaseValidator implements Validator
{
    /** Track if we are validating within a validation */
    private static final ReentrancyTracker reentrancy = new ReentrancyTracker();

    /** Per-thread validation statistics */
    private static final ThreadLocal<Statistics> statistics = ThreadLocal.withInitial(Statistics::new);

    /**
     * Statistics kept during a (possibly nested) validation operation
     */
    private static class Statistics
    {
        /** The number of problems encountered during validation */
        private int problems;

        /** The number of quibbles encountered during validation */
        private int quibbles;

        /** The number of warnings encountered during validation */
        private int warnings;

        public Statistics()
        {
        }

        public Statistics(final Statistics that)
        {
            this(that.problems, that.quibbles, that.warnings);
        }

        public Statistics(final int problems, final int quibbles, final int warnings)
        {
            this.problems = problems;
            this.quibbles = quibbles;
            this.warnings = warnings;
        }

        public void initialize()
        {
            problems = 0;
            quibbles = 0;
            warnings = 0;
        }

        /**
         * @return True if no problems or quibbles have been encountered during validation
         */
        private boolean isValid()
        {
            return (quibbles + problems) == 0;
        }
    }

    /** The listener while validation is going on */
    private Listener listener;

    /**
     * @return True if the validation in progress is invalid
     */
    public boolean isInvalid()
    {
        return !statistics().isValid();
    }

    /**
     * @return True if this validator is valid
     */
    public boolean isValid()
    {
        return !isInvalid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean validate(final Listener listener)
    {
        final var start = Time.now();

        // Save the given listener for calls to problem and warning methods,
        this.listener = listener;

        // determine if we have re-entered this method on this thread
        final var reentered = reentrancy.hasReentered();

        // and we HAVE NOT reentered due to a sub-validation, we're just starting to validate, so we
        if (!reentered)
        {
            // initialize the statistics for the thread.
            statistics().initialize();
        }

        // Make a copy of the current statistics for the thread.
        final var statistics = new Statistics(statistics());

        // Next, call the subclass onValidate() method (which may make calls to problem or quibble methods, causing invalidity)
        onValidate();

        // and if we haven't re-entered, we're done with the top-level validation,
        if (!reentered)
        {
            // so we reset the listener for cleanliness,
            this.listener = null;

            // and if a validation report is desired,
            if (validationReport())
            {
                // we output a short summary of the validation results
                listener.information("Validated $ in $ ($ problems, $ quibbles, $ warnings)", validationTarget(),
                        start.elapsedSince(), statistics.problems, statistics.quibbles, statistics.warnings);
            }
        }

        // and finally, we're valid if the validation didn't change the number of problems or quibbles
        return statistics.problems == statistics().problems && statistics.quibbles == statistics().quibbles;
    }

    /**
     * Broadcasts a {@link OperationHalted} with the given message
     */
    protected void fatal(final String message, final Object... parameters)
    {
        listener.halt(message, parameters);
    }

    /**
     * Broadcasts a {@link OperationHalted} with the given message if the invalid parameter is true
     *
     * @return True if there was a problem
     */
    protected final boolean fatalIf(final boolean invalid, final String message, final Object... parameters)
    {
        if (invalid)
        {
            problem(message, parameters);
            return true;
        }
        return false;
    }

    /**
     * Convenience method for checking collections
     *
     * @return True if the collection is null or empty
     */
    protected final boolean isEmpty(final Collection<?> collection)
    {
        return collection == null || collection.isEmpty();
    }

    /**
     * Convenience method for checking strings
     *
     * @return True if the value is null or empty
     */
    protected final boolean isEmpty(final String value)
    {
        return Strings.isEmpty(value);
    }

    /**
     * Convenience method for checking counts
     *
     * @return True if the count is null or empty
     */
    protected final boolean isZero(final Count count)
    {
        return count == null || count.isZero();
    }

    /**
     * This method is implemented by the subclass. It may call {@link #validate(Validator)} on its superclass and it
     * must call {@link #problemIf(boolean, String, Object...)}, {@link #warningIf(boolean, String, Object...)}, {@link
     * #problem(String, Object...)} or {@link #warning(String, Object...)} if there are any validation issues to report.
     * If the problem methods are not called then the validation is valid.
     */
    protected abstract void onValidate();

    /**
     * Broadcasts a {@link Problem} with the given message
     */
    protected void problem(final String message, final Object... parameters)
    {
        problem();
        listener.problem(message, parameters);
    }

    /**
     * In some cases, a subclass may want to register a problem without actually calling a listener. For example, if a
     * total count of problems is desired, but output is not.
     */
    protected void problem()
    {
        statistics().problems++;
    }

    /**
     * Broadcasts a {@link Problem} with the given message if the invalid parameter is true
     *
     * @return True if there was a problem
     */
    protected final boolean problemIf(final boolean invalid, final String message, final Object... parameters)
    {
        if (invalid)
        {
            problem(message, parameters);
            return true;
        }
        return false;
    }

    /**
     * Broadcasts a {@link Quibble} with the given message
     */
    protected void quibble(final String message, final Object... parameters)
    {
        quibble();
        listener.quibble(message, parameters);
    }

    /**
     * In some cases, a subclass may want to register a quibble without actually calling a listener. For example, if a
     * total count of problems is desired, but output is not.
     */
    protected void quibble()
    {
        statistics().quibbles++;
    }

    /**
     * Broadcasts a {@link Quibble} with the given message if the invalid parameter is true
     *
     * @return True if there was a quibble
     */
    protected final boolean quibbleIf(final boolean invalid, final String message, final Object... parameters)
    {
        if (invalid)
        {
            quibble(message, parameters);
            return true;
        }
        return false;
    }

    /**
     * Re-enters validation by calling the given validator. Calling the validator's {@link #validate(Listener)} method
     * directly may result in an inconsistent state.
     */
    protected final void validate(final Validator validator)
    {
        reentrancy.enter();
        try
        {
            validator.validate(listener);
        }
        finally
        {
            reentrancy.exit();
        }
    }

    /**
     * @return True if this validator should report results when it completes
     */
    protected boolean validationReport()
    {
        return false;
    }

    /**
     * @return The name of what was validated when reporting validation results
     */
    protected String validationTarget()
    {
        return "object";
    }

    /**
     * Broadcasts a {@link Warning} with the given message
     */
    protected void warning(final String message, final Object... parameters)
    {
        listener.warning(message, parameters);
        statistics().warnings++;
    }

    /**
     * Broadcasts a {@link Warning} with the given message if the invalid parameter is true
     */
    protected void warningIf(final boolean invalid, final String message, final Object... parameters)
    {
        if (invalid)
        {
            warning(message, parameters);
        }
    }

    private static Statistics statistics()
    {
        return statistics.get();
    }
}

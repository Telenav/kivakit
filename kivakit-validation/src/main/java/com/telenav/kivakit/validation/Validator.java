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
import com.telenav.kivakit.core.messaging.Transceiver;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.kivakit.validation.internal.lexakai.DiagramValidation;
import com.telenav.kivakit.validation.validators.NullValidator;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * When {@link #validate(Listener)} is called, a {@link Validator} validates itself, using the listener parameter to
 * send messages like {@link Problem} s and {@link Warning}s as validation proceeds. If no failure {@link Message} is
 * sent the method returns true, allowing the caller to make success or failure decisions easily. The
 * {@link BaseValidator} class makes it easy to implement validators by hiding access to the listener and providing
 * methods that check conditions and broadcast messages.
 *
 * @author jonathanl (shibo)
 * @see BaseValidator
 * @see Listener
 * @see Transceiver
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramValidation.class)
@UmlRelation(label = "reports to", referent = Listener.class)
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public interface Validator
{
    /** A validator that does nothing */
    static Validator nullValidator()
    {
        return new NullValidator();
    }

    /**
     * Validates this object, transmitting issues to the given listener. The listener {@link ValidationIssues} is handy
     * for capturing validation messages.
     *
     * @param listener The listener to transmit problems and warnings to. Note that {@link Listener} has convenience
     * methods on it to make composing and sending the messages easier.
     * @return True if the object being validated is valid
     * @see Listener
     * @see Validatable
     * @see ValidationIssues
     */
    boolean validate(Listener listener);
}

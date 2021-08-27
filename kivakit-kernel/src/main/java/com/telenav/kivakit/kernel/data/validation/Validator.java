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

import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.Transceiver;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.kernel.messaging.messages.status.Warning;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramDataValidation;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

/**
 * When {@link #validate(Listener)} is called, a {@link Validator} validates itself, using the listener parameter to
 * send messages like {@link Problem} s and {@link Warning}s as validation proceeds. If no failure {@link Message} is
 * sent the method returns true, allowing the caller to make success or failure decisions easily. The {@link
 * BaseValidator} class makes it easy to implement validators by hiding access to the listener and providing methods
 * that check conditions and broadcast messages.
 *
 * @author jonathanl (shibo)
 * @see BaseValidator
 * @see Listener
 * @see Transceiver
 */
@UmlClassDiagram(diagram = DiagramDataValidation.class)
@UmlRelation(label = "reports to", referent = Listener.class)
public interface Validator
{
    /** A validator that does nothing */
    Validator NULL = new BaseValidator()
    {
        @Override
        protected void onValidate()
        {
        }
    };

    /**
     * @return True if this object is valid, but without returning any reason why it's not valid. To find out why the
     * object is not valid, call {@link #validate(Listener)}.
     */
    default boolean validate()
    {
        return validate(Listener.none());
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

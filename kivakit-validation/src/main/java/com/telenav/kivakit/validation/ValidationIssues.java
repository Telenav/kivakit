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
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.listeners.MessageCounter;
import com.telenav.kivakit.core.messaging.listeners.MessageList;
import com.telenav.kivakit.core.messaging.messages.status.Glitch;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.validation.internal.lexakai.DiagramValidation;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A {@link MessageList} and {@link MessageCounter} that captures validation issues. Any message with a
 * {@link Message.Status} worse than or equal to that of a {@link Glitch} causes {@link #isValid()} to return false. In
 * particular, capturing a {@link Glitch} or {@link Problem} makes this {@link ValidationIssues} object invalid.
 *
 * <p><b>Validity</b></p>
 *
 * <ul>
 *     <li>{@link #isValid()}</li>
 * </ul>
 *
 * <p><b>Counting Messages</b></p>
 *
 * <ul>
 *     <li>{@link #count(Message.Status)}</li>
 *     <li>{@link #count(Class)}</li>
 *     <li>{@link #countWorseThanOrEqualTo(Message.Status)}</li>
 *     <li>{@link #countWorseThanOrEqualTo(Class)}</li>
 * </ul>
 *
 * <p><b>Filtering</b></p>
 *
 * <ul>
 *     <li>{@link #matching(Matcher)}</li>
 *     <li>{@link #messagesOfType(Class)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see MessageList
 * @see Message
 */
@UmlClassDiagram(diagram = DiagramValidation.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class ValidationIssues extends MessageList
{
    public ValidationIssues()
    {
        // Keep anything that doesn't represent outright success
        super(message -> !message.status().succeeded());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValidationIssues copy()
    {
        return (ValidationIssues) super.copy();
    }

    /**
     * Returns true if no problems or warnings have been encountered during validation
     */
    public boolean isValid()
    {
        return countWorseThanOrEqualTo(new Glitch().status()).isZero();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageList onNewInstance()
    {
        return new ValidationIssues();
    }
}

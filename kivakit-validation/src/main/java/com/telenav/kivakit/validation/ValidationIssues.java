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

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.listeners.MessageCounter;
import com.telenav.kivakit.core.messaging.listeners.MessageList;
import com.telenav.kivakit.core.messaging.messages.status.Glitch;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.validation.internal.lexakai.DiagramValidation;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A {@link MessageList} and {@link MessageCounter} that captures validation issues. Any message with a
 * {@link Message.Status} worse than or equal to that of a {@link Glitch} causes {@link #isValid()} to return false. In
 * particular, capturing a {@link Glitch} or {@link Problem} makes this {@link ValidationIssues} object invalid.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramValidation.class)
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
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
     * @return True if no problems or warnings have been encountered during validation
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

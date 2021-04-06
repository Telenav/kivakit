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

package com.telenav.kivakit.core.test.reporters;

import com.telenav.kivakit.core.kernel.data.validation.BaseValidationReporter;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.test.project.lexakai.diagrams.DiagramTest;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.junit.Assert;

import static com.telenav.kivakit.core.kernel.messaging.messages.MessageFormatter.Format.WITHOUT_EXCEPTION;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramTest.class)
public class JUnitValidationReporter extends BaseValidationReporter
{
    @Override
    public void report(final Message message)
    {
        Assert.fail(message.formatted(WITHOUT_EXCEPTION));
    }
}

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

package com.telenav.kivakit.core.test;

import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.ensure.FailureReporter;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.lexakai.DiagramTest;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.junit.Assert;

import static com.telenav.kivakit.core.string.Formatter.Format.WITH_EXCEPTION;

/**
 * A {@link FailureReporter} that causes a JUnit test failure. This validation reporter is installed by {@link CoreUnitTest}
 * to ensure that validation failures by {@link Ensure} are reported through JUnit.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
@UmlClassDiagram(diagram = DiagramTest.class)
public class JUnitFailureReporter implements FailureReporter
{
    @Override
    public void report(Message message)
    {
        Assert.fail(message.formatted(WITH_EXCEPTION));
    }
}

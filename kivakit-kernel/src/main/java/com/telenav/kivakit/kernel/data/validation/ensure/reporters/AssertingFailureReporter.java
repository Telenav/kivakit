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

package com.telenav.kivakit.kernel.data.validation.ensure.reporters;

import com.telenav.kivakit.kernel.data.validation.ensure.BaseFailureReporter;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramDataFailureReporter;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

/**
 * A validation reporter that asserts false when it hears a message
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataFailureReporter.class)
@LexakaiJavadoc(complete = true)
public class AssertingFailureReporter extends BaseFailureReporter
{
    @Override
    @UmlExcludeMember
    public void report(final Message message)
    {
        assert false : message.description();
    }
}

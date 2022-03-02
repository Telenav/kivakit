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

package com.telenav.kivakit.core.logging.loggers;

import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.logging.Log;
import com.telenav.kivakit.core.logging.logs.text.ConsoleLog;
import com.telenav.kivakit.core.project.lexakai.DiagramLogging;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

/**
 * A simple logger for bootstrapping purposes (the logging system itself may need to do logging, for example, which can
 * lead to problems)
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLogging.class)
public class ConsoleLogger extends BaseLogger
{
    private final Log log = new ConsoleLog();

    @Override
    @UmlExcludeMember
    protected ObjectSet<Log> logs()
    {
        return ObjectSet.objectSet(log);
    }
}

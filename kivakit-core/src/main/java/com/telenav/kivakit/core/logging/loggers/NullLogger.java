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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.internal.lexakai.DiagramLogging;
import com.telenav.kivakit.core.logging.Log;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;
import static com.telenav.kivakit.core.collections.set.ObjectSet.objectSet;

/**
 * A logger that does nothing.
 * <p>
 * The following code can be used to shut off all logging:
 * </p>
 * <pre>
 * LogFactory.factory(NullLogger::new)</pre>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLogging.class)
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class NullLogger extends BaseLogger
{
    /**
     * Returns an empty set of logs
     * <p>
     * {@inheritDoc}
     * </p>
     */
    @Override
    @UmlExcludeMember
    protected ObjectSet<Log> logs()
    {
        return objectSet();
    }
}

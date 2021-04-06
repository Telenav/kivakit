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

package com.telenav.kivakit.core.kernel.data.validation.ensure;

import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataValidationEnsure;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A validation problem reported by {@link Ensure} failures.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataValidationEnsure.class)
public class ValidationProblem extends Problem
{
    public ValidationProblem(final String message, final Object... arguments)
    {
        super(message, arguments);
    }

    public ValidationProblem(final Throwable cause, final String message, final Object... arguments)
    {
        super(cause, message, arguments);
    }

    public ValidationProblem()
    {
    }
}

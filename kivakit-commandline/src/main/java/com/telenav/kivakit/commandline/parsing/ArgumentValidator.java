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

package com.telenav.kivakit.commandline.parsing;

import com.telenav.kivakit.commandline.ArgumentList;
import com.telenav.kivakit.commandline.project.lexakai.diagrams.DiagramValidation;
import com.telenav.kivakit.kernel.data.validation.validators.BaseValidator;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

/**
 * <b>Not Public API</b>
 * <p>
 * A simple argument list validator that checks argument quantity and optionality against usage.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramValidation.class)
@UmlNotPublicApi
public class ArgumentValidator extends BaseValidator
{
    /** The arguments to check */
    @UmlAggregation
    private final ArgumentList arguments;

    /** The argument parsers to check against */
    @UmlAggregation
    private final ArgumentParserList parsers;

    /**
     * @param parsers The argument parsers to check against
     * @param arguments The arguments to check
     */
    public ArgumentValidator(final ArgumentParserList parsers, final ArgumentList arguments)
    {
        this.parsers = parsers;
        this.arguments = arguments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onValidate()
    {
        var remaining = arguments.size();

        // Go through argument parsers
        for (final var parser : parsers)
        {
            // and check arguments based on the parser's quantifier.
            switch (parser.quantifier())
            {
                case REQUIRED:
                    if (remaining == 0)
                    {
                        problem("Required ${debug} argument \"$\" is missing", parser, parser.description());
                    }
                    remaining--;
                    break;

                case ONE_OR_MORE:
                    if (remaining < 1)
                    {
                        problem("Must supply one or more ${debug} arguments for \"$\"", parser, parser.description());
                    }
                    remaining--;
                    break;

                case TWO_OR_MORE:
                    if (remaining < 2)
                    {
                        problem("Must supply two or more ${debug} arguments for \"$\"", parser, parser.description());
                    }
                    remaining -= 2;
                    break;

                case OPTIONAL:
                case ZERO_OR_MORE:
                    remaining--;
                    break;
            }
        }

        if (remaining > 0)
        {
            if (parsers.isEmpty() || !parsers.last().isAllowedMultipleTimes())
            {
                problem("Too many arguments");
            }
        }
    }
}

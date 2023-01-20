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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.commandline.ArgumentValueList;
import com.telenav.kivakit.commandline.internal.lexakai.DiagramValidation;
import com.telenav.kivakit.validation.BaseValidator;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * <b>Not Public API</b>
 * <p>
 * A simple argument list validator that checks argument quantity and optionality against usage.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramValidation.class)
@UmlNotPublicApi
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED,
             audience = AUDIENCE_INTERNAL)
public class ArgumentValueListValidator extends BaseValidator
{
    /** The arguments to check */
    @UmlAggregation
    private final ArgumentValueList argumentValues;

    /** The argument parsers to check against */
    @UmlAggregation
    private final ArgumentParserList parsers;

    /**
     * @param parsers The argument parsers to check against
     * @param argumentValues The arguments to check
     */
    public ArgumentValueListValidator(ArgumentParserList parsers, ArgumentValueList argumentValues)
    {
        this.parsers = parsers;
        this.argumentValues = argumentValues;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onValidate()
    {
        var remaining = argumentValues.size();

        // Go through argument parsers
        for (var parser : parsers)
        {
            // and check arguments based on the parser's quantifier.
            switch (parser.quantifier())
            {
                case REQUIRED ->
                {
                    if (remaining == 0)
                    {
                        problem("Required ${debug} argument \"$\" is missing", parser, parser.description());
                    }
                    remaining--;
                }
                case ONE_OR_MORE ->
                {
                    if (remaining < 1)
                    {
                        problem("Must supply one or more ${debug} arguments for \"$\"", parser, parser.description());
                    }
                    remaining--;
                }
                case OPTIONAL, ZERO_OR_MORE -> remaining--;
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

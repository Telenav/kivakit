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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.commandline.SwitchValueList;
import com.telenav.kivakit.commandline.internal.lexakai.DiagramValidation;
import com.telenav.kivakit.validation.BaseValidator;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

import java.util.HashMap;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.ApiType.PRIVATE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * <b>Not Public API</b>
 * <p>
 * A switch list validator that validates switches and checks for required and duplicate switches.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("DuplicatedCode") @UmlClassDiagram(diagram = DiagramValidation.class)
@UmlNotPublicApi
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE,
            type = PRIVATE)
public class SwitchValueListValidator extends BaseValidator
{
    /** The list of switches to validate */
    @UmlAggregation
    private final SwitchValueList switchValues;

    /** The list of switch parsers to validate against */
    @UmlAggregation
    private final SwitchParserList parsers;

    /**
     * @param parsers The switch parsers to validate against
     * @param switchValues The switches
     */
    public SwitchValueListValidator(SwitchParserList parsers, SwitchValueList switchValues)
    {
        this.parsers = parsers;
        this.switchValues = switchValues;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onValidate()
    {
        // Go through switch parsers
        for (var parser : parsers)
        {
            // and report failure if the parser requires the switch but the list doesn't have it.
            problemIf(parser.isRequired() && !switchValues.has(parser), "Required switch ${debug} not found", parser);
        }

        // Go through the switches
        var found = new HashMap<String, Boolean>();
        for (var switchValue : switchValues)
        {
            // and get the corresponding parser,
            var parser = parsers.forName(switchValue.name());
            if (parser != null)
            {
                // and if it is invalid or has a duplicate, report failure
                problemIfNull(parser.asObject(switchValue), "Switch '${debug}' is invalid", switchValue);
                problemIf(found.containsKey(parser.name()), "Duplicate switch '${debug}'", switchValue);
                found.put(parser.name(), Boolean.TRUE);
            }
            else
            {
                // otherwise, report an unknown switch.
                problem("Unrecognized switch '${debug}'", switchValue);
            }
        }
    }
}

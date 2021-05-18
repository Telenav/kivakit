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

import com.telenav.kivakit.commandline.project.lexakai.diagrams.DiagramValidation;
import com.telenav.kivakit.kernel.data.validation.validators.BaseValidator;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

import java.util.HashMap;

/**
 * <b>Not Public API</b>
 * <p>
 * A switch list validator that validates switches and checks for required and duplicate switches.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramValidation.class)
@UmlNotPublicApi
public class SwitchValidator extends BaseValidator
{
    /** The list of switches to validate */
    @UmlAggregation
    private final SwitchList switches;

    /** The list of switch parsers to validate against */
    @UmlAggregation
    private final SwitchParserList parsers;

    /**
     * @param parsers The switch parsers to validate against
     * @param switches The switches
     */
    public SwitchValidator(final SwitchParserList parsers, final SwitchList switches)
    {
        this.parsers = parsers;
        this.switches = switches;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onValidate()
    {
        // Go through switch parsers
        for (final var parser : parsers)
        {
            // and report failure if the parser requires the switch but the list doesn't have it.
            problemIf(parser.isRequired() && !switches.has(parser), "Required switch ${debug} not found", parser);
        }

        // Go through the switches
        final var found = new HashMap<String, Boolean>();
        for (final var _switch : switches)
        {
            // and get the corresponding parser,
            final var parser = parsers.forName(_switch.name());
            if (parser != null)
            {
                // and if it is invalid or has a duplicate, report failure
                problemIf(!parser.isValid(), "Switch '${debug}' is invalid", _switch);
                problemIf(found.containsKey(parser.name()), "Duplicate switch '${debug}'", _switch);
                found.put(parser.name(), Boolean.TRUE);
            }
            else
            {
                // otherwise, report an unknown switch.
                problem("Unrecognized switch '${debug}'", _switch);
            }
        }
    }
}


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

package com.telenav.kivakit.commandline;

import com.telenav.kivakit.commandline.lexakai.DiagramArgument;
import com.telenav.kivakit.commandline.lexakai.DiagramSwitch;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Specifies the number of arguments required or allowed
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramArgument.class)
@UmlClassDiagram(diagram = DiagramSwitch.class)
public enum Quantifier
{
    /**
     * The argument must be present
     */
    REQUIRED,

    /**
     * The argument may be omitted
     */
    OPTIONAL,

    /**
     * The argument can appear any number of times or be omitted
     */
    ZERO_OR_MORE,

    /**
     * The argument must appear at least one time but may appear more times
     */
    ONE_OR_MORE,

    /**
     * The argument must appear at least two times but may appear more times
     */
    TWO_OR_MORE
}

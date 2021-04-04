package com.telenav.kivakit.core.commandline;

import com.telenav.kivakit.core.commandline.project.lexakai.diagrams.DiagramArgument;
import com.telenav.kivakit.core.commandline.project.lexakai.diagrams.DiagramSwitch;
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

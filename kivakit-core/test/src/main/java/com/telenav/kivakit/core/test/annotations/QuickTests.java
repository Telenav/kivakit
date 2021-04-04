////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.test.annotations;

import com.telenav.kivakit.core.test.project.lexakai.diagrams.DiagramTest;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * JUnit category for marking slow tests with @Category annotation.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramTest.class)
public interface QuickTests
{
}

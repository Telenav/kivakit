////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.test;

import com.telenav.kivakit.core.test.project.lexakai.diagrams.DiagramTest;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

@UmlClassDiagram(diagram = DiagramTest.class)
public class UnitTestWatcher extends TestWatcher
{
    @UmlAggregation(label = "watches for failures in")
    private final UnitTest test;

    public UnitTestWatcher(final UnitTest test)
    {
        this.test = test;
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    @Override
    protected void failed(final Throwable e, final Description description)
    {
        // If the test that failed is a random test,
        if (test.isRandomTest())
        {
            // then print out the seed value so it can be reproduced.
            System.err.println("// randomValueFactory().seed(" + test.randomValueFactory().seed() + "L);");
            System.err.flush();
        }
    }
}

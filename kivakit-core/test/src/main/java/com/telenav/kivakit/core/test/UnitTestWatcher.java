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

package com.telenav.kivakit.core.test;

import com.telenav.kivakit.core.test.project.lexakai.diagrams.DiagramTest;
import com.telenav.kivakit.core.test.random.RandomValueFactory;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * A JUnit test watcher that prints out any random seed value when a random test fails so that the failure can be
 * reproduced with {@link RandomValueFactory#seed(long)}.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
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

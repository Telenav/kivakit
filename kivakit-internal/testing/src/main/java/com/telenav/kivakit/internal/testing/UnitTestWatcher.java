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

package com.telenav.kivakit.internal.testing;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.internal.testing.internal.lexakai.DiagramTest;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * A JUnit test watcher that prints out any random seed value when a random test fails so that the failure can be
 * reproduced with <code>RandomValueFactory.seed(long)</code>.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramTest.class)
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class UnitTestWatcher extends TestWatcher
{
    @UmlAggregation(label = "watches for failures in")
    private final CoreUnitTest test;

    public UnitTestWatcher(CoreUnitTest test)
    {
        this.test = test;
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    @Override
    protected void failed(Throwable e, Description description)
    {
        // If the test that failed is a random test,
        if (test.isRandomTest())
        {
            // then print out the seed value, so it can be reproduced.
            System.err.println("// random().seed(" + test.random().seed() + "L);");
            System.err.flush();
        }
    }
}

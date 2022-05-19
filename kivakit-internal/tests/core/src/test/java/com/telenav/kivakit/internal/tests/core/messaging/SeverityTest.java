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

package com.telenav.kivakit.internal.tests.core.messaging;

import com.telenav.kivakit.core.messaging.messages.Severity;import com.telenav.kivakit.internal.test.support.CoreUnitTest;
import org.junit.Test;

public class SeverityTest extends CoreUnitTest
{
    @Test
    public void test()
    {
        ensure(Severity.CRITICAL.isGreaterThan(Severity.HIGH));
        ensure(Severity.HIGH.isGreaterThan(Severity.MEDIUM_HIGH));
        ensure(Severity.MEDIUM_HIGH.isGreaterThan(Severity.MEDIUM));
        ensure(Severity.MEDIUM.isGreaterThan(Severity.LOW));
        ensure(Severity.LOW.isGreaterThan(Severity.NONE));

        ensure(Severity.HIGH.isLessThan(Severity.CRITICAL));
        ensure(Severity.MEDIUM_HIGH.isLessThan(Severity.HIGH));
        ensure(Severity.MEDIUM.isLessThan(Severity.MEDIUM_HIGH));
        ensure(Severity.LOW.isLessThan(Severity.MEDIUM));
        ensure(Severity.NONE.isLessThan(Severity.LOW));
    }
}

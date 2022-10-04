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

import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

import static com.telenav.kivakit.core.messaging.messages.Severity.CRITICAL;
import static com.telenav.kivakit.core.messaging.messages.Severity.HIGH;
import static com.telenav.kivakit.core.messaging.messages.Severity.LOW;
import static com.telenav.kivakit.core.messaging.messages.Severity.MEDIUM;
import static com.telenav.kivakit.core.messaging.messages.Severity.NONE;

public class SeverityTest extends CoreUnitTest
{
    @Test
    public void test()
    {
        ensure(CRITICAL.isGreaterThan(HIGH));
        ensure(HIGH.isGreaterThan(MEDIUM));
        ensure(MEDIUM.isGreaterThan(LOW));
        ensure(LOW.isGreaterThan(NONE));

        ensure(HIGH.isLessThan(CRITICAL));
        ensure(MEDIUM.isLessThan(HIGH));
        ensure(LOW.isLessThan(MEDIUM));
        ensure(NONE.isLessThan(LOW));
    }
}

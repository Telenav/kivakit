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

package com.telenav.kivakit.conversion;

import com.telenav.kivakit.conversion.core.time.DurationConverter;
import com.telenav.kivakit.conversion.core.time.SecondsConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.listeners.MessageChecker;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import com.telenav.kivakit.core.time.Duration;
import org.junit.Test;

public class ConverterTest extends CoreUnitTest
{
    @Test
    public void testBadInputs()
    {
        new MessageChecker().expect(Problem.class).check(() ->
                ensureEqual(null, new SecondsConverter(Listener.emptyListener()).convert(null)));

        new MessageChecker().expect(Problem.class).check(() ->
                ensureEqual(null, new SecondsConverter(Listener.emptyListener()).convert("")));

        new MessageChecker().expect(Problem.class).check(() ->
                ensureEqual(null, new SecondsConverter(Listener.emptyListener()).convert("x")));
    }

    @Test
    public void testResultMonad()
    {
        ensureEqual(success("5 minutes")
                .convert(DurationConverter.class)
                .get(), Duration.minutes(5));

        var junk = listenTo(success("junk"));
        junk.silence();
        ensureEqual(junk.convert(DurationConverter.class)
                .get(), null);
    }
}

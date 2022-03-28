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

package com.telenav.kivakit.core.thread;
import com.telenav.kivakit.core.test.CoreUnitTest;
import com.telenav.kivakit.core.value.count.Count;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

@SuppressWarnings("SpellCheckingInspection")
public class BatcherTest extends CoreUnitTest
{
    private static int number;

    /**
     * Processes batches of integer objects keeping a count of how many have been "processed"
     */
    private static class TestBatcher extends Batcher<Integer>
    {
        private int total;

        @Override
        protected Batcher<Integer> copy()
        {
            return new TestBatcher();
        }

        @Override
        protected void onBatch(Batch objects)
        {
            total += objects.size();
            trace("Processed batch of " + objects.size() + ", total = " + total);
        }

        Count total()
        {
            return Count.count(total);
        }
    }

    @Test(timeout = 1_000)
    public void testEmptyFlush()
    {
        batcher().stop();
    }

    @Test
    public void testRandom()
    {
        testBatcher(47);
        testBatcher(511);
        testBatcher(1);
        testBatcher(1024);
    }

    @Test
    public void testStop()
    {
        var batcher = batcher();
        batcher.start(Count._1);
        var adder = batcher.adder();
        for (var i = 0; i < 1_000; i++)
        {
            adder.add(i);
        }
        batcher.stop();
        ensureEqual(Count._1_000, batcher.total());
    }

    @NotNull
    private BatcherTest.TestBatcher batcher()
    {
        return (TestBatcher) new TestBatcher()
                .withName("TestBatcher-" + number++)
                .withBatchSize(Count._100)
                .withQueueSize(Count._100);
    }

    private void testBatcher(int totalCount)
    {
        var batcher = batcher();
        batcher.addListener(this);
        batcher.start(Count._1);
        var adder = batcher.adder();
        for (var i = 0; i < totalCount; i++)
        {
            adder.add(i);
        }
        batcher.stop();
        ensureEqual(batcher.total(), Count.count(totalCount));
    }
}

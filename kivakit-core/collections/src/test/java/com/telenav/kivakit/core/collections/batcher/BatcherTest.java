////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.batcher;

import com.telenav.kivakit.core.collections.project.CoreCollectionsUnitTest;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.test.random.RandomValueFactory;
import org.junit.Test;

public class BatcherTest extends CoreCollectionsUnitTest
{
    /**
     * Processes batches of integer objects keeping a count of how many have been "processed"
     */
    private static class TestBatcher extends Batcher<Integer>
    {
        private static int number;

        private int total;

        public TestBatcher()
        {
            super("Test" + number++, Maximum._100, Count._100);
        }

        @Override
        protected void onBatch(final Batch objects)
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
        final var batcher = new TestBatcher();
        batcher.stop();
    }

    @Test
    public void testRandom()
    {
        final var random = new RandomValueFactory();
        for (var i = 0; i < 4; i++)
        {
            testBatcher(random.newInt(1, 50) * 10);
        }
    }

    @Test
    public void testStop()
    {
        final var batcher = new TestBatcher();
        batcher.start(Count._1);
        final var adder = batcher.adder();
        for (var i = 0; i < 1_000; i++)
        {
            adder.add(i);
        }
        batcher.stop();
        ensureEqual(Count._1_000, batcher.total());
    }

    private void testBatcher(final int totalCount)
    {
        final var batcher = new TestBatcher();
        batcher.addListener(this);
        batcher.start(Count._1);
        final var adder = batcher.adder();
        for (var i = 0; i < totalCount; i++)
        {
            adder.add(i);
        }
        batcher.stop();
        ensureEqual(batcher.total(), Count.count(totalCount));
    }
}

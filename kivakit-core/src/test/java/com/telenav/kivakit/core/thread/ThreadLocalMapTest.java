package com.telenav.kivakit.core.thread;

import com.telenav.kivakit.core.test.UnitTest;
import com.telenav.kivakit.core.value.count.Count;
import org.junit.Test;

public class ThreadLocalMapTest extends UnitTest
{
    @Test
    public void test()
    {
        var map = new ThreadLocalMap<Integer, Integer>();
        var workers = Count._8;
        var executor = Threads.threadPool("TestPool", workers);
        workers.loop(() -> executor.submit(() ->
        {
            for (var i = 0; i < 100_000; i++)
            {
                map.put(i, i);
            }
            for (var i = 0; i < 100_000; i++)
            {
                ensureEqual(i, map.get(i));
            }
        }));
        Threads.shutdownAndAwait(executor);
    }
}

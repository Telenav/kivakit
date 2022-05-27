package com.telenav.kivakit.internal.tests.core.thread;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import com.telenav.kivakit.core.thread.ThreadLocalMap;
import com.telenav.kivakit.core.thread.Threads;
import com.telenav.kivakit.core.value.count.Count;
import org.junit.Test;

public class ThreadLocalMapTest extends CoreUnitTest
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

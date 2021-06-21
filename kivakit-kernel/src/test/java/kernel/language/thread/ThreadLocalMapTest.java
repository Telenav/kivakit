package kernel.language.thread;

import com.telenav.kivakit.kernel.language.threading.Threads;
import com.telenav.kivakit.kernel.language.threading.local.ThreadLocalMap;
import com.telenav.kivakit.kernel.language.values.count.Count;
import org.junit.Test;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;

public class ThreadLocalMapTest
{
    @Test
    public void test()
    {
        final var map = new ThreadLocalMap<Integer, Integer>();
        final var workers = Count._8;
        final var executor = Threads.threadPool("TestPool", workers);
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

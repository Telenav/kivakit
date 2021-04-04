////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.language.thread.locks;

import com.telenav.kivakit.core.kernel.language.threading.KivaKitThread;
import com.telenav.kivakit.core.kernel.language.threading.locks.legacy.BooleanLock;
import com.telenav.kivakit.core.kernel.language.threading.locks.legacy.NotifyBooleanLock;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.messaging.listeners.ThrowingListener;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureFalse;

public class BooleanLockTest
{
    private static final Duration SHORT_TIME = Duration.milliseconds(50);

    private static final Duration LONG_TIME = SHORT_TIME.times(4);

    @Test
    public void test()
    {
        new BaseThreadTest()
        {
            @Override
            protected void onRun()
            {
                final var lock = lock();

                new KivaKitThread("test")
                {
                    @Override
                    protected void onRun()
                    {
                        lock.set(true);
                    }

                    {
                        addListener(new ThrowingListener());
                    }
                }.start();

                // We will wait a while, so waitFor() and get() should both return true
                ensure(lock.waitFor(true, maximumWait()));
                ensure(lock.get());
            }
        }.run();
    }

    @Test
    public void testTimeOut()
    {
        new BaseThreadTest()
        {
            @Override
            protected void onRun()
            {
                // Create lock
                final var lock = lock();

                new KivaKitThread("test")
                {
                    @Override
                    protected void onRun()
                    {
                        // Sleep a while
                        LONG_TIME.sleep();

                        // Set the lock to true
                        lock.set(true);
                    }

                    {
                        addListener(new ThrowingListener());
                    }
                }.startSynchronously();

                // If we don't wait long enough, waitFor() and get() should both return false
                ensureFalse(lock.waitFor(true, SHORT_TIME));
                ensureFalse(lock.get());
            }
        }.run();
    }

    private BooleanLock lock()
    {
        // Create lock
        final BooleanLock lock = new NotifyBooleanLock();

        // Should default to false
        ensureEqual(false, lock.get());

        // Test set(true)
        lock.set(true);
        ensureEqual(true, lock.get());

        // Test set(false)
        lock.set(false);
        ensureEqual(false, lock.get());

        return lock;
    }
}

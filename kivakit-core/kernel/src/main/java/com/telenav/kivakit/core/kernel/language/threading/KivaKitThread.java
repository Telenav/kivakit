////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.threading;

import com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.core.kernel.interfaces.lifecycle.Startable;
import com.telenav.kivakit.core.kernel.interfaces.naming.Named;
import com.telenav.kivakit.core.kernel.language.threading.locks.legacy.ConditionLock;
import com.telenav.kivakit.core.kernel.language.threading.locks.legacy.NotifyAllBooleanLock;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageThread;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("UnusedReturnValue")
@UmlClassDiagram(diagram = DiagramLanguageThread.class)
public class KivaKitThread extends BaseRepeater implements Startable, Runnable, Named
{
    private static final Set<String> names = new HashSet<>();

    public static KivaKitThread repeat(final Listener listener, final Duration every, final String name,
                                       final Runnable code)
    {
        return run(listener, name, () -> every.loop(listener, code));
    }

    public static KivaKitThread run(final Listener listener, final String name, final Runnable code)
    {
        final var thread = listener.listenTo(new KivaKitThread(name, code));
        thread.start();
        return thread;
    }

    private final ConditionLock started = new ConditionLock(new NotifyAllBooleanLock());

    private final ConditionLock running = new ConditionLock(new NotifyAllBooleanLock());

    private final Thread thread;

    private Duration initialDelay = Duration.NONE;

    private boolean initialized;

    private Time startedAt;

    private Runnable code;

    public KivaKitThread(final String name, final Runnable code)
    {
        this(name);
        this.code = code;
    }

    public KivaKitThread(final String name)
    {
        super(name("Kiva-" + name));
        thread = new Thread(this, objectName());
        thread.setDaemon(true);
    }

    public KivaKitThread daemon(final boolean daemon)
    {
        thread.setDaemon(daemon);
        return this;
    }

    public KivaKitThread highPriority()
    {
        thread.setPriority(Thread.MAX_PRIORITY);
        return this;
    }

    public KivaKitThread initialDelay(final Duration initialDelay)
    {
        this.initialDelay = initialDelay;
        return this;
    }

    public KivaKitThread interrupt()
    {
        thread.interrupt();
        return this;
    }

    @Override
    public boolean isRunning()
    {
        return running.isSatisfied();
    }

    public void join()
    {
        try
        {
            thread.join();
        }
        catch (final InterruptedException ignored)
        {
        }
    }

    public KivaKitThread lowPriority()
    {
        thread.setPriority(Thread.MIN_PRIORITY);
        return this;
    }

    @Override
    public String name()
    {
        return thread.getName();
    }

    @Override
    public void run()
    {
        started();
        trace("Running");
        initialDelay();
        onBefore();
        try
        {
            if (code != null)
            {
                code.run();
            }
            else
            {
                onRun();
            }
        }
        catch (final Throwable e)
        {
            problem(e, "${class} threw exception", getClass());
        }
        onAfter();
        trace("Exiting");
        exited();
    }

    @Override
    public boolean start()
    {
        if (!thread.isAlive())
        {
            trace("Starting thread '${debug}'", thread.getName());
            try
            {
                thread.start();
            }
            catch (final IllegalThreadStateException ignored)
            {
                // Ignore
            }
        }
        return true;
    }

    public KivaKitThread startSynchronously()
    {
        start();
        // We don't just want the thread to be started, but to also be running.
        started.waitFor(true);
        running.waitFor(true);
        trace("Started thread '${debug}'", thread.getName());
        return this;
    }

    public Time startedAt()
    {
        return startedAt;
    }

    protected void exited()
    {
        running.reset();
    }

    protected void initialDelay()
    {
        if (!initialized)
        {
            initialDelay.sleep();
            initialized = true;
        }
    }

    protected void onAfter()
    {
    }

    protected void onBefore()
    {
    }

    protected void onRun()
    {
        Ensure.fail("Must either provide closure to constructor or implement onRun()");
    }

    protected void started()
    {
        startedAt = Time.now();
        started.satisfy();
        running.satisfy();
    }

    protected Thread thread()
    {
        return thread;
    }

    private static String name(String name)
    {
        int number = 1;
        while (names.contains(name))
        {
            name = name + "-" + number++;
        }
        names.add(name);
        return name;
    }
}

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

package com.telenav.kivakit.network.email;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.network.email.internal.lexakai.DiagramEmail;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * A simple email queue implemented with {@link ArrayBlockingQueue}, with no persistent backing.
 *
 * <p><b>Queueing</b></p>
 *
 * <ul>
 *     <li>{@link #close()}</li>
 *     <li>{@link #enqueue(Email, Duration)}</li>
 *     <li>{@link #isClosed()}</li>
 *     <li>{@link #isEmpty()}</li>
 *     <li>{@link #markSent(Email)}</li>
 *     <li>{@link #take()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramEmail.class)
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
class EmailQueue
{
    private volatile boolean closed;

    private final ArrayBlockingQueue<Email> queue = new ArrayBlockingQueue<>(1000);

    /**
     * Closes the queue to new entries
     */
    public void close()
    {
        closed = true;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean enqueue(Email email, Duration maximumWait)
    {
        if (!closed)
        {
            try
            {
                return queue.offer(email, maximumWait.milliseconds(), TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException ignored)
            {
            }
        }
        return false;
    }

    public boolean isClosed()
    {
        return closed;
    }

    public boolean isEmpty()
    {
        return queue.isEmpty();
    }

    public void markSent(Email email)
    {
        email.sentAt = Time.now();
    }

    public Email take()
    {
        try
        {
            return queue.take();
        }
        catch (InterruptedException ignored)
        {
            return null;
        }
    }
}

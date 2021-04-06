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

package com.telenav.kivakit.core.resource.reading;

import com.telenav.kivakit.core.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.core.kernel.interfaces.io.Closeable;
import com.telenav.kivakit.core.kernel.interfaces.messaging.Transmittable;
import com.telenav.kivakit.core.kernel.language.iteration.BaseIterator;
import com.telenav.kivakit.core.kernel.language.threading.context.CodeContext;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.messaging.Repeater;
import com.telenav.kivakit.core.kernel.messaging.repeaters.BaseRepeater;

public abstract class BaseObjectReader<T> extends BaseIterator<T> implements Repeater, Closeable
{
    private final BaseRepeater repeater = new BaseRepeater(getClass());

    @Override
    public void addListener(final Listener listener)
    {
        repeater.addListener(listener);
    }

    @Override
    public void addListener(final Listener listener, final Filter<Transmittable> filter)
    {
        repeater.addListener(listener, filter);
    }

    @Override
    public void clearListeners()
    {
        repeater.clearListeners();
    }

    @Override
    public CodeContext debugCodeContext()
    {
        return repeater.debugCodeContext();
    }

    @Override
    public void debugCodeContext(final CodeContext context)
    {
        repeater.debugCodeContext(context);
    }

    @Override
    public boolean hasListeners()
    {
        return repeater.hasListeners();
    }

    @Override
    public void onMessage(final Message message)
    {
        repeater.onMessage(message);
    }

    @Override
    public void removeListener(final Listener listener)
    {
        repeater.removeListener(listener);
    }

    @Override
    public void transmit(final Transmittable message)
    {
        repeater.transmit(message);
    }
}

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

package com.telenav.kivakit.core.serialization.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.core.kernel.language.vm.JavaVirtualMachine;
import com.telenav.kivakit.core.kernel.messaging.Debug;
import com.telenav.kivakit.core.kernel.messaging.Listener;

import java.util.function.Supplier;

/**
 * <b>Not public API</b>
 *
 * <p>
 * Performs Kryo serialization with debug tracing enabled by the environment variable KIVAKIT_SERIALIZATION_TRACE.
 * </p>
 *
 * @author jonathanl (shibo)
 */
class DebugKryo extends Kryo
{
    /** True to turn on Kryo tracing */
    private static final boolean TRACE = JavaVirtualMachine.isPropertyTrue("KIVAKIT_SERIALIZATION_TRACE");

    static
    {
        if (TRACE)
        {
            com.esotericsoftware.minlog.Log.TRACE();
        }
    }

    private final Debug DEBUG;

    private final Listener listener;

    public DebugKryo(final Listener listener)
    {
        this.listener = listener;
        DEBUG = new Debug(listener);
    }

    @Override
    public Registration readClass(final Input input)
    {
        return read(input, () -> super.readClass(input));
    }

    @Override
    public Object readClassAndObject(final Input input)
    {
        return read(input, () -> super.readClassAndObject(input));
    }

    @Override
    public <T> T readObject(final Input input, final Class<T> type)
    {
        return read(input, () -> super.readObject(input, type));
    }

    @Override
    public <T> T readObject(final Input input, final Class<T> type, final Serializer serializer)
    {
        return read(input, () -> super.readObject(input, type, serializer));
    }

    @Override
    public <T> T readObjectOrNull(final Input input, final Class<T> type)
    {
        return read(input, () -> super.readObjectOrNull(input, type));
    }

    @Override
    public <T> T readObjectOrNull(final Input input, final Class<T> type, final Serializer serializer)
    {
        return read(input, () -> super.readObjectOrNull(input, type, serializer));
    }

    @Override
    public Registration writeClass(final Output output, final Class type)
    {
        return write(type, output, () -> super.writeClass(output, type));
    }

    @Override
    public void writeClassAndObject(final Output output, final Object object)
    {
        write(object, output, () -> super.writeClassAndObject(output, object));
    }

    @Override
    public void writeObject(final Output output, final Object object)
    {
        write(object, output, () -> super.writeObject(output, object));
    }

    @Override
    public void writeObject(final Output output, final Object object, final Serializer serializer)
    {
        write(object, output, () -> super.writeObject(output, object, serializer));
    }

    @Override
    public void writeObjectOrNull(final Output output, final Object object, final Class type)
    {
        write(object, output, () -> super.writeObjectOrNull(output, object, type));
    }

    @Override
    public void writeObjectOrNull(final Output output, final Object object, final Serializer serializer)
    {
        write(object, output, () -> super.writeObjectOrNull(output, object, serializer));
    }

    private <T> T read(final Input input, final Supplier<T> code)
    {
        trace(true);
        try
        {
            final var position = input.total();
            final T value = code.get();
            listener.trace("Read at $: $", position, value);
            return value;
        }
        finally
        {
            trace(false);
        }
    }

    private void trace(final boolean on)
    {
        if (DEBUG.isDebugOn() && TRACE)
        {
            if (on)
            {
                com.esotericsoftware.minlog.Log.TRACE();
            }
            else
            {
                com.esotericsoftware.minlog.Log.NONE();
            }
        }
    }

    private <T> T write(final Object object, final Output output, final Supplier<T> code)
    {
        trace(true);
        try
        {
            listener.trace("Writing at $: $", output.total(), object);
            return code.get();
        }
        finally
        {
            trace(false);
        }
    }

    private void write(final Object object, final Output output, final Runnable code)
    {
        trace(true);
        try
        {
            listener.trace("Writing at $: $", output.total(), object);
            code.run();
        }
        finally
        {
            trace(false);
        }
    }
}

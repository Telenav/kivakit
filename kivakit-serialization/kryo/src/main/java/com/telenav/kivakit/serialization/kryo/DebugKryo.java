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

package com.telenav.kivakit.serialization.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.core.vm.SystemProperties;
import com.telenav.kivakit.core.messaging.Debug;
import com.telenav.kivakit.core.messaging.Listener;

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
    private static final boolean TRACE = SystemProperties.isPropertyTrue("KIVAKIT_SERIALIZATION_TRACE");

    static
    {
        if (TRACE)
        {
            com.esotericsoftware.minlog.Log.TRACE();
        }
    }

    private final Debug DEBUG;

    private final Listener listener;

    public DebugKryo(Listener listener)
    {
        this.listener = listener;
        DEBUG = new Debug(listener);
    }

    @Override
    public Registration readClass(Input input)
    {
        return read(input, () -> super.readClass(input));
    }

    @Override
    public Object readClassAndObject(Input input)
    {
        return read(input, () -> super.readClassAndObject(input));
    }

    @Override
    public <T> T readObject(Input input, Class<T> type)
    {
        return read(input, () -> super.readObject(input, type));
    }

    @Override
    public <T> T readObject(Input input, Class<T> type, Serializer serializer)
    {
        return read(input, () -> super.readObject(input, type, serializer));
    }

    @Override
    public <T> T readObjectOrNull(Input input, Class<T> type)
    {
        return read(input, () -> super.readObjectOrNull(input, type));
    }

    @Override
    public <T> T readObjectOrNull(Input input, Class<T> type, Serializer serializer)
    {
        return read(input, () -> super.readObjectOrNull(input, type, serializer));
    }

    @Override
    public Registration writeClass(Output output, Class type)
    {
        return write(type, output, () -> super.writeClass(output, type));
    }

    @Override
    public void writeClassAndObject(Output output, Object object)
    {
        write(object, output, () -> super.writeClassAndObject(output, object));
    }

    @Override
    public void writeObject(Output output, Object object)
    {
        write(object, output, () -> super.writeObject(output, object));
    }

    @Override
    public void writeObject(Output output, Object object, Serializer serializer)
    {
        write(object, output, () -> super.writeObject(output, object, serializer));
    }

    @Override
    public void writeObjectOrNull(Output output, Object object, Class type)
    {
        write(object, output, () -> super.writeObjectOrNull(output, object, type));
    }

    @Override
    public void writeObjectOrNull(Output output, Object object, Serializer serializer)
    {
        write(object, output, () -> super.writeObjectOrNull(output, object, serializer));
    }

    private <T> T read(Input input, Supplier<T> code)
    {
        trace(true);
        try
        {
            var position = input.total();
            T value = code.get();
            listener.trace("Read at $: $", position, value);
            return value;
        }
        finally
        {
            trace(false);
        }
    }

    private void trace(boolean on)
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

    private <T> T write(Object object, Output output, Supplier<T> code)
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

    private void write(Object object, Output output, Runnable code)
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

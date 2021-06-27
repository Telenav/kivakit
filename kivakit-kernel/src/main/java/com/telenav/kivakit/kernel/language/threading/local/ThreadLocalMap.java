package com.telenav.kivakit.kernel.language.threading.local;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ThreadLocalMap<Key, Value> extends AbstractMap<Key, Value>
{
    private final ThreadLocal<Map<Key, Value>> map = ThreadLocal.withInitial(HashMap::new);

    public ThreadLocalMap()
    {
    }

    @NotNull
    @Override
    public Set<Entry<Key, Value>> entrySet()
    {
        return this.map.get().entrySet();
    }

    @Override
    public Value put(final Key key, final Value value)
    {
        return this.map.get().put(key, value);
    }
}
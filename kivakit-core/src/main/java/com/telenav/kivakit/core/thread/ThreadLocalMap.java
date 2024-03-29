package com.telenav.kivakit.core.thread;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A map with values stored per-thread.
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class ThreadLocalMap<Key, Value> extends AbstractMap<Key, Value>
{
    /** The thread local maps */
    private final ThreadLocal<Map<Key, Value>> map = ThreadLocal.withInitial(HashMap::new);

    @NotNull
    @Override
    public Set<Entry<Key, Value>> entrySet()
    {
        return map.get().entrySet();
    }

    @Override
    public Value put(Key key, Value value)
    {
        return map.get().put(key, value);
    }
}

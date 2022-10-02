package com.telenav.kivakit.core.thread;

import com.telenav.kivakit.annotations.code.ApiQuality;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A map with values stored per-thread.
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = API_STABLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
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

package com.telenav.kivakit.core.value.name;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.mixins.Mixin;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A mixin for naming an object
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public interface NameMixin extends
        Named,
        Mixin
{
    @Override
    default String name()
    {
        return named().name();
    }

    /**
     * <b>Not public API</b>
     *
     * @return The implementation associated with this mixin
     */
    default Named named()
    {
        return mixin(NameMixin.class, Name::new);
    }
}

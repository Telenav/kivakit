package com.telenav.kivakit.core.value.name;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.mixins.Mixin;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

/**
 * A mixin for naming an object
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABILITY_STABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
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

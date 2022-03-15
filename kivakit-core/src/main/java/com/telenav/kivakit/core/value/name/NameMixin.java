package com.telenav.kivakit.core.value.name;

import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.mixins.Mixin;

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

/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * //
 * // © 2011-2021 Telenav, Inc.
 * //
 * // Licensed under the Apache License, Version 2.0 (the "License");
 * // you may not use this file except in compliance with the License.
 * // You may obtain a copy of the License at
 * //
 * // http://www.apache.org/licenses/LICENSE-2.0
 * //
 * // Unless required by applicable law or agreed to in writing, software
 * // distributed under the License is distributed on an "AS IS" BASIS,
 * // WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * // See the License for the specific language governing permissions and
 * // limitations under the License.
 * //
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 *
 */

package com.telenav.kivakit.kernel.language.trait;

import com.telenav.kivakit.kernel.language.objects.Hash;

/**
 * @author jonathanl (shibo)
 */
public class TraitKey
{
    private final Object object;

    private final Class<? extends Trait> traitType;

    public TraitKey(final Object object, final Class<? extends Trait> traitType)
    {
        this.object = object;
        this.traitType = traitType;
    }

    @Override
    public boolean equals(final Object uncast)
    {
        if (uncast instanceof TraitKey)
        {
            final TraitKey that = (TraitKey) uncast;
            return object == that.object && traitType == that.traitType;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Hash.many(object, traitType);
    }
}

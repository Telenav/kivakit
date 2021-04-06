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

package com.telenav.kivakit.core.collections.project;

import com.telenav.kivakit.core.collections.map.MultiSet;
import com.telenav.kivakit.core.collections.primitive.PrimitiveCollection;
import com.telenav.kivakit.core.collections.primitive.array.arrays.ByteArrayArray;
import com.telenav.kivakit.core.collections.primitive.array.arrays.IntArrayArray;
import com.telenav.kivakit.core.collections.primitive.array.arrays.LongArrayArray;
import com.telenav.kivakit.core.collections.primitive.array.bits.BitArray;
import com.telenav.kivakit.core.collections.primitive.array.bits.FixedSizeBitArray;
import com.telenav.kivakit.core.collections.primitive.array.packed.PackedArray;
import com.telenav.kivakit.core.collections.primitive.array.packed.SplitPackedArray;
import com.telenav.kivakit.core.collections.primitive.array.scalars.*;
import com.telenav.kivakit.core.collections.primitive.array.strings.PackedStringArray;
import com.telenav.kivakit.core.collections.primitive.list.store.IntLinkedListStore;
import com.telenav.kivakit.core.collections.primitive.list.store.LongLinkedListStore;
import com.telenav.kivakit.core.collections.primitive.list.store.PackedStringStore;
import com.telenav.kivakit.core.collections.primitive.map.DefaultHashingStrategy;
import com.telenav.kivakit.core.collections.primitive.map.multi.dynamic.LongToIntMultiMap;
import com.telenav.kivakit.core.collections.primitive.map.multi.dynamic.LongToLongMultiMap;
import com.telenav.kivakit.core.collections.primitive.map.multi.fixed.IntToByteFixedMultiMap;
import com.telenav.kivakit.core.collections.primitive.map.multi.fixed.IntToIntFixedMultiMap;
import com.telenav.kivakit.core.collections.primitive.map.multi.fixed.IntToLongFixedMultiMap;
import com.telenav.kivakit.core.collections.primitive.map.multi.fixed.IntToPackedArrayFixedMultiMap;
import com.telenav.kivakit.core.collections.primitive.map.multi.fixed.LongToLongFixedMultiMap;
import com.telenav.kivakit.core.collections.primitive.map.objects.LongToObjectMap;
import com.telenav.kivakit.core.collections.primitive.map.scalars.*;
import com.telenav.kivakit.core.collections.primitive.map.split.SplitLongToByteMap;
import com.telenav.kivakit.core.collections.primitive.map.split.SplitLongToIntMap;
import com.telenav.kivakit.core.collections.primitive.map.split.SplitLongToLongMap;
import com.telenav.kivakit.core.collections.primitive.set.LongSet;
import com.telenav.kivakit.core.collections.primitive.set.SplitLongSet;
import com.telenav.kivakit.core.kernel.language.collections.list.LinkedObjectList;
import com.telenav.kivakit.core.serialization.kryo.KryoTypes;

import static com.telenav.kivakit.core.collections.primitive.array.packed.PackedPrimitiveArray.OverflowHandling;

/**
 * Serializer for objects in the kivakit-core-collections module and in dependent projects.
 *
 * @author jonathanl (shibo)
 */
public class CoreCollectionsKryoTypes extends KryoTypes
{
    public CoreCollectionsKryoTypes()
    {
        //----------------------------------------------------------------------------------------------
        // NOTE: To maintain backward compatibility of serialization, registration groups and the types
        // in each registration group must remain in the same order.
        //----------------------------------------------------------------------------------------------

        group("collections", () -> register(PrimitiveCollection.class));

        group("dynamic-arrays", () ->
        {
            register(BitArray.class);
            register(ByteArray.class);
            register(CharArray.class);
            register(ShortArray.class);
            register(IntArray.class);
            register(LongArray.class);
        });

        group("packed-arrays", () ->
        {
            register(PackedArray.class);
            register(PackedStringArray.class);
            register(PackedStringStore.class);
            register(PackedArray[].class);
            register(OverflowHandling.class);
        });

        group("split-arrays", () ->
        {
            register(SplitByteArray.class);
            register(SplitCharArray.class);
            register(SplitIntArray.class);
            register(SplitLongArray.class);
            register(SplitPackedArray.class);
        });

        group("array-arrays", () ->
        {
            register(ByteArrayArray.class);
            register(IntArrayArray.class);
            register(LongArrayArray.class);
        });

        group("lists", () ->
        {
            register(LinkedObjectList.class);
            register(IntLinkedListStore.class);
            register(LongLinkedListStore.class);
        });

        group("maps", () ->
        {
            register(DefaultHashingStrategy.class);
            register(IntToByteMap.class);
            register(IntToIntMap.class);
            register(IntToLongMap.class);
            register(LongToByteMap.class);
            register(LongToIntMap.class);
            register(LongToLongMap.class);
            register(LongToObjectMap.class);
            register(StringToIntMap.class);
            register(StringToObjectMap.class);
        });

        group("collection-arrays", () ->
        {
            register(BitArray[].class);
            register(ByteArray[].class);
            register(CharArray[].class);
            register(ShortArray[].class);
            register(IntArray[].class);
            register(LongArray[].class);
            register(LongToByteMap[].class);
            register(LongToIntMap[].class);
            register(LongToLongMap[].class);
            register(LongSet[].class);
        });

        group("split-maps", () ->
        {
            register(SplitLongToByteMap.class);
            register(SplitLongToIntMap.class);
            register(SplitLongToLongMap.class);
        });

        group("sets", () ->
        {
            register(FixedSizeBitArray.class);
            register(LongSet.class);
            register(SplitLongSet.class);
            register(MultiSet.class);
        });

        group("multi-maps", () ->
        {
            register(IntToByteFixedMultiMap.class);
            register(IntToLongFixedMultiMap.class);
            register(LongToLongMultiMap.class);
            register(LongToLongFixedMultiMap.class);
            register(IntToIntFixedMultiMap.class);
            register(IntToPackedArrayFixedMultiMap.class);
            register(LongToIntMultiMap.class);
        });
    }
}

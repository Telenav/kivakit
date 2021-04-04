open module kivakit.core.collections
{
    requires transitive kivakit.core.serialization.kryo;
    requires transitive kivakit.core.test;

    exports com.telenav.kivakit.core.collections.batcher;
    exports com.telenav.kivakit.core.collections.iteration.iterables;
    exports com.telenav.kivakit.core.collections.iteration.iterators;
    exports com.telenav.kivakit.core.collections.map;
    exports com.telenav.kivakit.core.collections.primitive;
    exports com.telenav.kivakit.core.collections.primitive.array;
    exports com.telenav.kivakit.core.collections.primitive.array.arrays;
    exports com.telenav.kivakit.core.collections.primitive.array.bits.io.input;
    exports com.telenav.kivakit.core.collections.primitive.array.bits.io.output;
    exports com.telenav.kivakit.core.collections.primitive.array.bits.io;
    exports com.telenav.kivakit.core.collections.primitive.array.bits;
    exports com.telenav.kivakit.core.collections.primitive.array.packed;
    exports com.telenav.kivakit.core.collections.primitive.array.scalars;
    exports com.telenav.kivakit.core.collections.primitive.array.strings;
    exports com.telenav.kivakit.core.collections.primitive.iteration;
    exports com.telenav.kivakit.core.collections.primitive.list;
    exports com.telenav.kivakit.core.collections.primitive.list.store;
    exports com.telenav.kivakit.core.collections.primitive.map;
    exports com.telenav.kivakit.core.collections.primitive.map.multi.dynamic;
    exports com.telenav.kivakit.core.collections.primitive.map.multi.fixed;
    exports com.telenav.kivakit.core.collections.primitive.map.objects;
    exports com.telenav.kivakit.core.collections.primitive.map.scalars;
    exports com.telenav.kivakit.core.collections.primitive.map.split;
    exports com.telenav.kivakit.core.collections.primitive.set;
    exports com.telenav.kivakit.core.collections.project;
    exports com.telenav.kivakit.core.collections.set.logical;
    exports com.telenav.kivakit.core.collections.set.logical.operations;
    exports com.telenav.kivakit.core.collections.set;
    exports com.telenav.kivakit.core.collections.stack;
    exports com.telenav.kivakit.core.collections.watcher;
    exports com.telenav.kivakit.core.collections.primitive.map.multi;
}

open module kivakit.core.collections
{
    requires transitive kivakit.core.serialization.kryo;
    requires transitive kivakit.core.test;

    exports com.telenav.kivakit.core.collections.batcher;
    exports com.telenav.kivakit.core.collections.iteration.iterables;
    exports com.telenav.kivakit.core.collections.iteration.iterators;
    exports com.telenav.kivakit.core.collections.map;
    exports com.telenav.kivakit.core.collections.project;
    exports com.telenav.kivakit.core.collections.project.lexakai.diagrams;
    exports com.telenav.kivakit.core.collections.set.logical;
    exports com.telenav.kivakit.core.collections.set.logical.operations;
    exports com.telenav.kivakit.core.collections.set;
    exports com.telenav.kivakit.core.collections.stack;
    exports com.telenav.kivakit.core.collections.watcher;
}

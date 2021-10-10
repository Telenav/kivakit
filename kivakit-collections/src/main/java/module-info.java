open module kivakit.collections
{
    // KivaKit
    requires transitive kivakit.serialization.kryo;
    requires transitive kivakit.test;

    requires junit;
    requires org.jetbrains.annotations;

    // Module exports
    exports com.telenav.kivakit.collections.iteration.iterables;
    exports com.telenav.kivakit.collections.iteration.iterators;
    exports com.telenav.kivakit.collections.map;
    exports com.telenav.kivakit.collections.project;
    exports com.telenav.kivakit.collections.project.lexakai.diagrams;
    exports com.telenav.kivakit.collections.set.logical;
    exports com.telenav.kivakit.collections.set.logical.operations;
    exports com.telenav.kivakit.collections.set;
    exports com.telenav.kivakit.collections.stack;
    exports com.telenav.kivakit.collections.watcher;
    exports com.telenav.kivakit.collections;
}

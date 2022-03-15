open module kivakit.interfaces
{
    // Annotations
    requires transitive lexakai.annotations;
    requires transitive org.jetbrains.annotations;

    // Module exports
    exports com.telenav.kivakit.interfaces.code;
    exports com.telenav.kivakit.interfaces.collection;
    exports com.telenav.kivakit.interfaces.comparison;
    exports com.telenav.kivakit.interfaces.factory;
    exports com.telenav.kivakit.interfaces.function;
    exports com.telenav.kivakit.interfaces.io;
    exports com.telenav.kivakit.interfaces.lifecycle;
    exports com.telenav.kivakit.interfaces.loading;
    exports com.telenav.kivakit.interfaces.messaging;
    exports com.telenav.kivakit.interfaces.model;
    exports com.telenav.kivakit.interfaces.naming;
    exports com.telenav.kivakit.interfaces.numeric;
    exports com.telenav.kivakit.interfaces.string;
    exports com.telenav.kivakit.interfaces.time;
    exports com.telenav.kivakit.interfaces.value;
    exports com.telenav.kivakit.interfaces.monads;
}

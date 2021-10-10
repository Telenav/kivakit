open module kivakit.serialization.kryo
{
    // KivaKit
    requires transitive kivakit.serialization.core;
    requires transitive kivakit.test;

    // Kryo
    requires kryo;
    requires de.javakaffee.kryoserializers;
    requires minlog;

    // Module exports
    exports com.telenav.kivakit.serialization.kryo;
    exports com.telenav.kivakit.serialization.kryo.project.lexakai.diagrams;
}

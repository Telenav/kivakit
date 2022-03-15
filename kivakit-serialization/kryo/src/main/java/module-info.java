open module kivakit.serialization.kryo
{
    // KivaKit
    requires transitive kivakit.serialization.properties;

    // Kryo
    requires transitive kryo;
    requires transitive de.javakaffee.kryoserializers;
    requires minlog;
    requires kivakit.serialization.core;

    // Module exports
    exports com.telenav.kivakit.serialization.kryo;
    exports com.telenav.kivakit.serialization.kryo.lexakai;
    exports com.telenav.kivakit.serialization.kryo.types;
}

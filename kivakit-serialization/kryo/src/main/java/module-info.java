open module kivakit.serialization.kryo
{
    requires transitive kivakit.serialization.core;
    requires transitive kivakit.test;

    requires transitive kryo;
    requires transitive de.javakaffee.kryoserializers;
    requires transitive minlog;

    exports com.telenav.kivakit.serialization.kryo;
    exports com.telenav.kivakit.serialization.kryo.project;
    exports com.telenav.kivakit.serialization.kryo.project.lexakai.diagrams;
}

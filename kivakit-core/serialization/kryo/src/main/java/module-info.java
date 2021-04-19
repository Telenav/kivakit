open module kivakit.core.serialization.kryo
{
    requires transitive kivakit.core.serialization.core;
    requires transitive kivakit.core.test;

    requires transitive kryo;
    requires transitive de.javakaffee.kryoserializers;
    requires transitive minlog;

    exports com.telenav.kivakit.core.serialization.kryo;
    exports com.telenav.kivakit.core.serialization.kryo.project;
    exports com.telenav.kivakit.core.serialization.kryo.project.lexakai.diagrams;
}

open module kivakit.settings
{
    // KivaKit
    requires transitive kivakit.resource;
    requires transitive kivakit.serialization.properties;
    requires transitive kivakit.serialization.gson;

    // Lexakai
    requires lexakai.annotations;

    // Module exports
    exports com.telenav.kivakit.settings.lexakai;
    exports com.telenav.kivakit.settings;
    exports com.telenav.kivakit.settings.stores;
}

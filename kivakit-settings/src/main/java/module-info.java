open module kivakit.settings
{
    // KivaKit
    requires transitive kivakit.resource;
    requires transitive kivakit.serialization.properties;

    // Lexakai
    requires lexakai.annotations;

    // Module exports
    exports com.telenav.kivakit.settings.lexakai;
    exports com.telenav.kivakit.settings;
    exports com.telenav.kivakit.settings.stores;
}

open module kivakit.settings
{
    // KivaKit
    requires transitive kivakit.resource;
    requires transitive kivakit.serialization.json;
    requires lexakai.annotations;
    requires kivakit.core;

    // Module exports
    exports com.telenav.kivakit.settings.project.lexakai;
    exports com.telenav.kivakit.settings.settings;
    exports com.telenav.kivakit.settings.settings.stores;
}

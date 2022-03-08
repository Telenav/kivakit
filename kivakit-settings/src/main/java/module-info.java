open module kivakit.settings
{
    // KivaKit
    requires transitive kivakit.resource;

    // Lexakai
    requires lexakai.annotations;

    // Module exports
    exports com.telenav.kivakit.settings.project.lexakai;
    exports com.telenav.kivakit.settings.settings;
    exports com.telenav.kivakit.settings.settings.stores;
}

open module kivakit.configuration
{
    // KivaKit
    requires transitive kivakit.resource;
    requires kivakit.test;

    // Module exports
    exports com.telenav.kivakit.configuration;
    exports com.telenav.kivakit.configuration.lookup;
    exports com.telenav.kivakit.configuration.project.lexakai.diagrams;
    exports com.telenav.kivakit.configuration.settings;
    exports com.telenav.kivakit.configuration.settings.stores.resource;
    exports com.telenav.kivakit.configuration.settings.stores.memory;
}

open module kivakit.application
{
    // KivaKit
    requires transitive kivakit.configuration;
    requires transitive kivakit.commandline;
    requires transitive kivakit.component;

    // Module exports
    exports com.telenav.kivakit.application;
    exports com.telenav.kivakit.application.project.lexakai.diagrams;
}

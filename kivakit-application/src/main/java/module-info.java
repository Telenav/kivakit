open module kivakit.application
{
    // KivaKit
    requires transitive kivakit.configuration;
    requires transitive kivakit.commandline;

    // Module exports
    exports com.telenav.kivakit.application;
    exports com.telenav.kivakit.application.component;
    exports com.telenav.kivakit.application.project;
    exports com.telenav.kivakit.application.project.lexakai.diagrams;
}

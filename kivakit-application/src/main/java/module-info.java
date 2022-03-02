open module kivakit.application
{
    // KivaKit
    requires transitive kivakit.component;
    requires transitive kivakit.commandline;
    requires transitive kivakit.interfaces;

    // Lexakai
    requires transitive lexakai.annotations;

    // Module exports
    exports com.telenav.kivakit.application;
    exports com.telenav.kivakit.application.project.lexakai;
}

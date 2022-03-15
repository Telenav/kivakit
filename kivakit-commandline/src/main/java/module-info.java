open module kivakit.commandline
{
    // KivaKit
    requires transitive kivakit.core;
    requires transitive kivakit.collections;
    requires transitive kivakit.conversion;
    requires transitive kivakit.validation;

    // Lexakai
    requires lexakai.annotations;

    // Module exports
    exports com.telenav.kivakit.commandline;
    exports com.telenav.kivakit.commandline.parsing;
}

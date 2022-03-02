open module kivakit.network.core
{
    // KivaKit
    requires transitive kivakit.commandline;
    requires transitive kivakit.security;
    requires transitive kivakit.resource;
    requires transitive kivakit.conversion;

    // Module exports
    exports com.telenav.kivakit.network.core;
    exports com.telenav.kivakit.network.core.converters;
    exports com.telenav.kivakit.network.core.cluster;
    exports com.telenav.kivakit.network.core.project.lexakai;
    exports com.telenav.kivakit.network.core.authentication;
    exports com.telenav.kivakit.network.core.authentication.passwords;
}

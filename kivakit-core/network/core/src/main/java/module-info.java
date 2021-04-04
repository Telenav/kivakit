open module kivakit.core.network.core
{
    requires transitive kivakit.core.commandline;
    requires transitive kivakit.core.security;
    requires transitive kivakit.core.resource;

    exports com.telenav.kivakit.core.network.core;
    exports com.telenav.kivakit.core.network.core.cluster;
    exports com.telenav.kivakit.core.network.core.project;
}

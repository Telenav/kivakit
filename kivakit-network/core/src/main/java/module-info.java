open module kivakit.network.core
{
    requires transitive kivakit.commandline;
    requires transitive kivakit.security;
    requires transitive kivakit.resource;

    exports com.telenav.kivakit.network.core;
    exports com.telenav.kivakit.network.core.converters;
    exports com.telenav.kivakit.network.core.cluster;
    exports com.telenav.kivakit.network.core.project;
    exports com.telenav.kivakit.network.core.project.lexakai.diagrams;
}

open module kivakit.web.jersey
{
    requires transitive kivakit.web.jetty;

    requires transitive jersey.common;
    requires transitive jersey.container.servlet.core;
    requires transitive jersey.server;

    exports com.telenav.kivakit.web.jersey;
    exports com.telenav.kivakit.web.jersey.project;
}

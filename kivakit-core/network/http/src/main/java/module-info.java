open module kivakit.core.network.http
{
    requires transitive kivakit.core.network.core;
    requires transitive kivakit.core.resource;

    requires transitive httpcore;
    requires transitive httpclient;

    exports com.telenav.kivakit.core.network.http;
    exports com.telenav.kivakit.core.network.http.secure;
    exports com.telenav.kivakit.core.network.http.project;
    exports com.telenav.kivakit.core.network.http.project.lexakai.diagrams;
}

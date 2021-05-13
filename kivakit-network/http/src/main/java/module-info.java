open module kivakit.network.http
{
    requires transitive kivakit.network.core;
    requires transitive kivakit.resource;

    requires transitive httpcore;
    requires transitive httpclient;

    exports com.telenav.kivakit.network.http;
    exports com.telenav.kivakit.network.http.secure;
    exports com.telenav.kivakit.network.http.project;
    exports com.telenav.kivakit.network.http.project.lexakai.diagrams;
}

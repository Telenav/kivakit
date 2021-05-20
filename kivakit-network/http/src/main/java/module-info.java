open module kivakit.network.http
{
    requires transitive kivakit.network.core;
    requires transitive kivakit.resource;

    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;

    exports com.telenav.kivakit.network.http;
    exports com.telenav.kivakit.network.http.secure;
    exports com.telenav.kivakit.network.http.project;
    exports com.telenav.kivakit.network.http.project.lexakai.diagrams;
}

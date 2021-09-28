open module kivakit.network.http
{
    // KivaKit
    requires transitive kivakit.network.core;

    // HTTP
    requires transitive org.apache.httpcomponents.httpclient;
    requires transitive org.apache.httpcomponents.httpcore;

    // Module exports
    exports com.telenav.kivakit.network.http;
    exports com.telenav.kivakit.network.http.secure;
    exports com.telenav.kivakit.network.http.project.lexakai.diagrams;
}

open module kivakit.network.http
{
    // KivaKit
    requires transitive kivakit.network.core;
    requires transitive kivakit.resource;

    provides com.telenav.kivakit.resource.spi.ResourceResolver
            with com.telenav.kivakit.network.http.HttpGetResourceResolver;

    // HTTP
    requires java.net.http;

    // Module exports
    exports com.telenav.kivakit.network.http;
    exports com.telenav.kivakit.network.http.secure;
    exports com.telenav.kivakit.network.http.lexakai;
}

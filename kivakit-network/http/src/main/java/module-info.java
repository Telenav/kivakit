import com.telenav.kivakit.network.http.HttpGetResourceResolver;
import com.telenav.kivakit.resource.spi.ResourceResolver;

open module kivakit.network.http
{
    // KivaKit
    requires transitive kivakit.network.core;

    provides ResourceResolver with HttpGetResourceResolver;

    // HTTP
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;

    // Module exports
    exports com.telenav.kivakit.network.http;
    exports com.telenav.kivakit.network.http.secure;
    exports com.telenav.kivakit.network.http.project.lexakai.diagrams;
}

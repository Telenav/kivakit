open module kivakit.core.serialization.jersey.json
{
    requires transitive kivakit.core.serialization.json;

    requires transitive java.ws.rs;
    requires transitive java.xml.bind;

    exports com.telenav.kivakit.core.serialization.jersey.json;
}

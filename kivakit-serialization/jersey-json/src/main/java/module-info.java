open module kivakit.serialization.jersey.json
{
    requires transitive kivakit.kernel;
    requires transitive kivakit.serialization.json;

    requires transitive java.ws.rs;
    requires transitive java.xml.bind;

    exports com.telenav.kivakit.serialization.jersey.json;
    exports com.telenav.kivakit.serialization.jersey.json.project;
}

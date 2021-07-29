open module kivakit.serialization.jersey.json
{
    requires transitive java.ws.rs;

    requires transitive kivakit.serialization.json;

    exports com.telenav.kivakit.serialization.jersey.json;
    exports com.telenav.kivakit.serialization.jersey.json.project;
}

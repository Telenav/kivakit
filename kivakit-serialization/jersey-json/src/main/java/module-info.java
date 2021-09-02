open module kivakit.serialization.jersey.json
{
    // KivaKit
    requires transitive kivakit.serialization.json;

    // Jersey
    requires transitive java.ws.rs;
    requires transitive java.activation;

    // Module exports
    exports com.telenav.kivakit.serialization.jersey.json;
    exports com.telenav.kivakit.serialization.jersey.json.project;
}

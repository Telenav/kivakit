open module kivakit.serialization.jersey.json
{
    // KivaKit
    requires transitive kivakit.serialization.json;

    // Jersey bindings
    requires transitive java.ws.rs;

    // Module exports
    exports com.telenav.kivakit.serialization.jersey.json;
    exports com.telenav.kivakit.serialization.jersey.json.project;
}

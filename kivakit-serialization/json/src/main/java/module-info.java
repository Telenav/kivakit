open module kivakit.serialization.json
{
    // KivaKit
    requires transitive kivakit.kernel;

    // JSON
    requires transitive gson;

    // Module exports
    exports com.telenav.kivakit.serialization.json;
    exports com.telenav.kivakit.serialization.json.project;
    exports com.telenav.kivakit.serialization.json.serializers;
}

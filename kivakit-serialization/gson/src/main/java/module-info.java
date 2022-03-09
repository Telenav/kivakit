open module kivakit.serialization.gson
{
    // KivaKit
    requires transitive kivakit.conversion;

    // JSON
    requires gson;
    requires kivakit.core;
    requires kivakit.resource;

    // Module exports
    exports com.telenav.kivakit.serialization.gson;
    exports com.telenav.kivakit.serialization.gson.serializers;
}

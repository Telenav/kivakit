open module kivakit.serialization.gson
{
    // KivaKit
    requires transitive kivakit.conversion;

    // JSON
    requires com.google.gson;
    requires kivakit.core;
    requires kivakit.resource;

    // Module exports
    exports com.telenav.kivakit.serialization.gson;
    exports com.telenav.kivakit.serialization.gson.serializers;
    exports com.telenav.kivakit.serialization.gson.factory;
}

open module kivakit.serialization.json
{
    requires transitive kivakit.kernel;

    requires transitive gson;

    exports com.telenav.kivakit.serialization.json;
    exports com.telenav.kivakit.serialization.json.project;
    exports com.telenav.kivakit.serialization.json.serializers;
}

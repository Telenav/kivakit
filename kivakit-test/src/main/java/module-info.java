open module kivakit.test
{
    // KivaKit
    requires transitive kivakit.kernel;

    // Test
    requires junit;

    // Module exports
    exports com.telenav.kivakit.test.annotations;
    exports com.telenav.kivakit.test.project.lexakai.diagrams;
    exports com.telenav.kivakit.test.random;
    exports com.telenav.kivakit.test.reporters;
    exports com.telenav.kivakit.test;
}

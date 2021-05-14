open module kivakit.test
{
    requires transitive kivakit.kernel;

    requires transitive junit;

    exports com.telenav.kivakit.test.annotations;
    exports com.telenav.kivakit.test.project.lexakai.diagrams;
    exports com.telenav.kivakit.test.project;
    exports com.telenav.kivakit.test.random;
    exports com.telenav.kivakit.test.reporters;
    exports com.telenav.kivakit.test;
}

open module kivakit.core.test
{
    requires transitive kivakit.core.kernel;

    requires transitive junit;

    exports com.telenav.kivakit.core.test;
    exports com.telenav.kivakit.core.test.annotations;
    exports com.telenav.kivakit.core.test.random;
    exports com.telenav.kivakit.core.test.reporters;
}

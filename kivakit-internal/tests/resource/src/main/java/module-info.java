/**
 * This module-info does nothing, but must be present or the maven-compile-plugin
 * will refuse to compile the tests (which contains the version that is actually
 * used).
 */
open module kivakit.resource.tests
{
    requires kivakit.core;
    requires kivakit.test;
    requires kivakit.resource;
}

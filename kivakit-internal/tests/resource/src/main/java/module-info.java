/**
 * This module-info does nothing, but must be present or the maven-compile-plugin
 * will refuse to compile the tests (which contains the version that is actually
 * used).
 */
open module kivakit.internal.tests.resource
{
    requires kivakit.core;
    requires kivakit.internal.testing;
    requires kivakit.resource;
}

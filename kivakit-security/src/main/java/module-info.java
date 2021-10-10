open module kivakit.security
{
    // KivaKit
    requires transitive kivakit.test;

    requires junit;

    // Module exports
    exports com.telenav.kivakit.security.authentication;
    exports com.telenav.kivakit.security.authentication.passwords;
    exports com.telenav.kivakit.security.digest.digesters;
    exports com.telenav.kivakit.security.digest;
    exports com.telenav.kivakit.security.project.lexakai.diagrams;
    exports com.telenav.kivakit.security;
}

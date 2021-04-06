import com.telenav.kivakit.core.kernel.logging.Log;
import com.telenav.kivakit.logs.file.FileLog;

open module kivakit.logs.file
{
    requires transitive kivakit.core.resource;

    provides Log with FileLog;

    exports com.telenav.kivakit.logs.file;
}

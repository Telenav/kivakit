import com.telenav.kivakit.core.kernel.logging.Log;
import com.telenav.kivakit.logs.email.EmailLog;

open module kivakit.logs.email
{
    requires transitive kivakit.core.network.email;

    provides Log with EmailLog;

    exports com.telenav.kivakit.logs.email;
}

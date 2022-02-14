package kernel.messaging;

import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.language.traits.OperationTrait;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import org.junit.Test;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;

public class ResultTest extends BaseRepeater implements OperationTrait
{
    @Test
    public void test()
    {
        try
        {
            ensureEqual(result(this::maybe)
                    .or(this::failure)
                    .getOrThrow(), 7);
        }
        catch (Exception ignored)
        {
        }
    }

    private Integer failure()
    {
        problem("Failed");
        return null;
    }

    private Integer maybe()
    {
        if (Time.now().asSeconds() % 2 == 0)
        {
            problem("Maybe failed");
            return null;
        }

        return 7;
    }
}

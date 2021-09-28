package kernel.data.validation;

import com.telenav.kivakit.kernel.data.validation.BaseValidator;
import com.telenav.kivakit.kernel.data.validation.Validatable;
import com.telenav.kivakit.kernel.data.validation.ValidationType;
import com.telenav.kivakit.kernel.data.validation.Validator;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.listeners.MessageList;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.kernel.messaging.messages.status.Warning;
import org.junit.Test;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;

public class ValidationTest
{
    static class LaserCannon implements Validatable
    {
        private final int rounds;

        public LaserCannon(final int rounds)
        {
            this.rounds = rounds;
        }

        @Override
        public Validator validator(final ValidationType type)
        {
            return new BaseValidator()
            {
                @Override
                protected void onValidate()
                {
                    problemIf(rounds < 3, "Cannot use laser cannon with less than 3 rounds");
                }
            };
        }
    }

    static class Spaceship implements Validatable
    {
        private final String name;

        private final int laserCannons;

        public Spaceship(final String name, final int laserCannons)
        {
            this.name = name;
            this.laserCannons = laserCannons;
        }

        @Override
        public Validator validator(final ValidationType type)
        {
            return new BaseValidator()
            {
                @Override
                protected void onValidate()
                {
                    problemIf(name == null, "Spaceship must have a name");
                    problemIf(laserCannons < 3, "Spaceship cannot invade without at least 3 laser cannons");
                }
            };
        }
    }

    static class Turret implements Validatable
    {
        private final Listener listener;

        private final String name;

        private final LaserCannon cannon;

        public Turret(Listener listener, final String name, final LaserCannon cannon)
        {
            this.listener = listener;
            this.name = name;
            this.cannon = cannon;
        }

        @Override
        public Validator validator(final ValidationType type)
        {
            return new BaseValidator()
            {
                @Override
                protected void onValidate()
                {
                    problemIf(name == null, "Must give turret a cool name");
                    problemIf(!cannon.isValid(listener), "Invalid laser cannon");
                }
            };
        }
    }

    @Test
    public void testInvalid()
    {
        var zim = new Spaceship("ZIM's Cool Spaceship", 2);
        var capture = new MessageList();
        ensure(!zim.isValid(capture));
        ensure(capture.size() == 1);
        ensureEqual(capture.countWorseThanOrEqualTo(Message.Status.RESULT_INCOMPLETE), Count._1);
        ensureEqual(capture.count(Problem.class), Count._1);
        ensureEqual(capture.count(Warning.class), Count._0);
    }

    @Test
    public void testNestedInvalid()
    {
        var capture = new MessageList();
        var turret = new Turret(capture, null, new LaserCannon(1));
        ensure(!turret.isValid(capture));
        ensure(capture.size() == 3);
        ensureEqual(capture.countWorseThanOrEqualTo(Message.Status.RESULT_INCOMPLETE), Count._3);
        ensureEqual(capture.count(Problem.class), Count._3);
        ensureEqual(capture.count(Warning.class), Count._0);
    }

    @Test
    public void testNestedValid()
    {
        var capture = new MessageList();
        var turret = new Turret(capture, "Roger that", new LaserCannon(5));
        ensure(turret.isValid(capture));
        ensure(capture.size() == 0);
        ensureEqual(capture.countWorseThanOrEqualTo(Message.Status.RESULT_INCOMPLETE), Count._0);
        ensureEqual(capture.count(Problem.class), Count._0);
        ensureEqual(capture.count(Warning.class), Count._0);
    }

    @Test
    public void testValid()
    {
        var dib = new Spaceship("Dib's Cool Spaceship", 3);
        var capture = new MessageList();
        ensure(dib.isValid(capture));
        ensure(capture.size() == 0);
        ensureEqual(capture.countWorseThanOrEqualTo(Message.Status.RESULT_INCOMPLETE), Count._0);
        ensureEqual(capture.count(Problem.class), Count._0);
        ensureEqual(capture.count(Warning.class), Count._0);
    }
}

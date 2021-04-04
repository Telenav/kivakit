package com.telenav.kivakit.web.wicket.behaviors.status;

import com.telenav.kivakit.core.kernel.messaging.Message;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.IModel;

/**
 * @author jonathanl (shibo)
 */
public class MessageColor extends AttributeModifier
{
    public MessageColor(final IModel<Message.Status> status)
    {
        this(null, status);
    }

    public MessageColor(final String prefix, final IModel<Message.Status> model)
    {
        super("class", () ->
        {
            String className = null;
            final var status = model.getObject();
            if (status != null)
            {
                switch (status)
                {
                    case FAILED:
                    case PROBLEM:
                        className = "kivakit-problem";
                        break;

                    case RESULT_COMPROMISED:
                    case RESULT_INCOMPLETE:
                        className = "kivakit-warning";
                        break;

                    case SUCCEEDED:
                    case COMPLETED:
                    default:
                        className = "kivakit-information";
                        break;
                }
            }
            return className == null ? prefix : prefix != null ? prefix + " " + className : className;
        });
    }
}

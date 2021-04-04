////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.http.secure;

import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * Wrapper for a DefaultHttpClient which ignores all certificate errors. This client should be used when connecting to a
 * site which has an invalid certificate.
 *
 * @author ericg
 */
@SuppressWarnings("deprecation")
public class InvalidCertificateTrustingHttpClient extends DefaultHttpClient
{
    public InvalidCertificateTrustingHttpClient()
    {
        try
        {
            final var context = SSLContext.getInstance("TLS");
            final var trustManager = new X509TrustManager()
            {
                @Override
                public void checkClientTrusted(final X509Certificate[] xcs, final String string)
                {
                }

                @Override
                public void checkServerTrusted(final X509Certificate[] xcs, final String string)
                {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }
            };
            context.init(null, new TrustManager[] { trustManager }, null);

            final var socketFactory = new SSLSocketFactory(context, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            final var schemeRegistry = getConnectionManager().getSchemeRegistry();
            schemeRegistry.register(new Scheme("https", 443, socketFactory));
        }
        catch (final Exception e)
        {
            throw new Problem(e, "Unable to create trusting certificate").asException();
        }
    }
}

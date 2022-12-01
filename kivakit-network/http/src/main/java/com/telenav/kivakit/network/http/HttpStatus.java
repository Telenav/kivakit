////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.network.http;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.network.http.internal.lexakai.DiagramHttp;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.HashMap;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static java.util.Collections.synchronizedMap;

/**
 * The status of an HTTP request.
 *
 * <p><b>Tests</b></p>
 *
 * <ul>
 *     <li>{@link #isClientError()} </li>
 *     <li>{@link #isFailure()}</li>
 *     <li>{@link #isRedirect()}</li>
 *     <li>{@link #isServerError()}</li>
 *     <li>{@link #isSuccess()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramHttp.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public enum HttpStatus
{
    // --- 1xx Informational ---

    /** {@code 100 Continue} (HTTP/1.1 - RFC 2616) */
    CONTINUE(100),

    /** {@code 101 Switching Protocols} (HTTP/1.1 - RFC 2616) */
    SWITCHING_PROTOCOLS(101),

    /** {@code 102 Processing} (WebDAV - RFC 2518) */
    PROCESSING(102),

    // --- 2xx Success ---

    /** {@code 200 OK} (HTTP/1.0 - RFC 1945) */
    OK(200),

    /** {@code 201 Created} (HTTP/1.0 - RFC 1945) */
    CREATED(201),

    /** {@code 202 Accepted} (HTTP/1.0 - RFC 1945) */
    ACCEPTED(202),

    /** {@code 203 Non Authoritative Information} (HTTP/1.1 - RFC 2616) */
    NON_AUTHORITATIVE_INFORMATION(203),

    /** {@code 204 No Content} (HTTP/1.0 - RFC 1945) */
    NO_CONTENT(204),

    /** {@code 205 Reset Content} (HTTP/1.1 - RFC 2616) */
    RESET_CONTENT(205),

    /** {@code 206 Partial Content} (HTTP/1.1 - RFC 2616) */
    PARTIAL_CONTENT(206),

    /**
     * {@code 207 Multi-Status} (WebDAV - RFC 2518) or {@code 207 Partial Update OK} (HTTP/1.1 -
     * draft-ietf-http-v11-spec-rev-01?)
     */
    MULTI_STATUS(207),

    // --- 3xx Redirection ---

    /** {@code 300 Multiple Choices} (HTTP/1.1 - RFC 2616) */
    MULTIPLE_CHOICES(300),

    /** {@code 301 Moved Permanently} (HTTP/1.0 - RFC 1945) */
    MOVED_PERMANENTLY(301),

    /** {@code 302 Moved Temporarily} (Sometimes {@code Found}) (HTTP/1.0 - RFC 1945) */
    MOVED_TEMPORARILY(302),

    /** {@code 303 See Other} (HTTP/1.1 - RFC 2616) */
    SEE_OTHER(303),

    /** {@code 304 Not Modified} (HTTP/1.0 - RFC 1945) */
    NOT_MODIFIED(304),

    /** {@code 305 Use Proxy} (HTTP/1.1 - RFC 2616) */
    USE_PROXY(305),

    /** {@code 307 Temporary Redirect} (HTTP/1.1 - RFC 2616) */
    TEMPORARY_REDIRECT(307),

    // --- 4xx Client Error ---

    /** {@code 400 Bad Request} (HTTP/1.1 - RFC 2616) */
    BAD_REQUEST(400),

    /** {@code 401 Unauthorized} (HTTP/1.0 - RFC 1945) */
    UNAUTHORIZED(401),

    /** {@code 402 Payment Required} (HTTP/1.1 - RFC 2616) */
    PAYMENT_REQUIRED(402),

    /** {@code 403 Forbidden} (HTTP/1.0 - RFC 1945) */
    FORBIDDEN(403),

    /** {@code 404 Not Found} (HTTP/1.0 - RFC 1945) */
    NOT_FOUND(404),

    /** {@code 405 Method Not Allowed} (HTTP/1.1 - RFC 2616) */
    METHOD_NOT_ALLOWED(405),

    /** {@code 406 Not Acceptable} (HTTP/1.1 - RFC 2616) */
    NOT_ACCEPTABLE(406),

    /** {@code 407 Proxy Authentication Required} (HTTP/1.1 - RFC 2616) */
    PROXY_AUTHENTICATION_REQUIRED(407),

    /** {@code 408 Request Timeout} (HTTP/1.1 - RFC 2616) */
    REQUEST_TIMEOUT(408),

    /** {@code 409 Conflict} (HTTP/1.1 - RFC 2616) */
    CONFLICT(409),

    /** {@code 410 Gone} (HTTP/1.1 - RFC 2616) */
    GONE(410),

    /** {@code 411 Length Required} (HTTP/1.1 - RFC 2616) */
    LENGTH_REQUIRED(411),

    /** {@code 412 Precondition Failed} (HTTP/1.1 - RFC 2616) */
    PRECONDITION_FAILED(412),

    /** {@code 413 Request Entity Too Large} (HTTP/1.1 - RFC 2616) */
    REQUEST_TOO_LONG(413),

    /** {@code 414 Request-URI Too Long} (HTTP/1.1 - RFC 2616) */
    REQUEST_URI_TOO_LONG(414),

    /** {@code 415 Unsupported Media Type} (HTTP/1.1 - RFC 2616) */
    UNSUPPORTED_MEDIA_TYPE(415),

    /** {@code 416 Requested Range Not Satisfiable} (HTTP/1.1 - RFC 2616) */
    REQUESTED_RANGE_NOT_SATISFIABLE(416),

    /** {@code 417 Expectation Failed} (HTTP/1.1 - RFC 2616) */
    EXPECTATION_FAILED(417),

    /**
     * Static constant for a 419 error. {@code 419 Insufficient Space on Resource} (WebDAV -
     * draft-ietf-webdav-protocol-05?) or {@code 419 Proxy Re-authentication Required} (HTTP/1.1 drafts?)
     */
    INSUFFICIENT_SPACE_ON_RESOURCE(419),

    /**
     * Static constant for a 420 error. {@code 420 Method Failure} (WebDAV - draft-ietf-webdav-protocol-05?)
     */
    METHOD_FAILURE(420),

    /** {@code 422 Unprocessable Entity} (WebDAV - RFC 2518) */
    UNPROCESSABLE_ENTITY(422),

    /** {@code 423 Locked} (WebDAV - RFC 2518) */
    LOCKED(423),

    /** {@code 424 Failed Dependency} (WebDAV - RFC 2518) */
    FAILED_DEPENDENCY(424),

    // --- 5xx Server Error ---

    /** {@code 500 Server Error} (HTTP/1.0 - RFC 1945) */
    INTERNAL_SERVER_ERROR(500),

    /** {@code 501 Not Implemented} (HTTP/1.0 - RFC 1945) */
    NOT_IMPLEMENTED(501),

    /** {@code 502 Bad Gateway} (HTTP/1.0 - RFC 1945) */
    BAD_GATEWAY(502),

    /** {@code 503 Service Unavailable} (HTTP/1.0 - RFC 1945) */
    SERVICE_UNAVAILABLE(503),

    /** {@code 504 Gateway Timeout} (HTTP/1.1 - RFC 2616) */
    GATEWAY_TIMEOUT(504),

    /** {@code 505 HTTP Version Not Supported} (HTTP/1.1 - RFC 2616) */
    HTTP_VERSION_NOT_SUPPORTED(505),

    /** {@code 507 Insufficient Storage} (WebDAV - RFC 2518) */
    INSUFFICIENT_STORAGE(507);

    private static Map<Integer, HttpStatus> statusForCode;

    public static HttpStatus httpStatus(int statusCode)
    {
        var status = statusForCode.get(statusCode);
        if (status == null)
        {
            status = NOT_IMPLEMENTED;
        }
        return status;
    }

    private final int code;

    HttpStatus(int code)
    {
        this.code = code;
        statusForCode().put(code, this);
    }

    public int code()
    {
        return code;
    }

    public boolean isClientError()
    {
        return code >= 400 && code < 500;
    }

    /**
     * Returns true if this status code represents request failure
     */
    public boolean isFailure()
    {
        return !isSuccess();
    }

    public boolean isRedirect()
    {
        return code >= 300 && code < 400;
    }

    public boolean isServerError()
    {
        return code >= 500 && code < 600;
    }

    public boolean isSuccess()
    {
        return code >= 200 && code < 300;
    }

    @Override
    public String toString()
    {
        return Integer.toString(code);
    }

    private static Map<Integer, HttpStatus> statusForCode()
    {
        if (statusForCode == null)
        {
            statusForCode = synchronizedMap(new HashMap<>());
        }
        return statusForCode;
    }
}

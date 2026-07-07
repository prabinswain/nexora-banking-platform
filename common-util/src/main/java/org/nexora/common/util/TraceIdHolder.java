package org.nexora.common.util;

import org.slf4j.MDC;
import java.util.UUID;


/**
 * TraceIdHolder — reads the current request's trace ID from MDC.
 *
 * HOW IT WORKS:
 *   1. A filter (TraceIdFilter) runs on every request
 *   2. It puts a UUID into MDC: MDC.put("traceId", uuid)
 *   3. Every log line automatically includes [traceId=abc-123]
 *   4. Every ApiResponse includes the same traceId in the body
 *
 * RESULT:
 *   User says "I got an error, here's my response: traceId=abc-123"
 *   You search Kibana/ELK for traceId=abc-123 → see every log line for that request
 *   Debug in 30 seconds instead of 30 minutes.
 */

public class TraceIdHolder {

    private static final String TRACE_ID_KEY= "traceId";

    public static  String get(){
        String traceId = MDC.get(TRACE_ID_KEY);
        // Fallback: generate one if not set (shouldn't happen in production)
        return  traceId!= null ? traceId : UUID.randomUUID().toString();
    }

    public static void set(String traceId ){
        MDC.put(TRACE_ID_KEY,traceId);
    }

    public static void clear(){
        MDC.remove(TRACE_ID_KEY);
    }

}

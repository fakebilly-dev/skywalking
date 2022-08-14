package org.apache.skywalking.apm.plugin.monet.log.utils;

/**
 * TraceIdUtil
 * @author FakeBilly
 * @version V1.0.0
 * @github https://github.com/fakebilly-dev/skywalking
 **/
public class TraceIdUtil {

    public static String getTraceId(String traceId) {
        if (null == traceId || "".equals(traceId) || "N/A".equals(traceId) || "Ignored_Trace".equals(traceId)) {
            return "";
        }
        return traceId;
    }

}

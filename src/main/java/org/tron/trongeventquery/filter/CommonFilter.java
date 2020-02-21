package org.tron.trongeventquery.filter;


import com.alibaba.fastjson.JSONObject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@WebFilter(urlPatterns = "/*")
public class CommonFilter implements Filter {


    public  static String TOTAL_REQUST = "TOTAL_REQUEST";
    public  static String FAIL_REQUST = "FAIL_REQUEST";
    public  static String FAIL4XX_REQUST = "FAIL4XX_REQUEST";
    public  static String FAIL5XX_REQUST = "FAIL5XX_REQUEST";
    public  static String OK_REQUST = "OK_REQUEST";
    private static int totalCount = 0;
    private static int failCount = 0;
    private static int count4xx=0;
    private static int count5xx=0;
    private static int okCount=0;
    private static int interval = 1440;      // 24 hour
    private static HashMap<String, JSONObject> EndpointCount = new HashMap<String, JSONObject>();
    public String END_POINT = "END_POINT";
    public long gapMilliseconds = interval * 60 * 1000;
    private long preciousTime = 0;

    public int getTotalCount() {
        return this.totalCount;
    }

    public int getInterval() {
        return this.interval;
    }

    public int getFailCount() {
        return this.failCount;
    }

    public int getOkCount() { return this.okCount; }

    public int getCount4xx() {
        return count4xx;
    }
    public int getCount5xx() {
        return count5xx;
    }

    public HashMap<String, JSONObject> getEndpointMap() {
        return this.EndpointCount;
    }

    public CommonFilter getInstance() { return this; }

    @Override public void init(FilterConfig filterConfig) throws ServletException {
        // code here
        preciousTime = System.currentTimeMillis();
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
//        System.out.println("--------------doFilter start--------------");
        long currentTime = System.currentTimeMillis();
        if (currentTime - preciousTime > gapMilliseconds) {   //reset every 1 minutes
            totalCount = 0;
            failCount = 0;
            count4xx=0;
            count5xx=0;
            okCount=0;
            preciousTime = currentTime;
            EndpointCount.clear();
        }

        if (request instanceof HttpServletRequest) {
            String endpoint = ((HttpServletRequest) request).getRequestURI();
            JSONObject obj = new JSONObject();
            if (EndpointCount.containsKey(endpoint)) {
                obj = EndpointCount.get(endpoint);
            } else {
                obj.put(TOTAL_REQUST, 0);
                obj.put(FAIL_REQUST, 0);
                obj.put(FAIL4XX_REQUST, 0);
                obj.put(FAIL5XX_REQUST, 0);
                obj.put(OK_REQUST, 0);
                obj.put(END_POINT, endpoint);
            }
            obj.put(TOTAL_REQUST, (int) obj.get(TOTAL_REQUST) + 1);
            totalCount++;
            System.out.println("--response:"+response);
            try {
                chain.doFilter(request, response);
                HttpServletResponse resp = (HttpServletResponse) response;
//                System.out.println("--status:"+resp.getStatus());
//                System.out.println("--response:"+response);
                if (resp.getStatus() != 200) {
                    failCount++;
                    count5xx=failCount-count4xx;
                    obj.put(FAIL_REQUST, (int) obj.get(FAIL_REQUST) + 1);
                    obj.put(FAIL5XX_REQUST, (int) obj.get(FAIL_REQUST)-(int) obj.get(FAIL4XX_REQUST));
                }
                if (resp.getStatus() < 500 & resp.getStatus() > 399) {
                    count4xx++;
                    count5xx=failCount-count4xx;
                    obj.put(FAIL4XX_REQUST, (int) obj.get(FAIL4XX_REQUST) + 1);
                    obj.put(FAIL5XX_REQUST, (int) obj.get(FAIL_REQUST)-(int) obj.get(FAIL4XX_REQUST));
                }
            } catch (Exception e) {
                failCount++;
                count5xx=failCount-count4xx;
                obj.put(FAIL_REQUST, (int) obj.get(FAIL_REQUST) + 1);
                obj.put(FAIL5XX_REQUST, (int) obj.get(FAIL_REQUST)-(int) obj.get(FAIL4XX_REQUST));
                throw e;
            }
            // update map
            EndpointCount.put(endpoint, obj);
        } else {
            chain.doFilter(request, response);
        }
//        System.out.println("TOTAL_REQUST:"+ totalCount);
//        System.out.println("FAIL_REQUST:"+failCount);
//        System.out.println("--------------doFilter stop--------------");
    }

    @Override
    public void destroy() {
    }
}
package org.wm.apilab.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.core.MethodParameter;
import org.springframework.core.NamedThreadLocal;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.wm.apilab.utils.JSONUtils;

@Component
public class TimeInterceptor extends HandlerInterceptorAdapter {

    private final NamedThreadLocal<Long> startTimeThreadLocal =
            new NamedThreadLocal<>("startTimeThreadLocal");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        System.out.println(">>>>>>>Time Interceptor PreHandle");

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 取得 handler 信息
        System.out.println("handler class：" + handlerMethod.getBeanType().getName());
        System.out.println("handler method：" + handlerMethod.getMethod().getName());

        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
        for (MethodParameter methodParameter : methodParameters) {
            String parameterName = methodParameter.getParameterName();
            // 只能取得慘數名，取不到慘數值
            // System.out.println("parameterName: " + parameterName);
        }
        
        if (handlerMethod.getMethod().getName().equals("error")) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getOutputStream().println(JSONUtils.fillResultString(500, "Internal Server Error!!!", JSONObject.NULL));

            throw new Exception();
        }

        // 寫入系統時間 threadLocal
        startTimeThreadLocal.set(System.currentTimeMillis());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        System.out.println(">>>>>>>Time Interceptor PostHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) throws Exception {

        Long startTime = startTimeThreadLocal.get();
        long endTime = System.currentTimeMillis();

        System.out.println(">>>>>>>Time Interceptor Consume " + (endTime - startTime) + " ms");

        System.out.println(">>>>>>>Time Interceptor AfterCompletion");
    }
}

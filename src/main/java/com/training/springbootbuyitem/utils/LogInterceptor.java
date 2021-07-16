package com.training.springbootbuyitem.utils;

import com.training.springbootbuyitem.constant.BuyItemConstant;
import com.training.springbootbuyitem.constant.ItemStorageConstant;
import com.training.springbootbuyitem.enums.EnumOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        MDC.put(ItemStorageConstant.OPERATION, EnumOperation.RestockItem.name());
        MDC.put(ItemStorageConstant.TRACE_ID, UUID.randomUUID().toString());
        log.info(String.format(BuyItemConstant.LOGGING_HANDLER_INBOUND_MSG, request.getMethod(), request.getRequestURL(), LocalDate.now().toString()));

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

        log.info(String.format(BuyItemConstant.LOGGING_HANDLER_OUTBOUND_MSG, response.getStatus(), request.getRequestURL(), LocalDate.now().toString()));
        MDC.remove(ItemStorageConstant.TRACE_ID);
        MDC.remove(ItemStorageConstant.OPERATION);
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}

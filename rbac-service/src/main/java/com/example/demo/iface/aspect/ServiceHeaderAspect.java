package com.example.demo.iface.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.demo.infra.context.ContextHolder;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 攔截所有 Controller 的切面
 */
@Aspect
@Component
@Slf4j
public class ServiceHeaderAspect {

	/**
	 * 定義切入點，針對帶有 @RestController 注解的類進行切入。
	 */
	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void pointCut() {
	}

	@Around("pointCut()")
	public Object getServiceHeader(ProceedingJoinPoint joinPoint) throws Throwable {
		// 嘗試取得 Service 的請求頭
		HttpServletRequest request = getCurrentHttpRequest();

		if (request != null) {
			String serviceHeader = request.getHeader(ContextHolder.SERVICE_HEADER_KEY);
			log.info("serviceHeader: {}", serviceHeader);
			ContextHolder.setService(serviceHeader);
		} else {
			log.warn("無法取得 HttpServletRequest，可能不是 HTTP 請求上下文");
		}

		return joinPoint.proceed();
	}

	/**
	 * 取得目前的 HttpServletRequest
	 */
	private HttpServletRequest getCurrentHttpRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
			return servletRequestAttributes.getRequest();
		}
		return null;
	}
}

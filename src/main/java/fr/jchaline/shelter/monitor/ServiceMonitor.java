package fr.jchaline.shelter.monitor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceMonitor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceMonitor.class);
	
	@Around("execution(* fr.jchaline.shelter.service.WorldService.*(..))")
	public void logServiceAccess(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		long before = System.currentTimeMillis();
		proceedingJoinPoint.proceed();
		long after = System.currentTimeMillis();
		LOGGER.debug("Time for method {} : {} ms", proceedingJoinPoint.getSignature(), after - before);
	}
}

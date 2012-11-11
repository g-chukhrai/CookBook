package by.chuger.cookbook.model.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LoggingAspect {

    private static Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* by.chuger.cookbook.model.dao.*.*(..))")
    public void logMethodExecution(JoinPoint jp) {
        logger.info(jp.toShortString());
    }

}

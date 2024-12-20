package dev.start.init.annotation.impl;

import dev.start.init.annotation.Loggable;
import dev.start.init.logging.CustomLogger;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class MethodLogger {

    private final CustomLogger customLog = new CustomLogger();

    @Around("execution(* *(..)) && @annotation(loggable)")
    public Object log(final ProceedingJoinPoint joinPoint, final Loggable loggable) throws Throwable {

        var method = joinPoint.toShortString();
        var start = System.currentTimeMillis();

        switchStartingLogger(loggable.level(), method, joinPoint.getArgs());
        Object response = joinPoint.proceed();

        if (loggable.ignoreResponseData()) {
            switchFinishingLogger(loggable.level(), method, "{...}", start);
        } else {
            switchFinishingLogger(loggable.level(), method, response, start);
        }

        return response;
    }

    private void switchStartingLogger(final String level, final String method, final Object args) {
        final String format = "=> Starting -  {} args: {}";

        switch (level) {
            case "warn" -> LOG.warn(format, method, args);
            case "error" -> LOG.error(format, method, args);
            case "debug" -> LOG.debug(format, method, args);
            case "trace" -> LOG.trace(format, method, args);
            case "critical" -> customLog.critical(format, method, args);
            default -> LOG.info(format, method, args);
        }
    }

    private void switchFinishingLogger(String level, String method, Object response, long start) {
        final String format = "<= {} : {} - Finished, duration: {} ms";

        switch (level) {
            case "warn" -> LOG.warn(format, method, response, System.currentTimeMillis() - start);
            case "error" -> LOG.error(format, method, response, System.currentTimeMillis() - start);
            case "debug" -> LOG.debug(format, method, response, System.currentTimeMillis() - start);
            case "trace" -> LOG.trace(format, method, response, System.currentTimeMillis() - start);
            case "critical" -> customLog.critical(format, method, response, System.currentTimeMillis() - start);
            default -> LOG.info(format, method, response, System.currentTimeMillis() - start);
        }
    }
}


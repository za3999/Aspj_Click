package com.test.aspj;


import android.util.Log;
import android.view.View;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Method;

@Aspect
public class ClickPointAspect {

    private static final int INTERVAL = 1000;
    private long lastClickTime;
    private View lastClick;

    @Pointcut("execution(* android.view.View.OnClickListener+.onClick(..))")
    public void onclickMethod() {
    }

    @Pointcut("execution(void *..lambda*(android.view.View+))")
    public void onLambdaClickMethod() {
    }

    @Pointcut("execution(@com.test.aspj.ClickIgnore void *(..))")
    public void onClickIgnore() {
    }

    @Around("onclickMethod() || onLambdaClickMethod() || onClickIgnore()")
    public void aroundClickMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        long time = System.currentTimeMillis();
        StringBuilder builder = new StringBuilder();
        Class clazz = joinPoint.getSignature().getDeclaringType();
        builder.append("clazz:");
        builder.append(clazz);
        builder.append(",");
        String methodName = joinPoint.getSignature().getName();
        builder.append("methodName:");
        builder.append(methodName);
        builder.append(",");
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            builder.append("arg:");
            builder.append(arg.getClass().getName());
            builder.append(",");
        }
        Log.d("TEST1", "message " + builder.toString());
        if (args.length != 1 && !(args[0] instanceof View)) {
            joinPoint.proceed();
            return;
        }
        if (time - lastClickTime > INTERVAL) {
            joinPoint.proceed();
        } else {
            Log.d("TEST1", joinPoint.getSignature() + " ignore ");
        }
        lastClickTime = time;
    }

}

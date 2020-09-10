package com.test.aspj;


import android.util.Log;
import android.view.View;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ClickPointAspect {

    private final String TAG = getClass().getSimpleName();

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
        logMethod(joinPoint);
        checkClick(joinPoint);
    }

    private void logMethod(ProceedingJoinPoint joinPoint) {
        StringBuilder builder = new StringBuilder();
        Class clazz = joinPoint.getSignature().getDeclaringType();
        builder.append(clazz.getName());
        builder.append(".");
        String methodName = joinPoint.getSignature().getName();
        builder.append(methodName);
        builder.append("(");
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            builder.append(arg.getClass().getName());
            if (i < args.length - 1) {
                builder.append(",");
            }
        }
        builder.append(")");
        Log.d(TAG, "method " + builder.toString());
    }

    private void checkClick(ProceedingJoinPoint joinPoint) throws Throwable {
        long time = System.currentTimeMillis();
        Object[] args = joinPoint.getArgs();
        if (args.length != 1 && !(args[0] instanceof View)) {
            joinPoint.proceed();
            return;
        }
        View clickView = (View) args[0];
        if (time - lastClickTime > INTERVAL || lastClick != clickView) {
            joinPoint.proceed();
        } else {
            Log.d(TAG, joinPoint.getSignature() + " ignore ");
        }
        lastClickTime = time;
        lastClick = clickView;
    }

}

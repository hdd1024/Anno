package com.example.anno.listener.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@OnBaseListener(setListener = "setOnClickListener",
        setListenerClass = View.OnClickListener.class,
        setCallbackMethod = "onClick"
)
public @interface OnClick {
    int value();//组件id
}

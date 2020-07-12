package com.example.anno.listener.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnBaseListener {
    //事件三要素---方法名 setOnClickListener
    String setListener();

    //事件三要素---事件接口 OnClickListener
    Class setListenerClass();

    //事件三要素---事件执行的方法 onClick
    String setCallbackMethod();
}

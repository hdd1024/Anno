package com.example.anno.listener;

import android.util.Log;

import com.example.anno.listener.annotation.OnBaseListener;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectListener {
    private static String TAG = InjectListener.class.getSimpleName();
    private static InjectListener mInstance;
    public static InjectListener getInstance() {
        if (mInstance == null) {
            synchronized (InjectListener.class) {
                if (mInstance == null) {
                    mInstance = new InjectListener();
                }
            }
        }
        return mInstance;
    }
    public void init(Object activityObj) {
        Class<?> activityClass = activityObj.getClass();
        for (Method declaredMethod : activityClass.getDeclaredMethods()) {
            declaredMethod.setAccessible(true);
            findOnListener(activityObj, activityClass, declaredMethod);
        }


    }
    private void findOnListener(final Object taget, Class<?> activityClass, final Method listenerMethod) {
        //因为我们要拿的具体事件注解中的公共事件注解@OnBaseListener，一个类中可能有多个事件，事件有可能有不相同
        //因此我们需要通过getAnnotations()获取方法上的所有注解，然后通过annotationTyp来判断是否为@OnBaseListener注解过的
        //注解，如果是则取出其中的设置的值
        for (Annotation annotation : listenerMethod.getAnnotations()) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            OnBaseListener onBaseListener = annotationType.getAnnotation(OnBaseListener.class);
            if (onBaseListener == null) {
                continue;
            }
            //拿到事件三元素注解元素中的值
            String setListener = onBaseListener.setListener();
            Class setListenerClass = onBaseListener.setListenerClass();
            String setCallbackMethod = onBaseListener.setCallbackMethod();
            try {
                //因为不知道从是那个事件方法，因此要通过反射动态获取事件监听注解中的方法
                Method valueMthod = annotationType.getDeclaredMethod("value");
                //获取view中的id
                int value = (int) valueMthod.invoke(annotation);

                //根据id获取到view getMethod()不仅可以获取自己的方法，还能获取到父类中的方法
                Method findViewById = activityClass.getMethod("findViewById", int.class);
                Object view = findViewById.invoke(taget, value);
                Log.d(TAG, ">>>>>>>>>>>" + value);
                //设置view的事件监听 必须要用于getMethod()来获取view设置事件监听的方法，因为我们不知道当前控件的事件是不是
                //它自己定义的还是父类View以及View的事件，getMethod()不仅会把自己类中的返回返回，还会将父类中以及父类中父类
                // 的方法返回
                Method setListenerMethod = view.getClass().getMethod(setListener, setListenerClass);

                //使用动态代理技术，自动生成事件监听接口的实例
                Object proxy = Proxy.newProxyInstance(setListenerClass.getClassLoader(), new Class[]{setListenerClass},
                        new InvocationHandler() {
                            /**
                             *  在执行接口中的方法的时候，会回调这个invoke方法，内执行接口中的一个方法，就会回调一次
                             * @param o 很少用
                             * @param method 要执行接口中的方法，如果接口中有多个方法，那么此次调用invoke()，method
                             *               指向的方法就不同
                             * @param objects 执行方法的参数
                             * @return 返回执行方法的结果
                             * @throws Throwable 异常
                             */
                            @Override
                            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                                //因为 监听函数只有一个方法，因此代理的invoke之会执行一次，我们不需要执行method方法
                                //因为我们要执行自己自定义的方法，下面的返回结果也就是我们自定义的返回结果
                                return listenerMethod.invoke(taget, null);
                            }
                        });
                //设置监听
                setListenerMethod.invoke(view, proxy);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

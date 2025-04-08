package me.traduciendo.bunkers.utils;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@SuppressWarnings("all")
public class ReflectionUtils {

    @NotNull // Just to deny those filthy warnings
    public static Field accessField(Class<?> object, String name) {
        try {

            Field field = object.getDeclaredField(name);
            field.setAccessible(true);
            return field;

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    @NotNull // Just to deny those filthy warnings
    public static Method accessMethod(Class<?> object, String name, Class<?>... params) {
        try {

            Method method = object.getMethod(name, params);
            method.setAccessible(true);
            return method;

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    @NotNull // Just to deny those filthy warnings
    public static Constructor<?> accessConstructor(Class<?> object, int index) {
        Constructor<?> constructor = object.getDeclaredConstructors()[index];
        constructor.setAccessible(true);
        return constructor;
    }
}
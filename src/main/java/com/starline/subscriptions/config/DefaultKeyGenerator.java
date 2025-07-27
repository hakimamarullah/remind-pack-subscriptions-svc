package com.starline.subscriptions.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.core.KotlinDetector;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Component
public class DefaultKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return simpleHash(target.getClass().getCanonicalName()) + "::"
                + method.getName() + "::"
                + generateKey((KotlinDetector.isSuspendingFunction(method) ?
                Arrays.copyOf(params, params.length - 1) : params));
    }

    /**
     * Generate a key based on the specified parameters.
     */
    public static Object generateKey(Object... params) {
        if (params.length == 0) {
            return DefaultKey.EMPTY;
        }
        if (params.length == 1) {
            Object param = params[0];
            if (param != null && !param.getClass().isArray()) {
                return param;
            }
        }
        return new DefaultKey(params);
    }

    public static int simpleHash(String input) {
        int hash = 0;
        for (int i = 0; i < input.length(); i++) {
            hash = 31 * hash + input.charAt(i);
        }
        return hash;
    }

}


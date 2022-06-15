//Copyright 2022 Mindera
//SPDX-License-Identifier: Apache-2.0

package mindera.logfilter.service;

import java.util.HashMap;
import java.util.Map;

public enum SpringlessHttpMethod {
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE;

    private static final Map<String, SpringlessHttpMethod> mappings = new HashMap(16);

    private SpringlessHttpMethod() {
    }

    public static SpringlessHttpMethod resolve(/*@Nullable*/ String method) {
        return method != null ? (SpringlessHttpMethod)mappings.get(method) : null;
    }

    public boolean matches(String method) {
        return this.name().equals(method);
    }

    static {
        SpringlessHttpMethod[] var0 = values();
        int var1 = var0.length;

        for(int var2 = 0; var2 < var1; ++var2) {
            SpringlessHttpMethod httpMethod = var0[var2];
            mappings.put(httpMethod.name(), httpMethod);
        }

    }
}
package com.example.demowithtests.util;

import org.hibernate.resource.jdbc.spi.StatementInspector;

public class HibernateQueryInterceptor implements StatementInspector {
    private static final ThreadLocal<Integer> queryCountThreadLocal = ThreadLocal.withInitial(() -> 0);

    public static void resetQueryCount() {
        queryCountThreadLocal.set(0);
    }

    public static int getQueryCount() {
        return queryCountThreadLocal.get();
    }

    @Override
    public String inspect(String sql) {
        queryCountThreadLocal.set(queryCountThreadLocal.get() + 1);
        return sql;
    }
}

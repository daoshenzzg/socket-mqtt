package com.yb.socket.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author daoshenzzg@163.com
 * @date 2018/12/30 18:31
 */
public class Sequence {
    private static Sequence instance = new Sequence();
    private Map<String, AtomicInteger> sequences = new ConcurrentHashMap<>();

    private Sequence() {
    }

    public static Sequence getInstance() {
        return instance;
    }

    public Integer addAndGet(String sequenceName) {
        return addAndGet(sequenceName, 1);
    }

    public Integer addAndGet(String sequenceName, int step) {
        if (StringUtils.isBlank(sequenceName)) {
            return null;
        }

        AtomicInteger sequence = null;
        if (sequences.get(sequenceName) == null) {
            sequence = new AtomicInteger();
            sequences.put(sequenceName, sequence);
        } else {
            sequence = sequences.get(sequenceName);
        }

        return sequence.addAndGet(step);
    }
}

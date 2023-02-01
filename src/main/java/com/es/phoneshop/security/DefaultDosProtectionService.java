package com.es.phoneshop.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService {
    private static DosProtectionService instance;

    private static final long THRESHOLD = 20L;

    private Map<String, Long> countMap = new ConcurrentHashMap<>();

    private DefaultDosProtectionService() {

    }

    public static DosProtectionService getInstance() {
        if (instance == null) {
            instance = new DefaultDosProtectionService();
        }
        return instance;
    }

    @Override
    public boolean isAllowed(String ip) {
        Long count = countMap.get(ip);
        if (count == null) {
            count = 1L;
        } else {
            if (count > THRESHOLD) {
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                countMap.put(ip, 0L);
                            }
                        },
                        5000
                );
                return false;
            }
            count++;
        }
        countMap.put(ip, count);
        return true;
    }
}

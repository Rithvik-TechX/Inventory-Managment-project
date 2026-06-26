package com.inventory.database_system.util;

import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ReferenceIdGenerator {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    private final AtomicInteger counter = new AtomicInteger(1000);

    /**
     * Generates IDs like: TXN-202503291435-1001
     */
    public String generate(String prefix) {
        String timestamp = LocalDateTime.now().format(FMT);
        int seq = counter.incrementAndGet();
        return prefix.toUpperCase() + "-" + timestamp + "-" + seq;
    }

    public String generateTransactionRef() {
        return generate("TXN");
    }
}

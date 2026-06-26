package com.inventory.database_system.util;

import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class SKUGenerator {

    /**
     * Generates an SKU like: CAT-PROD-XXXX
     * e.g. ELEC-LAPT-A3F2
     */
    public String generate(String categoryPrefix, String productPrefix) {
        String cat = sanitize(categoryPrefix, 4);
        String prod = sanitize(productPrefix, 4);
        String unique = UUID.randomUUID().toString().replace("-", "").substring(0, 4).toUpperCase();
        return cat + "-" + prod + "-" + unique;
    }

    public String generateSimple() {
        return "SKU-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }

    private String sanitize(String input, int maxLen) {
        if (input == null || input.isBlank()) return "GEN";
        return input.replaceAll("[^A-Za-z0-9]", "").toUpperCase()
                .substring(0, Math.min(input.length(), maxLen));
    }
}

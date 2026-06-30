package com.inventory;

import com.inventory.repository.CategoryRepository;
import com.inventory.repository.ProductRepository;
import com.inventory.repository.SupplierRepository;
import com.inventory.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class IntegratedApplication {
    private static final Logger logger = LoggerFactory.getLogger(IntegratedApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(IntegratedApplication.class, args);
    }

    @Bean
    CommandLineRunner dataHealthCheck(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            SupplierRepository supplierRepository,
            UserRepository userRepository) {
        return args -> {
            long products = productRepository.count();
            long categories = categoryRepository.count();
            long suppliers = supplierRepository.count();
            long users = userRepository.count();

            logger.info("=================================================");
            logger.info("  INVENTTRACK STARTUP DATA HEALTH CHECK");
            logger.info("  Products:   {}", products);
            logger.info("  Categories: {}", categories);
            logger.info("  Suppliers:  {}", suppliers);
            logger.info("  Users:      {}", users);
            if (products == 0 && categories == 0) {
                logger.warn("  Database appears empty. Check datasource configuration.");
            }
            logger.info("=================================================");
        };
    }
}

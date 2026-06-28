package com.inventory.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}") private String fromAddress;
    @Value("${app.admin.email}") private String adminEmail;
    @Value("${app.frontend.url:http://localhost:3000}") private String frontendUrl;

    public EmailService(JavaMailSender mailSender) { this.mailSender = mailSender; }

    @Async
    public void sendLowStockAlert(String productName, String sku, int currentStock,
                                  int reorderPoint, String categoryName, String recipientEmail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromAddress);
            helper.setTo(recipientEmail);
            helper.setSubject("Low Stock Alert — " + productName + " | InvenTrack");
            helper.setText(buildLowStockEmailHtml(productName, sku, currentStock, reorderPoint, categoryName), true);
            mailSender.send(message);
        } catch (Exception ex) {
            System.err.println("[Email] Failed to send low-stock alert to " + recipientEmail + ": " + ex.getMessage());
        }
    }

    public void sendLowStockAlertToMainAdmin(String productName, String sku, int currentStock,
                                             int reorderPoint, String categoryName) {
        sendLowStockAlert(productName, sku, currentStock, reorderPoint, categoryName, adminEmail);
    }

    @Async
    public void sendEmail(String recipient, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress); message.setTo(recipient); message.setSubject(subject); message.setText(body);
            mailSender.send(message);
        } catch (Exception ex) {
            System.err.println("[Email] Failed to send to " + recipient + ": " + ex.getMessage());
        }
    }

    private String buildLowStockEmailHtml(String productName, String sku, int currentStock,
                                          int reorderPoint, String categoryName) {
        return """
            <!doctype html><html><body style="margin:0;background:#F8F9FC;font-family:Inter,Arial,sans-serif">
            <div style="max-width:520px;margin:40px auto;background:#fff;border:1px solid #E5E7EB;border-radius:12px;overflow:hidden">
              <div style="padding:28px 32px;background:#4F46E5;color:#fff"><h1 style="margin:0;font-size:20px">Low Stock Alert</h1><p style="margin:4px 0 0;opacity:.8">InvenTrack Inventory Management</p></div>
              <div style="padding:32px"><div style="padding:16px 20px;border:1px solid #FDE68A;border-radius:8px;background:#FEF3C7;color:#92400E">This product has reached its reorder point and needs attention.</div>
                <div style="margin-top:24px;padding:20px;border:1px solid #E5E7EB;border-radius:8px"><h2 style="margin:0 0 4px;color:#111827;font-size:18px">%s</h2><p style="margin:0 0 16px;color:#6B7280;font-family:monospace">SKU: %s · Category: %s</p><p style="color:#EF4444;font-size:22px;font-weight:700">%d units <span style="color:#9CA3AF;font-size:12px">current stock</span></p><p style="color:#6B7280">Reorder point: %d units</p></div>
                <a href="%s/app/products" style="display:inline-block;margin-top:20px;padding:10px 24px;border-radius:8px;color:#fff;background:#4F46E5;text-decoration:none;font-weight:600">View Product in InvenTrack →</a>
              </div><div style="padding:20px 32px;border-top:1px solid #E5E7EB;text-align:center;color:#9CA3AF;font-size:12px">You received this because you are an InvenTrack administrator.<br>© 2026 InvenTrack</div>
            </div></body></html>
            """.formatted(productName, sku, categoryName, currentStock, reorderPoint, frontendUrl);
    }
}

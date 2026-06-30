package com.inventory.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

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
            logger.info("Low-stock email sent successfully to {}", recipientEmail);
        } catch (Exception ex) {
            logger.error("Failed to send low-stock email to {}: {}", recipientEmail, ex.getMessage());
        }
    }

    public void sendLowStockAlertToMainAdmin(String productName, String sku, int currentStock,
                                             int reorderPoint, String categoryName) {
        sendLowStockAlert(productName, sku, currentStock, reorderPoint, categoryName, adminEmail);
    }

    @Async
    public void sendPasswordResetEmail(String recipientEmail, String resetToken, String frontendUrl) {
        try {
            String resetLink = frontendUrl + "/reset-password?token=" + resetToken;
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromAddress);
            helper.setTo(recipientEmail);
            helper.setSubject("Reset your InvenTrack password");
            String html = """
                <!doctype html><html><body style="margin:0;background:#F8F9FC;font-family:Inter,Arial,sans-serif">
                <div style="max-width:480px;margin:40px auto;background:white;border:1px solid #E5E7EB;border-radius:12px;overflow:hidden">
                  <div style="background:#4F46E5;padding:24px 32px;text-align:center">
                    %s
                    <h1 style="margin:0;color:white;font-size:18px">InvenTrack</h1>
                  </div>
                  <div style="padding:32px;color:#374151;font-size:14px;line-height:1.6">
                    <p>Hello,</p><p>We received a request to reset your InvenTrack password. This link expires in 30 minutes.</p>
                    <div style="text-align:center"><a href="%s" style="display:inline-block;margin:20px 0;padding:12px 28px;border-radius:8px;background:#4F46E5;color:white;text-decoration:none;font-weight:600">Reset Password</a></div>
                    <p style="margin-top:24px;color:#9CA3AF;font-size:12px">If you did not request this, you can safely ignore this email. Your password will not change.</p>
                  </div>
                  <div style="padding:20px 32px;border-top:1px solid #E5E7EB;text-align:center;color:#9CA3AF;font-size:12px">This email was sent by InvenTrack. Never share this link with anyone.<br>© 2026 InvenTrack</div>
                </div></body></html>
                """.formatted(emailLogo(), resetLink);
            helper.setText(html, true);
            mailSender.send(message);
            logger.info("Password reset email sent to {}", recipientEmail);
        } catch (Exception ex) {
            logger.error("Failed to send password reset email to {}: {}", recipientEmail, ex.getMessage());
        }
    }

    @Async
    public void sendEmail(String recipient, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress); message.setTo(recipient); message.setSubject(subject); message.setText(body);
            mailSender.send(message);
            logger.info("Email sent successfully to {}", recipient);
        } catch (Exception ex) {
            logger.error("Failed to send email to {}: {}", recipient, ex.getMessage());
        }
    }

    private String buildLowStockEmailHtml(String productName, String sku, int currentStock,
                                          int reorderPoint, String categoryName) {
        return """
            <!doctype html><html><body style="margin:0;background:#F8F9FC;font-family:Inter,Arial,sans-serif">
            <div style="max-width:520px;margin:40px auto;background:#fff;border:1px solid #E5E7EB;border-radius:12px;overflow:hidden">
              <div style="padding:28px 32px;background:#4F46E5;color:#fff;text-align:center">%s<h1 style="margin:8px 0 0;font-size:20px">Low Stock Alert</h1><p style="margin:4px 0 0;opacity:.8">InvenTrack Inventory Management</p></div>
              <div style="padding:32px"><div style="padding:16px 20px;border:1px solid #FDE68A;border-radius:8px;background:#FEF3C7;color:#92400E">This product has reached its reorder point and needs attention.</div>
                <div style="margin-top:24px;padding:20px;border:1px solid #E5E7EB;border-radius:8px"><h2 style="margin:0 0 4px;color:#111827;font-size:18px">%s</h2><p style="margin:0 0 16px;color:#6B7280;font-family:monospace">SKU: %s · Category: %s</p><p style="color:#EF4444;font-size:22px;font-weight:700">%d units <span style="color:#9CA3AF;font-size:12px">current stock</span></p><p style="color:#6B7280">Reorder point: %d units</p></div>
                <a href="%s/app/products" style="display:inline-block;margin-top:20px;padding:10px 24px;border-radius:8px;color:#fff;background:#4F46E5;text-decoration:none;font-weight:600">View Product in InvenTrack →</a>
              </div><div style="padding:20px 32px;border-top:1px solid #E5E7EB;text-align:center;color:#9CA3AF;font-size:12px">You received this because you are an InvenTrack administrator.<br>© 2026 InvenTrack</div>
            </div></body></html>
            """.formatted(emailLogo(), productName, sku, categoryName, currentStock, reorderPoint, frontendUrl);
    }

    private String emailLogo() {
        return """
            <svg width="40" height="40" viewBox="0 0 64 64" role="img" aria-label="InvenTrack" style="display:block;margin:0 auto">
              <rect width="64" height="64" rx="16" fill="#4F46E5" stroke="white" stroke-width="2"/>
              <g fill="none" stroke="white" stroke-width="3" stroke-linecap="round" stroke-linejoin="round">
                <path d="m18 24 14-8 14 8v17L32 49 18 41z"/>
                <path d="m18 24 14 8 14-8M32 32v17M25 20l14 8"/>
              </g>
            </svg>
            """;
    }
}

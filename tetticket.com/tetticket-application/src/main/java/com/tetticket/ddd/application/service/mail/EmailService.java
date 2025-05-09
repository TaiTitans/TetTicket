package com.tetticket.ddd.application.service.mail;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.transactional.SendContact;
import com.mailjet.client.transactional.SendEmailsRequest;
import com.mailjet.client.transactional.TransactionalEmail;
import com.mailjet.client.transactional.response.SendEmailsResponse;
import com.tetticket.ddd.domain.model.entity.Order;
import com.tetticket.ddd.domain.model.entity.OrderItem;
import com.tetticket.ddd.infrastructure.repository.EmailRepository;
import com.tetticket.ddd.infrastructure.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailParseException;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class EmailService implements EmailRepository {
    private final MailjetClient mailjetClient;
    private final OrderItemRepository orderItemRepository;
    public EmailService(
            @Value("${mailjet.api.key}") String apiKey,
            @Value("${mailjet.api.secret}") String secretKey, OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
        ClientOptions options = ClientOptions.builder()
                .apiKey(apiKey)
                .apiSecretKey(secretKey)
                .build();
        this.mailjetClient = new MailjetClient(options);
    }

    /**
     * Sends a one-time password (OTP) email to a specified recipient.
     *
     * @param to  the recipient's email address to which the OTP email will be sent
     * @param otp the one-time password to include in the email
     */
@Override
public void sendOTPEmail(String to, String otp) {
    try {
        TransactionalEmail message = createTransactionalEmail(to, otp);
        SendEmailsRequest request = SendEmailsRequest.builder()
                .message(message)
                .build();
        request.sendWith(this.mailjetClient);
    } catch (MailjetException e) {
        log.error("Error sending email: {}", e.getMessage());
        throw new MailParseException("Could not parse mail", e);
    }
}

private TransactionalEmail createTransactionalEmail(String to, String otp) {
    return TransactionalEmail.builder()
            .to(new SendContact(to))
            .from(new SendContact("mekongocop@gmail.com", "TetTicket"))
            .subject("Your OTP Code")
            .htmlPart(buildEmailContent(otp))
            .build();
}

private String buildEmailContent(String otp) {
    return "<html><body>" +
            "<table width='100%' cellpadding='0' cellspacing='0' style='background-color: #f5f5f5;'>" +
            "  <tr>" +
            "    <td align='center' style='padding: 20px 0;'>" +
            "      <img src='https://res.cloudinary.com/duwzchnsp/image/upload/v1725289359/qygz0ghl1ac3ajas4pny.png' alt='TetTicket' style='max-width: 200px;'>" +
            "    </td>" +
            "  </tr>" +
            "  <tr>" +
            "    <td align='center' style='padding: 20px;'>" +
            "      <h1 style='color: #333;'>Hello, customer!</h1>" +
            "      <p style='font-size: 16px;'>We've received a request to verify your identity. Your one-time password (OTP) is:</p>" +
            "      <p style='font-size: 24px; font-weight: bold; color: #007bff;'>" + otp + "</p>" +
            "      <p>Please enter this code on the verification page to complete the process.</p>" +
            "    </td>" +
            "  </tr>" +
            "</table>" +
            "</body></html>";
}

    public void sendEmail(String to, String subject, String htmlContent) {
        try {
            TransactionalEmail message = TransactionalEmail
                    .builder()
                    .to(new SendContact(to))
                    .from(new SendContact("mekongocop@gmail.com", "TetTicket"))
                    .subject(subject)
                    .htmlPart(htmlContent)
                    .build();

            SendEmailsRequest request = SendEmailsRequest
                    .builder()
                    .message(message)
                    .build();

            SendEmailsResponse response = request.sendWith(this.mailjetClient);
        } catch (MailjetException e) {
            log.error("Error sending email: {}", e.getMessage());
            throw new MailParseException("Could not parse mail", e);
        }
    }

    public String generateOrderConfirmationTemplate(Order order){
    List<OrderItem> orderItems = orderItemRepository.findByOrder_id(order.getId());
    StringBuilder itemsHtml = new StringBuilder();
    double total = 0;

        for (OrderItem item : orderItems) {
            total += item.getPrice() * item.getQuantity();
            itemsHtml.append(String.format("""
                <tr>
                    <td style='padding: 12px; border-bottom: 1px solid #ddd;'>%s</td>
                    <td style='padding: 12px; border-bottom: 1px solid #ddd;'>%d</td>
                    <td style='padding: 12px; border-bottom: 1px solid #ddd;'>$%.2f</td>
                    <td style='padding: 12px; border-bottom: 1px solid #ddd;'>$%.2f</td>
                </tr>
                """,
                    item.getTicket_id().getName(),
                    item.getQuantity(),
                    item.getPrice(),
                    item.getPrice() * item.getQuantity()
            ));
        }

        return """
                <html>
                       <body style='font-family: Arial, sans-serif; line-height: 1.6; color: #333;'>
                           <table width='100%' cellpadding='0' cellspacing='0' style='max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);'>
                               <tr>
                                   <td style='text-align: center; padding: 20px;'>
                                       <img src='https://res.cloudinary.com/duwzchnsp/image/upload/v1725289359/qygz0ghl1ac3ajas4pny.png' alt='TetTicket' style='max-width: 200px;'>
                                   </td>
                               </tr>
                               <tr>
                                   <td style='padding: 20px;'>
                                       <h1 style='color: #007bff; margin-bottom: 20px;'>Order Confirmation</h1>
                                       <p>Thank you for your order! Your order details are below:</p>
                
                                       <div style='background-color: #f8f9fa; padding: 15px; border-radius: 4px; margin: 20px 0;'>
                                           <p><strong>Order ID:</strong> #%d</p>
                                           <p><strong>Date:</strong> %s</p>
                                           <p><strong>Status:</strong> <span style='color: #28a745;'>%s</span></p>
                                       </div>
                
                                       <table style='width: 100%%; border-collapse: collapse; margin-top: 20px;'>
                                           <thead>
                                               <tr style='background-color: #f8f9fa;'>
                                                   <th style='padding: 12px; text-align: left;'>Ticket</th>
                                                   <th style='padding: 12px; text-align: left;'>Quantity</th>
                                                   <th style='padding: 12px; text-align: left;'>Price</th>
                                                   <th style='padding: 12px; text-align: left;'>Subtotal</th>
                                               </tr>
                                           </thead>
                                           <tbody>
                                               %s
                                           </tbody>
                                           <tfoot>
                                               <tr>
                                                   <td colspan='3' style='padding: 12px; text-align: right;'><strong>Total:</strong></td>
                                                   <td style='padding: 12px;'><strong>$%.2f</strong></td>
                                               </tr>
                                           </tfoot>
                                       </table>
                
                                       <div style='margin-top: 30px; padding: 20px; background-color: #f8f9fa; border-radius: 4px;'>
                                           <p style='margin: 0;'><strong>Need help?</strong> Contact our support team at support@tetticket.com</p>
                                       </div>
                                   </td>
                               </tr>
                               <tr>
                                   <td style='text-align: center; padding: 20px; background-color: #f8f9fa; border-radius: 0 0 8px 8px;'>
                                       <p style='margin: 0; color: #666;'>TetTicket - Your Trusted Ticket Platform</p>
                                   </td>
                               </tr>
                           </table>
                       </body>
                       </html>
            """.formatted(
                order.getId(),
                order.getCreated_at().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss")),
                order.getStatus().name(),
                itemsHtml.toString(),
                total
        );
    }
    }



package sn.esmt.msi.notification.dto;

import java.time.LocalDateTime;

public class NotificationDTO {

    public static class SendNotificationRequest {
        private String passNumber;
        private String recipientEmail;
        private String recipientPhone;
        private String subject;
        private String message;
        private NotificationType type;

        public String getPassNumber() { return passNumber; }
        public void setPassNumber(String passNumber) { this.passNumber = passNumber; }
        public String getRecipientEmail() { return recipientEmail; }
        public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }
        public String getRecipientPhone() { return recipientPhone; }
        public void setRecipientPhone(String recipientPhone) { this.recipientPhone = recipientPhone; }
        public String getSubject() { return subject; }
        public void setSubject(String subject) { this.subject = subject; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public NotificationType getType() { return type; }
        public void setType(NotificationType type) { this.type = type; }
    }

    public static class NotificationResponse {
        private String status;
        private String message;
        private LocalDateTime sentAt;

        public NotificationResponse(String status, String message, LocalDateTime sentAt) {
            this.status = status;
            this.message = message;
            this.sentAt = sentAt;
        }

        public String getStatus() { return status; }
        public String getMessage() { return message; }
        public LocalDateTime getSentAt() { return sentAt; }
    }

    public enum NotificationType {
        EMAIL, SMS, PUSH
    }
}
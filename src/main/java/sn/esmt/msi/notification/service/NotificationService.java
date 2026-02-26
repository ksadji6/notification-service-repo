package sn.esmt.msi.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import sn.esmt.msi.notification.dto.NotificationDTO.*;
import java.time.LocalDateTime;
@RefreshScope
@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    // Valeurs récupérées depuis le Config Server
    @Value("${notification.low-balance-threshold:500.0}")
    private double lowBalanceThreshold;

    @Value("${notification.email.enabled:true}")
    private boolean emailEnabled;

    @Value("${notification.sms.enabled:true}")
    private boolean smsEnabled;

    /**
     * Envoyer une notification (EMAIL / SMS / PUSH)
     * Simule l'envoi via des logs
     */
    public NotificationResponse sendNotification(SendNotificationRequest request) {
        logger.info("================================================");
        logger.info("[NOTIFICATION] Nouveau message");
        logger.info("[NOTIFICATION] Type    : {}", request.getType());
        logger.info("[NOTIFICATION] Pass    : {}", request.getPassNumber());
        logger.info("[NOTIFICATION] Sujet   : {}", request.getSubject());
        logger.info("[NOTIFICATION] Message : {}", request.getMessage());

        switch (request.getType()) {
            case EMAIL -> {
                if (emailEnabled) {
                    logger.info("[NOTIFICATION][EMAIL]   -> {} : {}",
                            request.getRecipientEmail(), request.getMessage());
                } else {
                    logger.warn("[NOTIFICATION][EMAIL]  Email désactivé.");
                }
            }
            case SMS -> {
                if (smsEnabled) {
                    logger.info("[NOTIFICATION][SMS]  -> {} : {}",
                            request.getRecipientPhone(), request.getMessage());
                } else {
                    logger.warn("[NOTIFICATION][SMS]  SMS désactivé.");
                }
            }
            case PUSH -> {
                logger.info("[NOTIFICATION][PUSH]  -> pass {} : {}",
                        request.getPassNumber(), request.getMessage());
            }
        }

        logger.info("================================================");
        return new NotificationResponse("SENT", "Notification envoyée avec succès.", LocalDateTime.now());
    }

    /**
     * Alerte solde faible
     * Appelé quand le solde passe sous le seuil configuré dans le Config Server
     */
    public NotificationResponse sendLowBalanceAlert(String passNumber, double balance) {
        logger.warn("================================================");
        logger.warn("[NOTIFICATION] ⚠  ALERTE SOLDE FAIBLE !");
        logger.warn("[NOTIFICATION] Pass    : {}", passNumber);
        logger.warn("[NOTIFICATION] Solde   : {} FCFA", balance);
        logger.warn("[NOTIFICATION] Seuil   : {} FCFA", lowBalanceThreshold);
        logger.warn("================================================");

        SendNotificationRequest request = new SendNotificationRequest();
        request.setPassNumber(passNumber);
        request.setSubject("Smart Mobility Pass - Alerte solde faible");
        request.setMessage("Votre solde est de " + balance + " FCFA, "
                + "en dessous du seuil de " + lowBalanceThreshold
                + " FCFA. Veuillez recharger votre Mobility Pass.");
        request.setType(NotificationType.SMS);

        return sendNotification(request);
    }

    /**
     * Confirmation de trajet
     * Appelé par le Trip Management Service après chaque trajet
     */
    public NotificationResponse sendTripConfirmation(String passNumber,
                                                     String transport,
                                                     String departure,
                                                     String arrival,
                                                     double amount) {
        logger.info("[NOTIFICATION] Confirmation trajet - pass : {}", passNumber);

        SendNotificationRequest request = new SendNotificationRequest();
        request.setPassNumber(passNumber);
        request.setSubject("Smart Mobility Pass - Confirmation de trajet");
        request.setMessage("Trajet " + transport
                + " de " + departure
                + " à " + arrival
                + " confirmé. Montant débité : " + amount + " FCFA.");
        request.setType(NotificationType.PUSH);

        return sendNotification(request);
    }

    /**
     * Confirmation de recharge
     * Appelé par le Billing Service après chaque recharge
     */
    public NotificationResponse sendRechargeConfirmation(String passNumber, double amount, double newBalance) {
        logger.info("[NOTIFICATION] Confirmation recharge - pass : {}", passNumber);

        SendNotificationRequest request = new SendNotificationRequest();
        request.setPassNumber(passNumber);
        request.setSubject("Smart Mobility Pass - Recharge confirmée");
        request.setMessage("Votre Mobility Pass a été rechargé de "
                + amount + " FCFA. "
                + "Nouveau solde : " + newBalance + " FCFA.");
        request.setType(NotificationType.SMS);

        return sendNotification(request);
    }
}
package sn.esmt.msi.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.esmt.msi.notification.dto.NotificationDTO.*;
import sn.esmt.msi.notification.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * POST /api/notifications/send
     * Envoyer une notification manuelle
     */
    @PostMapping("/send")
    public ResponseEntity<NotificationResponse> sendNotification(
            @RequestBody SendNotificationRequest request) {
        return ResponseEntity.ok(notificationService.sendNotification(request));
    }

    /**
     * POST /api/notifications/low-balance-alert
     * Déclencher une alerte solde faible
     * Appelé par le Billing Service
     */
    @PostMapping("/low-balance-alert")
    public ResponseEntity<NotificationResponse> lowBalanceAlert(
            @RequestParam String passNumber,
            @RequestParam double balance) {
        return ResponseEntity.ok(
                notificationService.sendLowBalanceAlert(passNumber, balance));
    }

    /**
     * POST /api/notifications/trip-confirmation
     * Confirmer un trajet
     * Appelé par le Trip Management Service
     */
    @PostMapping("/trip-confirmation")
    public ResponseEntity<NotificationResponse> tripConfirmation(
            @RequestParam String passNumber,
            @RequestParam String transport,
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam double amount) {
        return ResponseEntity.ok(
                notificationService.sendTripConfirmation(
                        passNumber, transport, departure, arrival, amount));
    }

    /**
     * POST /api/notifications/recharge-confirmation
     * Confirmer une recharge
     * Appelé par le Billing Service
     */
    @PostMapping("/recharge-confirmation")
    public ResponseEntity<NotificationResponse> rechargeConfirmation(
            @RequestParam String passNumber,
            @RequestParam double amount,
            @RequestParam double newBalance) {
        return ResponseEntity.ok(
                notificationService.sendRechargeConfirmation(passNumber, amount, newBalance));
    }
}
package com.example.appointmentsystem.service;

import com.example.appointmentsystem.model.Appointment;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender emailSender;
    
    @Autowired
    private PdfService pdfService;
    
    public void sendAppointmentConfirmation(Appointment appointment) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        
        helper.setTo(appointment.getUser().getEmail());
        helper.setSubject("Confirmation de votre rendez-vous");
        
        String emailContent = String.format(
            "Bonjour,\n\n" +
            "Votre rendez-vous a été confirmé pour le %s.\n" +
            "Numéro de confirmation : %s\n\n" +
            "Cordialement,\n" +
            "L'équipe de gestion des rendez-vous",
            appointment.getTimeSlot().getStartTime(),
            appointment.getConfirmationNumber()
        );
        
        helper.setText(emailContent);
        
        // Ajouter le PDF en pièce jointe
        byte[] pdf = pdfService.generateConfirmationPdf(appointment);
        helper.addAttachment(
            "confirmation.pdf",
            new ByteArrayResource(pdf)
        );
        
        emailSender.send(message);
        
        // Marquer l'email comme envoyé
        appointment.setEmailSent(true);
    }
}
package com.example.appointmentsystem.service;

import com.example.appointmentsystem.model.Appointment;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;

@Service
public class PdfService {
    
    public byte[] generateConfirmationPdf(Appointment appointment) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            
            // Ajouter le contenu
            document.add(new Paragraph("Confirmation de rendez-vous")
                .setFontSize(20)
                .setBold());
                
            document.add(new Paragraph(String.format(
                "Numéro de confirmation : %s",
                appointment.getConfirmationNumber()
            )));
            
            document.add(new Paragraph(String.format(
                "Date et heure : %s",
                appointment.getTimeSlot().getStartTime()
            )));
            
            document.add(new Paragraph(String.format(
                "Client : %s",
                appointment.getUser().getEmail()
            )));
            
            document.close();
            
            // Marquer le PDF comme généré
            appointment.setPdfGenerated(true);
            
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }
}
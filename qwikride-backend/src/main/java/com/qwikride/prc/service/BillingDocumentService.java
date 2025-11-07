package com.qwikride.prc.service;

import com.qwikride.prc.model.LedgerEntry;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillingDocumentService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public byte[] buildReceipt(LedgerEntry entry) {
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 720);
                contentStream.showText("QwikRide Trip Receipt");
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA, 12);
                int y = 690;
                y = writeLine(contentStream, y, String.format(Locale.US, "Trip ID: %d", entry.getId()));
                y = writeLine(contentStream, y, String.format(Locale.US, "Plan: %s", entry.getPlanName()));
                y = writeLine(contentStream, y,
                        String.format(Locale.US, "Trip Window: %s - %s",
                                DATE_TIME_FORMATTER.format(entry.getStartTime()),
                                DATE_TIME_FORMATTER.format(entry.getEndTime())));
                y = writeLine(contentStream, y,
                        String.format(Locale.US, "Duration: %d min", entry.getDurationMinutes()));
                y = writeLine(contentStream, y,
                        String.format(Locale.US, "Distance: %.2f km", entry.getDistanceKm()));
                y = writeLine(contentStream, y,
                        String.format(Locale.US, "Payment Status: %s", entry.getPaymentStatus()));
                if (entry.getPaymentReference() != null) {
                    y = writeLine(contentStream, y,
                            String.format(Locale.US, "Reference: %s", entry.getPaymentReference()));
                }
                y -= 10;
                contentStream.beginText();
                contentStream.newLineAtOffset(50, y);
                contentStream.showText("Charges:");
                contentStream.endText();
                y -= 18;
                for (var charge : entry.getCharges()) {
                    y = writeLine(contentStream, y,
                            String.format(Locale.US, "- %s: $%s", charge.getCode(),
                                    charge.getAmount().toPlainString()));
                }
                y -= 10;
                contentStream.beginText();
                contentStream.newLineAtOffset(50, y);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.showText(String.format(Locale.US, "Total: $%s", entry.getTotal().toPlainString()));
                contentStream.endText();
            }

            document.save(outputStream);
            return outputStream.toByteArray();
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to generate receipt", ex);
        }
    }

    public byte[] buildCsv(List<LedgerEntry> entries) {
        String header = "ledgerEntryId,planName,startTime,endTime,durationMinutes,distanceKm,total,paymentStatus";
        String rows = entries.stream()
                .sorted(Comparator.comparing(LedgerEntry::getStartTime))
                .map(entry -> String.join(",",
                        quote(entry.getId()),
                        quote(entry.getPlanName()),
                        quote(DATE_TIME_FORMATTER.format(entry.getStartTime())),
                        quote(DATE_TIME_FORMATTER.format(entry.getEndTime())),
                        quote(Long.toString(entry.getDurationMinutes())),
                        quote(Double.toString(entry.getDistanceKm())),
                        quote(amount(entry.getTotal())),
                        quote(entry.getPaymentStatus().name())))
                .collect(Collectors.joining("\n"));
        String csv = header + "\n" + rows;
        return csv.getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }

    private static String quote(Object value) {
        String content = value == null ? "" : value.toString().replace("\"", "\"\"");
        return '"' + content + '"';
    }

    private static String amount(BigDecimal value) {
        return value == null ? "0.00" : value.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString();
    }

    private int writeLine(PDPageContentStream contentStream, int currentY, String text) throws IOException {
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.newLineAtOffset(50, currentY);
        contentStream.showText(text);
        contentStream.endText();
        return currentY - 18;
    }
}

package com.employmentservice.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Component;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * Generiše QR kod (PNG kao base64 data-URL) iz proizvoljnog teksta.
 * Koristi se za prijavu kandidata na sajam zapošljavanja (FairCheckIn).
 */
@Component
public class QrCodeGenerator {

    public String generateBase64(String text) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 250, 250);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", baos);
            String base64 = Base64.getEncoder().encodeToString(baos.toByteArray());
            return "data:image/png;base64," + base64;
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Greška pri generisanju QR koda: " + e.getMessage());
        }
    }
}

package com.LibraryManagement.LibraryUserManagement.Common.Utilities.Services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class QRGenerationService {

    public static String getQRMessage(String floorName, String wing, long id){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('{');
        stringBuilder.append("floor : "+floorName);
        stringBuilder.append("wing : "+wing);
        stringBuilder.append("tableNo : "+id);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public byte[] generateQRCodeImage(String token) throws WriterException, IOException {
            int width = 300;
            int height = 300;
            BitMatrix bitMatrix = new MultiFormatWriter()
                    .encode(token, BarcodeFormat.QR_CODE, width, height);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            return outputStream.toByteArray();
        }
    }


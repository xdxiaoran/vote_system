package com.xdxiaoran.tool;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * 描述：   生成二维码工具
 */
@Service
public class QRCodeGenerator {


    public static void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
    public String getQRCodeImageByUrl(String url) throws IOException, WriterException {
        generateQRCodeImage(url, 350, 350, (ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static/images/QRCodeImage.png").substring(1));
        return (ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static/images/QRCodeImage.png").substring(1);
    }
}

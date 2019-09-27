package com.racloop.delivery;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IdGenerator {

    public enum Distributors {
        AMA, APH, SDM
    }

    private static int numberOfIds = 216 * 10;
    private static final String DISTRIBUTOR_IDENTIFIER = Distributors.AMA.toString();

    private static final String ORDER_TICK_PREFIX = "OT";
    private static final String DATE_PREFIX;
    private static final String DATE_PREFIX_DISPLAY;

    private static final String QR_CODE_IMAGE_PATH = "data/qrcode.png";

    private static void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    static {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddhhmmss");
        Date date = new Date();
        DATE_PREFIX = simpleDateFormat.format(date);
        simpleDateFormat = new SimpleDateFormat("ss");
        DATE_PREFIX_DISPLAY = simpleDateFormat.format(date);
    }

    private static void save(List<String> records, String path) {
        File file = new File(path);
        FileWriter fr = null;
        BufferedWriter br = null;
        try {
            fr = new FileWriter(file);
            br = new BufferedWriter(fr);
            for (String record : records) {
                br.write(record);
                br.write(System.getProperty("line.separator"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(br != null) br.close();
                if(fr != null) fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ArrayList<String> records = new ArrayList<>();
        for (int i = 0; i < numberOfIds; i++) {
            int index = i + 1;
            String id = ORDER_TICK_PREFIX + "-" + DISTRIBUTOR_IDENTIFIER + DATE_PREFIX + "-" + String.format("%05d" , index);
//            String id = DISTRIBUTOR_IDENTIFIER + "-" + DATE_PREFIX + "-" + String.format("%04d" , index);
            String displayId = DISTRIBUTOR_IDENTIFIER + DATE_PREFIX_DISPLAY + "-" + index;
            records.add(displayId + "," + id);
        }
        save(records, "/Users/rajanpunchouty/tmp/qr/qr.csv");
//        try {
//            generateQRCodeImage(ORDER_TICK_PREFIX + DISTRIBUTOR_IDENTIFIER + DATE_PREFIX + UUID.randomUUID().toString(), 100, 100, QR_CODE_IMAGE_PATH);
//        } catch (WriterException e) {
//            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
//        } catch (IOException e) {
//            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
//        }
    }
}

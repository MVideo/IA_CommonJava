package ru.mvideo.dit.xi.java;

import java.io.*;

import com.sap.aii.mapping.api.*;

public class plain2xml extends AbstractTransformation {

    @Override
    public void transform(TransformationInput ti, TransformationOutput to) throws StreamTransformationException {
        try {
            ZZamena("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Message><Record>", "</Record></Message>", "\r\n", "</Record><Record>", ti, to);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public static void ZZamena(String strB, String strE, String str1, String str11, TransformationInput filename, TransformationOutput os) throws IOException {
        ZZamena(strB, strE, str1, str11, filename.getInputPayload().getInputStream(), os.getOutputPayload().getOutputStream());
    }

    public static void ZZamena(String strB, String strE, String str1, String str11, InputStream filename, OutputStream os) throws IOException {
        DataInputStream F = new DataInputStream(filename);
        DataOutputStream F1 = new DataOutputStream(os);
        byte[] fbeg = strB.getBytes();
        byte[] fend = strE.getBytes();
        byte[] bPoisk1 = str1.getBytes();
        byte[] bZamena1 = str11.getBytes();
        int len1 = bPoisk1.length;
        byte[] Buf1 = new byte[len1];
        int j1 = 0;
        try {
            F1.write(fbeg);
            while (true) {
                Buf1[j1] = F.readByte();
                if (Buf1[j1] == bPoisk1[j1]) {
                    j1++;
                    if (j1 == len1) {
                        F1.write(bZamena1);   // Записываем строку замены					
                        j1 = 0;
                    }
                    continue;
                }
                if (j1 == 0) {
                    F1.writeByte(Buf1[0]);
                } else {
                    F1.write(Buf1, 0, j1 + 1);
                }
                j1 = 0;
            }
        } catch (IOException e) {
            F1.write(fend);
            F.close();
            F1.close();
        }
    }

    public static void main(String[] args) throws IOException {
        try {
            InputStream is = new FileInputStream("c:/11.xml");
            OutputStream os = new FileOutputStream("c:/12.xml");
            plain2xml.ZZamena("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Message><Record>", "</Record></Message>", "\r\n", "</Record><Record>", is, os);
            os.close();
            is.close();
        } catch (Exception e) {
        }
    }
}

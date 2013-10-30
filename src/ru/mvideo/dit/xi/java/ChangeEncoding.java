package ru.mvideo.dit.xi.java;

//Импорт SAP библиотек
import com.sap.aii.mapping.api.*;

//Импорт библиотек для работы с файлами (для тестирования)
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.mvideo.dit.xi.java.filetest.*;

/**
 * Change message encoding
 * @author rassakhatsky
 */
public class ChangeEncoding extends AbstractTransformation {

    public static void main(String[] args) throws FileNotFoundException, StreamTransformationException {
        String folder = "C:\\";
        String file_1 = "11.xml";
        String file_2 = "12.xml";
        com.sap.aii.mapping.api.TransformationInput ti = buildTransformationInput(folder + file_1);
        com.sap.aii.mapping.api.TransformationOutput to = buildTransformationOutput(folder + file_2);
        ChangeEncoding ss = new ChangeEncoding();
        ss.transform(ti, to);
    }

    @Override
    public void transform(TransformationInput ti, TransformationOutput to) throws StreamTransformationException {
        String default_encoding = "UTF-8";
        String encoding = "windows-1251";
        StringBuilder preSource = new StringBuilder("");

        getTrace().addInfo("Start transformation");

        preSource = convertStreamToString(ti.getInputPayload().getInputStream(), default_encoding, encoding);
        getTrace().addInfo("Get Input Data - successful");

        String strData = null;
        try {
            strData = new String(preSource.toString().getBytes("ISO-8859-1"), "UTF-8");
            getTrace().addInfo("Start encoding");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ChangeEncoding.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            // Specify the Encoding type you would like to have in Below line(un comment one you like).
            to.getOutputPayload().getOutputStream().write(strData.getBytes("windows-1251"));
            getTrace().addInfo("Finish encoding");

        } catch (IOException e) {
        }
    }

    public StringBuilder convertStreamToString(InputStream in, String default_encoding, String encoding) {
        StringBuilder sb = new StringBuilder();
        try {
            int ch;
            while ((ch = in.read()) > -1) {
                sb.append((char) ch);
                if (sb.length() == (30 + default_encoding.length())) {
                    sb.replace(30, 30 + default_encoding.length(), encoding);
                }
            }
        } catch (IOException exception) {
        }
        return sb;
    }

    /**
     * This class should be used only for manual testing
     * @param file - file destination
     * @return 
     */
    private static TransformationInput buildTransformationInput(String file) {
        MyTransformationInput ti = new MyTransformationInput();
        ti.setFileName(file);
        InputPayload inputPayload = ti.getInputPayload();
        return ti;
    }

    /**
     * This class should be used only for manual testing
     * @param file - file destination
     * @return 
     */
    private static TransformationOutput buildTransformationOutput(String file) {
        MyTransformationOutput to = new MyTransformationOutput();
        to.setFileName(file);
        return to;
    }
}

package ru.mvideo.dit.xi.java.filetest;

import com.sap.aii.mapping.api.InputPayload;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Input Payload class for manual file testing of SAP PI Java Mappings 
 * @author rassakhatsky
 */
class MyInputPayload extends InputPayload {

    private String filename;

    public void setFileName(String filename) {
        this.filename = filename;
    }

    @Override
    public InputStream getInputStream() {
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(filename);
            return fis;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MyInputPayload.class.getName()).log(Level.SEVERE, null, ex);
        }

        return fis;
    }
}

package ru.mvideo.dit.xi.java.filetest;

import com.sap.aii.mapping.api.OutputAttachments;
import com.sap.aii.mapping.api.OutputHeader;
import com.sap.aii.mapping.api.OutputParameters;
import com.sap.aii.mapping.api.OutputPayload;
import com.sap.aii.mapping.api.TransformationOutput;

/**
 *Transfarmation Input class for manual file testing of SAP PI Java Mappings
 * @author rassakhatsky
 */
public class MyTransformationOutput extends TransformationOutput {

    private String filename;

    public void setFileName(String filename) {
        this.filename = filename;
    }

    @Override
    public OutputHeader getOutputHeader() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OutputParameters getOutputParameters() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OutputPayload getOutputPayload() {
        MyOutputPayload outputPayload = new MyOutputPayload();
        outputPayload.setFileName(filename);
        return outputPayload;
    }

    @Override
    public OutputAttachments getOutputAttachments() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void copyInputAttachments() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

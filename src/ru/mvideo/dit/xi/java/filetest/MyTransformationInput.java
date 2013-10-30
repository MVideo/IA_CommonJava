package ru.mvideo.dit.xi.java.filetest;

import com.sap.aii.mapping.api.InputAttachments;
import com.sap.aii.mapping.api.InputHeader;
import com.sap.aii.mapping.api.InputParameters;
import com.sap.aii.mapping.api.InputPayload;
import com.sap.aii.mapping.api.TransformationInput;

/**
 *Transfarmation Input class for manual file testing of SAP PI Java Mappings
 * @author rassakhatsky
 */
public class MyTransformationInput extends TransformationInput {

    private String filename;
    MyInputParameters inputParameters;

    public void setFileName(String filename) {
        this.filename = filename;
    }

    @Override
    public InputHeader getInputHeader() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public InputParameters getInputParameters() {
        return inputParameters;
    }

    @Override
    public InputPayload getInputPayload() {
        MyInputPayload inputPayload = new MyInputPayload();
        inputPayload.setFileName(filename);
        inputPayload.getInputStream();
        return inputPayload;
    }

    @Override
    public InputAttachments getInputAttachments() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

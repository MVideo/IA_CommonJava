package ru.mvideo.dit.xi.java;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.sap.aii.mapping.api.AbstractTrace;
import com.sap.aii.mapping.api.StreamTransformation;
import com.sap.aii.mapping.api.StreamTransformationConstants;
import com.sap.aii.mapping.api.StreamTransformationException;

public class AsyncSyncBridge implements StreamTransformation {

    public static final String VM_CONTEXT = "http://sap.com/xi/XI";
    public static final String VM_INTERFACE_SENDER_AGENCY = "async";
    public static final String VM_INTERFACE_SENDER_SCHEME = "interface";
    public static final String VM_INTERFACE_RECEIVER_AGENCY = "asb";
    public static final String VM_INTERFACE_RECEIVER_SCHEME = "interface";
    public static final String STR_SEPARATOR = ",";

    /*
    public static final String VM_SETTINGS_SENDER_AGENCY = "async";
    public static final String VM_SETTINGS_SENDER_SCHEME = "settings";
    public static final String VM_SETTINGS_SENDER_VALUE_SERVER = "server_name";
    public static final String VM_SETTINGS_SENDER_VALUE_PORT = "server_port";
    public static final String VM_SETTINGS_SENDER_VALUE_USER = "server_user";
    public static final String VM_SETTINGS_SENDER_VALUE_PASS = "server_pass";
     */
    //public static final String VM_SETTINGS_RECEIVER_AGENCY = "asb";
    //public static final String VM_SETTINGS_RECEIVER_SCHEME = "settings";
    private Map param = null;
    private AbstractTrace trace = null;

    public void setParameter(Map param) {
        this.param = param;
        if (param == null) {
            this.param = new HashMap();
        }
    }
    String sender_service = "";
    String async_interface_name = "";
    String async_interface_ns = "";
    String sync_interface = "";
    String sync_interface_ns = "";
    String sync_interface_name = "";
    String server_name = "";
    String server_port = "";
    String server_user = "";
    String server_pass = "";

    public void execute(InputStream is, OutputStream os)
            throws StreamTransformationException {
        try {
            trace = (AbstractTrace) this.param.get(StreamTransformationConstants.MAPPING_TRACE);
            sender_service = (String) this.param.get(StreamTransformationConstants.SENDER_SERVICE);
            async_interface_name = (String) this.param.get(StreamTransformationConstants.INTERFACE);
            async_interface_ns = (String) this.param.get(StreamTransformationConstants.INTERFACE_NAMESPACE);


            sync_interface = ValueMapping.getValue(VM_INTERFACE_SENDER_AGENCY,
                    VM_INTERFACE_SENDER_SCHEME,
                    async_interface_ns + "," + async_interface_name,
                    VM_INTERFACE_RECEIVER_AGENCY,
                    VM_INTERFACE_RECEIVER_SCHEME);

            String[] vals = sync_interface.split(STR_SEPARATOR);
            if (vals.length == 2) {
                sync_interface_ns = vals[0].trim();
                trace.addWarning("interface_ns from vm =  " + sync_interface_ns);
                sync_interface_name = vals[1].trim();
                trace.addWarning("interface_name from vm =  "
                        + sync_interface_name);
            }

            if (sync_interface_ns.equalsIgnoreCase("")
                    || sync_interface_name.equalsIgnoreCase("")) {
                throw new RuntimeException(
                        new Exception(
                        "Wrong data from value mapping. See trace for details."));
            }

            Map mp = new HashMap();

            mp.put(PlainHTTP.PARAM_SERVICE, sender_service);
            mp.put(PlainHTTP.PARAM_NAMESPACE, sync_interface_ns);
            mp.put(PlainHTTP.PARAM_INTERFACE, sync_interface_name);

            PlainHTTP ph = new PlainHTTP();
            ph.call(mp, trace, is, os);

        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e);

        }
    }
}
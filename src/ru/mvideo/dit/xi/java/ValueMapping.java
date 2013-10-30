package ru.mvideo.dit.xi.java;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import ru.mvideo.dit.xi.java.datatypes.ValueMappingReplication;

import com.sap.aii.mapping.api.AbstractTrace;
import com.sap.aii.mapping.value.api.IFIdentifier;
import com.sap.aii.mapping.value.api.ValueMappingException;
import com.sap.aii.mapping.value.api.XIVMFactory;
import com.sap.aii.mapping.value.api.XIVMService;

public class ValueMapping {

    public static final String VM_CONTEXT = "http://sap.com/xi/XI";
    public static final String VM_SETTINGS_SOURCE_AGENCY = "Param";
    public static final String VM_SETTINGS_SOURCE_SCHEME = "Name";
    public static final String VM_SETTINGS_SOURCE_VALUE_SERVER = "server_name";
    public static final String VM_SETTINGS_SOURCE_VALUE_PORT = "server_port";
    public static final String VM_SETTINGS_SOURCE_VALUE_USER = "server_user";
    public static final String VM_SETTINGS_SOURCE_VALUE_PASS = "server_pass";
    public static final String VM_SETTINGS_TARGET_AGENCY = "PI";
    public static final String VM_SETTINGS_TARGET_SCHEME = "Value";

    public static String getValue(String srcContext, String srcAgency,
            String srcScheme, String srcValue, String tgtContext,
            String tgtAgency, String tgtScheme) {
        String result = "";
        IFIdentifier vmSource = XIVMFactory.newIdentifier(srcContext,
                srcAgency, srcScheme);
        IFIdentifier vmTarget = XIVMFactory.newIdentifier(tgtContext,
                tgtAgency, tgtScheme);
        try {
            result = XIVMService.executeMapping(vmSource, vmTarget, srcValue);
        } catch (ValueMappingException vmEx) {
            throw new RuntimeException(vmEx);
        }
        return result;
    }

    public static String getValue(String srcAgency, String srcScheme,
            String srcVvalue, String tgtAgency, String tgtScheme) {
        return getValue(VM_CONTEXT, srcAgency, srcScheme, srcVvalue,
                VM_CONTEXT, tgtAgency, tgtScheme);
    }

    public static String getUser() {
        return getValue(VM_SETTINGS_SOURCE_AGENCY, VM_SETTINGS_SOURCE_SCHEME,
                VM_SETTINGS_SOURCE_VALUE_USER, VM_SETTINGS_TARGET_AGENCY,
                VM_SETTINGS_TARGET_SCHEME);
    }

    public static String getPass() {
        return getValue(VM_SETTINGS_SOURCE_AGENCY, VM_SETTINGS_SOURCE_SCHEME,
                VM_SETTINGS_SOURCE_VALUE_PASS, VM_SETTINGS_TARGET_AGENCY,
                VM_SETTINGS_TARGET_SCHEME);
    }

    public static String getServer() {
        return getValue(VM_SETTINGS_SOURCE_AGENCY, VM_SETTINGS_SOURCE_SCHEME,
                VM_SETTINGS_SOURCE_VALUE_SERVER, VM_SETTINGS_TARGET_AGENCY,
                VM_SETTINGS_TARGET_SCHEME);
    }

    public static String getPort() {
        return getValue(VM_SETTINGS_SOURCE_AGENCY, VM_SETTINGS_SOURCE_SCHEME,
                VM_SETTINGS_SOURCE_VALUE_PORT, VM_SETTINGS_TARGET_AGENCY,
                VM_SETTINGS_TARGET_SCHEME);
    }

    public static void writeValue(ValueMappingReplication vmr, AbstractTrace trace) {

        if (trace != null) {
            trace.addWarning("ValueMappingReplication = " + vmr);
        }

        Map mp = new HashMap();

        mp.put(PlainHTTP.PARAM_SERVICE, "DUMMY");
        mp.put(PlainHTTP.PARAM_NAMESPACE, "http://sap.com/xi/XI/System");
        mp.put(PlainHTTP.PARAM_INTERFACE, "ValueMappingReplicationOutSynchronous");

        ByteArrayInputStream is = new ByteArrayInputStream(vmr.toString().getBytes());
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        PlainHTTP ph = new PlainHTTP();
        ph.call(mp, trace, is, os);

        String response = os.toString();
        if (trace != null) {
            trace.addWarning("Response of ValueMapping.writeValue = " + response);
        }
    }
}
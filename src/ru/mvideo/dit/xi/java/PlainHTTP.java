package ru.mvideo.dit.xi.java;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import com.sap.aii.mapping.api.AbstractTrace;

public class PlainHTTP {

    public static final String PARAM_METHOD = "param_method";
    public static final String PARAM_CONTENT_TYPE = "param_conttype";
    public static final String PARAM_ENCODING = "param_encoding";
    public static final String PARAM_SERVER = "param_server";
    public static final String PARAM_PORT = "param_port";
    public static final String PARAM_SERVICE = "param_service";
    public static final String PARAM_NAMESPACE = "param_namespace";
    public static final String PARAM_INTERFACE = "param_interface";
    public static final String PARAM_USER = "param_user";
    public static final String PARAM_PASS = "param_pass";
    public static final String PARAM_QOS = "param_qos";
    AbstractTrace logTrace = null;

    public void call(Map param, AbstractTrace trace, InputStream is, OutputStream os) {

        Object obj;
        logTrace = trace;

        // optional parameters, they have default values
        obj = param.get(PARAM_METHOD);
        String Method = (obj == null) ? "POST" : (String) obj; // HTTP method

        obj = param.get(PARAM_CONTENT_TYPE);
        String ContentType = (obj == null) ? "text/xml" : (String) obj; // Content-Type

        obj = param.get(PARAM_ENCODING);
        String Encoding = (obj == null) ? "utf-8" : (String) obj; // Encoding

        obj = param.get(PARAM_SERVER);
        String Server = (obj == null) ? ValueMapping.getServer() : (String) obj; // PI server (sap-mxd)

        obj = param.get(PARAM_PORT);
        String Port = (obj == null) ? ValueMapping.getPort() : (String) obj; // ABAP Port (8032)

        obj = param.get(PARAM_USER);
        String User = (obj == null) ? ValueMapping.getUser() : (String) obj; // User name

        obj = param.get(PARAM_PASS);
        String Pass = (obj == null) ? ValueMapping.getPass() : (String) obj; // User password

        obj = param.get(PARAM_QOS);
        String QOS = (obj == null) ? "BE" : (String) obj; // Quality of service (EO, BE, EOIO)


        // obligatory parameters
        obj = param.get(PARAM_SERVICE);
        String Service = (obj == null) ? "" : (String) obj; // Service name

        obj = param.get(PARAM_NAMESPACE);
        String Namespace = (obj == null) ? "" : (String) obj; // Interface namespace

        obj = param.get(PARAM_INTERFACE);
        String Interface = (obj == null) ? "" : (String) obj; // Interface name 


        byte[] byt = null;
        try {
            log("read payload, available: " + is.available());
            byt = new byte[is.available()];
            is.read(byt);
        } catch (IOException e) {
            //e.printStackTrace();
            throw new RuntimeException(e);
        }

        String request = "http://" + Server + ":" + Port + "/sap/xi/adapter_plain"
                + "?type=entry" + "&service=" + Service + "&namespace="
                + Namespace + "&interface=" + Interface + "&sap-user="
                + User + "&sap-password=" + Pass + "&qos=" + QOS;
        log("request string: " + request);


        try {
            URL url = new URL(request);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) connection;

            // Set the appropriate HTTP parameters.
            httpConn.setRequestProperty("Content-Length",
                    String.valueOf(byt.length));
            httpConn.setRequestProperty("Content-Type", ContentType
                    + "; charset=" + Encoding);
            httpConn.setRequestProperty("SOAPAction", "");
            httpConn.setRequestMethod(Method);

            log("Sending request");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            OutputStream out = httpConn.getOutputStream();
            out.write(byt);
            out.close();

            log("Response Code = " + httpConn.getResponseCode());
            log("Response Message =  "
                    + httpConn.getResponseMessage());

            // Read the response
            DataOutputStream douts = new DataOutputStream(os);
            DataInputStream dinps = new DataInputStream(
                    httpConn.getInputStream());

            int i;
            try {
                while ((i = dinps.read()) != -1) {
                    douts.writeByte(i);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                dinps.close();
                douts.close();
            }

        } catch (Exception ex) {
            log("Error: " + ex.getMessage());
            throw new RuntimeException(ex);
        }

    }

    // write trace log, if trace exists
    private void log(String text) {
        if (logTrace != null) {
            logTrace.addWarning(text);
        }
    }
}
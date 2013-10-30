package ru.mvideo.dit.xi.java;

import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import com.sap.aii.mapping.api.AbstractTrace;
import com.sap.aii.mapping.api.StreamTransformationConstants;
import com.sap.aii.mappingtool.tf3.rt.Container;

public class ChannelManager {

    private String service;
    private String channel;
    private String party;
    private AbstractTrace trace;
    private String user = ValueMapping.getUser();
    private String pass = ValueMapping.getPass();

    // constructor for trace enable, use container from UDF input parameters
    public ChannelManager(String _service, String _channel, Container container) {
        service = _service;
        channel = _channel;
        if (container != null) {
            Map param = container.getTransformationParameters();
            trace = (AbstractTrace) param.get(StreamTransformationConstants.MAPPING_TRACE);
        }
        log("new ChannelManager for " + service + "\\" + channel);
    }

    // base constructor
    public ChannelManager(String _service, String _channel) {
        service = _service;
        channel = _channel;
    }

    // write trace log, if trace exists
    private void log(String text) {
        if (trace != null) {
            trace.addWarning(text);
        }
    }

    // set party if use (not necessarily)
    public void setParty(String _party) {
        party = _party;
    }

    // start channel
    public String startChannel() {
        log("start channel");
        return connect(getUrl(service, channel, "start"));
    }

    // stop channel
    public String stopChannel() {
        log("stop channel");
        return connect(getUrl(service, channel, "stop"));
    }

    // form Url to request
    private String getUrl(String service, String channel, String action) {
        String partyParam = (party == null) ? "" : "party=" + party;
        String result = "http://" + ValueMapping.getServer() + ":"
                + ValueMapping.getPort()
                + "/AdapterFramework/ChannelAdminServlet?" + partyParam
                + "service=" + service + "&channel=" + channel + "&action="
                + action;
        log("getUrl = " + result);
        return result;
    }

    // inner class for authentication
    class MyAuthenticator extends Authenticator {

        public PasswordAuthentication getPasswordAuthentication() {
            return (new PasswordAuthentication(user, pass.toCharArray()));
        }
    }

    // send request to start/stop channel
    // return http code from response
    String connect(String _url) {
        log("try connect");
        String result = "-1";
        try {
            Authenticator.setDefault(new MyAuthenticator());
            URL url = new URL(_url);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) connection;
            httpConn.connect();
            result = httpConn.getResponseCode() + "";
            log("connect result = " + result);
            httpConn.disconnect();
        } catch (Exception ex) {
            log("connect error " + ex.getClass() + ex.getMessage());
            throw new RuntimeException(ex);
        }
        return result;
    }
}
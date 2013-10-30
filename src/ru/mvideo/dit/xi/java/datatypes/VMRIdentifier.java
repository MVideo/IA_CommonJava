package ru.mvideo.dit.xi.java.datatypes;

public class VMRIdentifier {

    public String agency;
    public String scheme;
    public String value;

    public VMRIdentifier(String _agency, String _scheme, String _value) {
        agency = _agency;
        scheme = _scheme;
        value = _value;
    }

    public String toString() {
        return "<Identifier agency=\"" + agency + "\" scheme=\"" + scheme + "\">" + value + "</Identifier>";
    }
}
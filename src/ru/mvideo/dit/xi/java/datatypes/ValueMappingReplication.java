package ru.mvideo.dit.xi.java.datatypes;

import java.util.ArrayList;
import java.util.Iterator;

public class ValueMappingReplication {

    ArrayList items = new ArrayList();

    public void add(VMRItem item) {
        items.add(item);
    }

    public ArrayList getItems() {
        return items;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<ns1:ValueMappingReplication xmlns:ns1=\"http://sap.com/xi/XI/System\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchemainstance\">");
        for (Iterator iterator = items.iterator(); iterator.hasNext();) {
            VMRItem item = (VMRItem) iterator.next();
            sb.append(item);
        }
        sb.append("</ns1:ValueMappingReplication>");
        return sb.toString();
    }
}
package ru.mvideo.dit.xi.java.datatypes;

public class VMRItem {

    public static final String OPER_INSERT = "Insert"; // Insert = all fields must be set;
    public static final String OPER_DELETE = "Delete"; // Delete = all fields must be set;
    public static final String OPER_DELETE_GROUP = "DeleteGroup"; // DeleteGroup = fields GroupID and context must be set; 
    public static final String OPER_DELETE_CONTEXT = "DeleteContext"; // DeleteContext = field Context must be set;
    public static final String OPER_DELETE_CONTEXT_GENERIC = "DeleteContextGeneric"; // DeleteContextGeneric = Context contains the starting part for the context to be deleted
    public static final String CONTEXT_XI = "http://sap.com/xi/XI";
    public String operation;
    public String groupID;
    public String context;
    VMRIdentifier ident;

    public VMRItem(String _operation, String _groupID, String _context) {
        operation = _operation;
        groupID = _groupID;
        context = _context;
    }

    public void setIdent(VMRIdentifier _ident) {
        ident = _ident;
    }

    public VMRIdentifier getIdent() {
        return ident;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<Item>");
        sb.append("<Operation>" + operation + "</Operation>");
        if (groupID != null) {
            sb.append("<GroupID>" + groupID + "</GroupID>");
        }
        sb.append("<Context>" + context + "</Context>");
        if (ident != null) {
            sb.append(ident);
        }
        sb.append("</Item>");
        return sb.toString();
    }
}
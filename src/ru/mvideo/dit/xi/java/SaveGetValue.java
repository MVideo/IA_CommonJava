package ru.mvideo.dit.xi.java;

import java.util.Map;

import ru.mvideo.dit.xi.java.datatypes.VMRIdentifier;
import ru.mvideo.dit.xi.java.datatypes.VMRItem;
import ru.mvideo.dit.xi.java.datatypes.ValueMappingReplication;

import com.sap.aii.mapping.api.AbstractTrace;
import com.sap.aii.mapping.api.StreamTransformationConstants;
import com.sap.aii.mappingtool.tf3.rt.Container;

public class SaveGetValue {

    public static final String CONTEXT_SGV = "ru.mvideo.dit.xi.java.SaveGetValue";
    public static final String SOURCE_AGENCY = "PI";
    public static final String SOURCE_SCHEME = "MessageID";
    public static final String TARGET_AGENCY = "SGV";
    public static final String TARGET_SCHEME = "Value";

    // сохранение значения. Для вызова из UDF
    static public String saveValueUDF(String val, Container container) {
        Map param = container.getTransformationParameters();
        return saveValue(val, param);
    }

    /*
     * сохранение значения. Для вызова из XSL
     * 
     * call sample:
     * --------------------------------------------------------------
     * ----------------------------------------- <xsl:stylesheet ...
     * xmlns:sgv="ru.mvideo.episys.common.SaveGetValue" ... > ... <xsl:param
     * name="inputparam"/> ... <xsl:if
     * test="function-available('sgv:saveValueXSL')"> <xsl:variable
     * name="tmp"><xsl:value-of
     * select="sgv:saveValueXSL(<your value for save>, $inputparam)"
     * /></xsl:variable> </xsl:if> ...
     * ------------------------------------------
     * -------------------------------------------------------------
     */
    static public void saveValueXSL(String val, Map param) {
        saveValue(val, param);
    }

    // сохранение значения
    static public String saveValue(String val, Map param) {
        AbstractTrace trace = (AbstractTrace) param.get(StreamTransformationConstants.MAPPING_TRACE);

        String messID = (String) param.get(StreamTransformationConstants.MESSAGE_ID);
        trace.addWarning("saveValue, MESSAGE_ID = " + messID);

        ValueMappingReplication vmr = new ValueMappingReplication();
        VMRItem item;
        VMRIdentifier ident;

        item = new VMRItem(VMRItem.OPER_INSERT, messID, CONTEXT_SGV);
        ident = new VMRIdentifier(SOURCE_AGENCY, SOURCE_SCHEME, messID);
        item.setIdent(ident);
        vmr.add(item);

        item = new VMRItem(VMRItem.OPER_INSERT, messID, CONTEXT_SGV);
        ident = new VMRIdentifier(TARGET_AGENCY, TARGET_SCHEME, val);
        item.setIdent(ident);
        vmr.add(item);

        ValueMapping.writeValue(vmr, trace);
        trace.addWarning("saveValue, saved value = " + val);
        return val;
    }

    static public String getValueUDF(Container container) {
        Map param = container.getTransformationParameters();
        return getValue(param);
    }

    static public String getValueXSL(Map param) {
        return getValue(param);
    }

    // чтение и удаление значения
    static public String getValue(Map param) {

        AbstractTrace trace = (AbstractTrace) param.get(StreamTransformationConstants.MAPPING_TRACE);

        String messID = (String) param.get(StreamTransformationConstants.REF_TO_MESSAGE_ID);
        String val = ValueMapping.getValue(CONTEXT_SGV, SOURCE_AGENCY, SOURCE_SCHEME, messID, CONTEXT_SGV, TARGET_AGENCY, TARGET_SCHEME);
        trace.addWarning("SaveGetValue, getValue = " + val);

        ValueMappingReplication vmr = new ValueMappingReplication();
        VMRItem item;

        item = new VMRItem(VMRItem.OPER_DELETE_GROUP, messID, CONTEXT_SGV);
        vmr.add(item);
        ValueMapping.writeValue(vmr, trace);
        return val;
    }
}
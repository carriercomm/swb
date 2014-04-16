package org.semanticwb.bsc.tracing;

import java.util.Calendar;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.TreeSet;
import org.semanticwb.platform.SemanticObject;

public class Risk extends org.semanticwb.bsc.tracing.base.RiskBase {

    public Risk(org.semanticwb.platform.SemanticObject base) {
        super(base);
    }

    /**
     * Obtiene el prefijo en base al a&ntilde;o actual seguido de un n&uacute;mero consecutivo. 
     * @return el objeto String que representa el prefijo para un Riesgo
     */
    @Override
    public String getPrefix() {
        String prefix = super.getPrefix();
        if (prefix == null) {
            setPrefix(Calendar.getInstance().get(Calendar.YEAR)+"_"+getConsecutive());
            setYearRisk(Calendar.getInstance().get(Calendar.YEAR));
        }
        return prefix;
    }
    
    /**
     * Almacena el prefijo del Riesgo actual.
     * @param value el objeto String que representa el prefijo de un Riesgo
     */
    @Override
    public void setPrefix(String value) {
        super.setPrefix(value);
    }

    /**
     * Obtiene el siguiente prefijo consecutivo para un Riesgo.
     * @return el objeto String que representa el siguiente prefijo para un Riesgo
     */
    private synchronized int getConsecutive() {
        int consecutive = 0;
        SortedSet<Integer> map = new TreeSet<Integer>();
        Iterator<SemanticObject> it = getBSC().getSemanticModel().listSubjects(Risk.bsc_yearRisk, Calendar.getInstance().get(Calendar.YEAR));
        while (it.hasNext()) {
            SemanticObject obj = it.next();
            String prefix = obj.getProperty(Risk.bsc_prefix, false);
            if(prefix != null && prefix.lastIndexOf("_") > -1) {
                prefix = prefix.substring(prefix.lastIndexOf("_") + 1);
                int serial = Integer.parseInt(prefix);
                map.add(serial);
            }
        }
        //Collections.sort(map);
        try {
            consecutive = map.last();  //map.get(map.size() - 1;
        }catch(NoSuchElementException nse) {
            consecutive = 0;
        }finally {
            consecutive++;
        }
        return consecutive;
    }
}

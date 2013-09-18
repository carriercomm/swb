package org.semanticwb.bsc.tracing;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Locale;
import org.semanticwb.bsc.Measure;
import org.semanticwb.bsc.accessory.Period;
import org.semanticwb.bsc.tracing.base.SeriesBase;
import org.semanticwb.model.SWBModel;
import org.semanticwb.platform.SemanticObject;
import org.semanticwb.platform.SemanticObserver;


public class Series extends org.semanticwb.bsc.tracing.base.SeriesBase implements Comparable<Series>
{
    static
    {
        SeriesBase.bsc_format.registerObserver(new SemanticObserver() {
            @Override
            public void notify(SemanticObject obj, Object prop, String lang, String action)
            {
                Series s = (Series)obj.createGenericInstance();
                if("REMOVE".equalsIgnoreCase(action)) {
                    s.formatter = null;
                }else {
                    s.setFormatter();
                }
            }
        });
        
        sclass.registerObserver(new SemanticObserver() {
            @Override
            public void notify(SemanticObject obj, Object prop, String lang, String action)
            {
                SWBModel model = (SWBModel)obj.getModel().getModelObject().createGenericInstance();
                if("CREATE".equalsIgnoreCase(action)) {
                }else if("SET".equalsIgnoreCase(action)) {
                }else if("ADD".equalsIgnoreCase(action)) {               
                }else if("REMOVE".equalsIgnoreCase(action)) {               
                }
            }
        });
    }
    
    
    
    
    public static final String defaultFormatPattern = "#,##0.0#";
    private DecimalFormat formatter;
    
    public Series(org.semanticwb.platform.SemanticObject base)
    {
        super(base);
    }

    public NumberFormat getFormatter() {
System.out.println("getFormatter():"+formatter);
        if(formatter==null) {
            setFormatter();
        }
        return formatter;
    }
    
    private void setFormatter() {
        org.semanticwb.bsc.catalogs.Format format = getFormat();
        Locale locale;
        try {
            locale = new Locale(format.getLanguage().getId().toLowerCase(), format.getCountry().getId().toUpperCase());
        }catch(Exception e) {
            locale = new Locale("es","MX");
        }
        NumberFormat numFormat = NumberFormat.getNumberInstance(locale);
        formatter = (DecimalFormat)numFormat;
        try {
            formatter.applyPattern(format.getFormatPattern());
        }catch(Exception iae) {
            formatter.applyPattern(Series.defaultFormatPattern);
        }
    }
    
    @Override
    public int compareTo(Series anotherSeries) {
        int compare = 0;
        compare = getIndex() > anotherSeries.getIndex() ? 1 : -1;
        return compare;
    }
    
    public Measure getMeasureByPeriod(Period period)
    {
        Iterator<Measure> measures = listMeasures();
        Measure measure = null;
        while(measures.hasNext())
        {
            measure = measures.next();
            if(measure.getEvaluation().getPeriod().equals(period))
            {
                return measure;
            }
        }
        return null;
    }
}

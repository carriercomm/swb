package org.semanticwb.bsc.tracing;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.semanticwb.SWBUtils;
import org.semanticwb.base.util.GenericFilterRule;
import org.semanticwb.bsc.BSC;
import org.semanticwb.bsc.SM;
import org.semanticwb.bsc.accessory.Period;
import org.semanticwb.bsc.catalogs.Format;
import org.semanticwb.bsc.tracing.base.SeriesBase;
import org.semanticwb.model.FormValidateException;
import org.semanticwb.model.GenericIterator;
import org.semanticwb.model.SWBContext;
import org.semanticwb.model.User;
import org.semanticwb.platform.SemanticObject;
import org.semanticwb.platform.SemanticObserver;
import org.semanticwb.platform.SemanticProperty;


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
    }
    
    
    
    
    
    private DecimalFormat formatter;
    
    public Series(org.semanticwb.platform.SemanticObject base)
    {
        super(base);
    }

    public NumberFormat getFormatter() {
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
            formatter.applyPattern(Format.defaultFormatPattern);
        }
    }
    
    @Override
    public int compareTo(Series anotherSeries) {
        int compare = 0;
        compare = getIndex() > anotherSeries.getIndex() ? 1 : -1;
        return compare;
    }

    @Override
    public Measure getMeasure() {
        return getMeasure(getSm().getLastPeriod());
    }
    
    public Measure getMeasure(Period period)
    {
        if(period==null) {
            return null;
        }
        
        Iterator<Measure> measures = listMeasures();
        Measure measure;
        while(measures.hasNext())
        {
            measure = measures.next();
            if(measure.getEvaluation()!=null && period.equals(measure.getEvaluation().getPeriod()))
            {
                return measure;
            }
        }
        return null;
    }
    
    public List<EvaluationRule> listValidEvaluationRules() {
        return listValidEvaluationRules(true);
    }
    
    public List<EvaluationRule> listValidEvaluationRules(boolean ascendent) {        
        List<EvaluationRule> validRules = SWBUtils.Collections.filterIterator(listEvaluationRules(), new GenericFilterRule<EvaluationRule>() {
                                                                        @Override
                                                                        public boolean filter(EvaluationRule r) {
                                                                            BSC scorecard = (BSC)r.getSemanticObject().getModel().getModelObject().createGenericInstance();
                                                                            User user = SWBContext.getSessionUser(scorecard.getUserRepository().getId());
                                                                            if(user==null) {
                                                                                user = SWBContext.getAdminUser();
                                                                            }
                                                                            return !r.isValid() || !user.haveAccess(r);
                                                                        }            
                                                                    });
        if(ascendent) {
            Collections.sort(validRules);
        }else {
            Collections.sort(validRules, Collections.reverseOrder());
        }
        return validRules;
    }

    @Override
    public void validOrder(HttpServletRequest request, SemanticProperty prop, String propName) throws FormValidateException {
        int ordinal;
        try {
            String value = request.getParameter(propName);
            ordinal = Integer.parseInt(value);
        }catch(NumberFormatException pe) {            
            throw new FormValidateException("El valor debe ser numérico y no puede repetirse");
        }
        
        GenericIterator<Series> it = getSm().listSerieses();
        while(it.hasNext()) {
            Series s = it.next();
            if( this.equals(s) ) {
                continue;
            }
            if(ordinal == s.getIndex()) {
                throw new FormValidateException("El valor ordinal debe ser numérico y no puede repetirse");
            }
        }
    }
}

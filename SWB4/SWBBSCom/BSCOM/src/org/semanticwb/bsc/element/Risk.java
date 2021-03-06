package org.semanticwb.bsc.element;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.semanticwb.SWBPortal;
import org.semanticwb.bsc.accessory.Period;
import org.semanticwb.bsc.parser.RiskParser;
import org.semanticwb.bsc.tracing.Control;
import org.semanticwb.bsc.tracing.Factor;
import org.semanticwb.model.SWBContext;
import org.semanticwb.model.User;
import org.semanticwb.model.WebSite;
import org.semanticwb.platform.SemanticObject;

public class Risk extends org.semanticwb.bsc.element.base.RiskBase {

    static {
        SWBPortal.getIndexMgr().getDefaultIndexer().registerParser(Risk.class, new RiskParser());
    }

    public Risk(org.semanticwb.platform.SemanticObject base) {
        super(base);
    }

    /**
     * Obtiene el prefijo en base al a&ntilde;o actual seguido de un
     * n&uacute;mero consecutivo.
     *
     * @return el objeto String que representa el prefijo para un Riesgo
     */
    @Override
    public String getPrefix() {
        String prefix = super.getPrefix();
        if (prefix == null) {
            final int y = Calendar.getInstance().get(Calendar.YEAR);
            prefix = y + "_" + getConsecutive();
            setPrefix(prefix);
            setYearRisk(y);
        }
        return prefix;
    }

    /**
     * Almacena el prefijo del Riesgo actual.
     *
     * @param value el objeto String que representa el prefijo de un Riesgo
     */
    @Override
    public void setPrefix(String value) {
        super.setPrefix(value);
    }

    /**
     * Obtiene el siguiente prefijo consecutivo para un Riesgo.
     *
     * @return el objeto String que representa el siguiente prefijo para un
     * Riesgo
     */
    private synchronized int getConsecutive() {
        int consecutive = 0;
        SortedSet<Integer> map = new TreeSet<Integer>();
        Iterator<SemanticObject> it = getBSC().getSemanticModel().listSubjects(Risk.bsc_yearRisk, Calendar.getInstance().get(Calendar.YEAR));
        while (it.hasNext()) {
            SemanticObject obj = it.next();
            String prefix = obj.getProperty(Risk.bsc_prefix, false);
            if (prefix != null && prefix.lastIndexOf("_") > -1) {
                prefix = prefix.substring(prefix.lastIndexOf("_") + 1);
                int serial = Integer.parseInt(prefix);
                map.add(serial);
            }
        }
        //Collections.sort(map);
        try {
            consecutive = map.last();  //map.get(map.size() - 1;
        } catch (NoSuchElementException nse) {
            consecutive = 0;
        } finally {
            consecutive++;
        }
        return consecutive;
    }

    /**
     * Calcula el cuadrante del mapa de riesgos en que aparecer&aacute; el
     * riesgo en base a los valores del nivel de impacto y la probabilidad,
     * tanto para la evaluaci&oacute;n inicial como para la final
     *
     * @param initial indica si el cuadrante a calcular es de la
     * evaluaci&oacute;n inicial {@code true}, o de la final {@code false}
     * @return el n&uacute;mero del cuadrante en el mapa de riesgos en que
     * aparecer&aacute; este riesgo
     */
    public synchronized short calculateQuadrant(boolean initial) {

        short quadrant = 0;
        if (initial && this.getIniAssessmentImpactLevel() > 0 && this.getIniAssessmentLikelihood() > 0) {
            if (this.getIniAssessmentImpactLevel() > 5 && this.getIniAssessmentLikelihood() > 5) {
                quadrant = 1;
            } else if (this.getIniAssessmentImpactLevel() <= 5 && this.getIniAssessmentLikelihood() > 5) {
                quadrant = 2;
            } else if (this.getIniAssessmentImpactLevel() > 5 && this.getIniAssessmentLikelihood() <= 5) {
                quadrant = 3;
            } else if (this.getIniAssessmentImpactLevel() <= 5 && this.getIniAssessmentLikelihood() <= 5) {
                quadrant = 4;
            }
        } else if (!initial && this.getFinAssessmentImpactLevel() > 0 && this.getFinAssessmentLikelihood() > 0) {
            if (this.getFinAssessmentImpactLevel() > 5 && this.getFinAssessmentLikelihood() > 5) {
                quadrant = 1;
            } else if (this.getFinAssessmentImpactLevel() < 6 && this.getFinAssessmentLikelihood() > 5) {
                quadrant = 2;
            } else if (this.getFinAssessmentImpactLevel() > 5 && this.getFinAssessmentLikelihood() < 6) {
                quadrant = 3;
            } else if (this.getFinAssessmentImpactLevel() < 6 && this.getFinAssessmentLikelihood() < 6) {
                quadrant = 4;
            }
        }
        return quadrant;
    }

    /**
     * Calcula si el riesgo es controlado suficientemente en base a la
     * determinaci&oacute;n de todos los controles relacionados.
     *
     * @return {@literal SI} en caso de que la determinaci&oacute;n de todos los
     * controles relacionados sea suficiente, {@literal NO} en caso contrario
     */
    public synchronized boolean calculateControled() {

        boolean controled = true;

        Iterator<Factor> factorIt = this.listFactors();
        if (factorIt != null && factorIt.hasNext()) {
            while (factorIt.hasNext()) {
                Factor factor = factorIt.next();
                Iterator<Control> controlIt = factor.listControls();
                if (controlIt != null && controlIt.hasNext()) {
                    while (controlIt.hasNext()) {
                        Control control = controlIt.next();
                        if (!control.calculateDetermination()) {
                            controled = false;
                            break;
                        }
                    }
                } else {
                    controled = false;
                    break;
                }
            }
        } else {
            controled = false;
        }

        return controled;
    }

    /**
     * Genera un conjunto de objetos {@code Factor} validos para el usuario de
     * sesi&oacute;n, relacionados a este {@code Risk}, ordenado por el prefijo
     * de cada uno.
     *
     * @return un {@code Iterator} con el conjunto de factores asociados al
     * riesgo que son validos y a los que el usuario tiene acceso.
     */
    public Iterator<Factor> listValidFactorsByPrefix() {

        Iterator<Factor> facIt = listFactors();
        TreeMap<String, Factor> validFactors = new TreeMap<String, Factor>();
        User user = SWBContext.getSessionUser();

        while (facIt != null && facIt.hasNext()) {
            Factor factor = facIt.next();
            if (factor.isValid() && user != null && user.haveAccess(factor)) {
                validFactors.put(factor.getPrefix(), factor);
            }
        }
        Iterator<Entry<String, Factor>> entriesIt = validFactors.entrySet().iterator();
        ArrayList<Factor> factors = new ArrayList<Factor>();
        while (entriesIt.hasNext()) {
            Entry<String, Factor> entry = entriesIt.next();
            factors.add(entry.getValue());
        }
        return factors.iterator();
    }

    /**
     * Genera un conjunto de objetos {@code Factor} validos para el usuario de
     * sesi&oacute;n, relacionados a este {@code Risk}.
     *
     * @return un {@code Iterator} con el conjunto de factores asociados al
     * riesgo que son validos y a los que el usuario tiene acceso.
     */
    public Iterator<Factor> listValidFactors() {

        Iterator<Factor> facIt = listFactors();
        ArrayList<Factor> factors = new ArrayList();
        User user = SWBContext.getSessionUser();

        while (facIt != null && facIt.hasNext()) {
            Factor factor = facIt.next();
            if (factor.isValid() && user != null && user.haveAccess(factor)) {
                factors.add(factor);
            }
        }
        return factors.iterator();
    }
    
    @Override
    public boolean canView() {
        final WebSite scorecard = (WebSite) getSemanticObject().getModel().getModelObject().createGenericInstance();
        final User user = SWBContext.getSessionUser(scorecard.getUserRepository().getId());
        return user.haveAccess(this);
    }
    
    @Override
    public String getStatusIconClass() {
        return "quadrant_"+calculateQuadrant(Boolean.FALSE);
    }

    @Override
    public String getStatusIconClass(Period period) {
        return getStatusIconClass();
    }
}

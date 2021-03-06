/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.semanticwb.process.forms;

import com.arthurdo.parser.HtmlStreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import org.semanticwb.Logger;
import org.semanticwb.SWBUtils;
import org.semanticwb.model.Resource;
import org.semanticwb.model.User;
import org.semanticwb.platform.SemanticClass;
import org.semanticwb.platform.SemanticProperty;
import org.semanticwb.portal.api.SWBParamRequest;
import org.semanticwb.process.model.SWBProcessFormMgr;

/**
 *
 * @author jorge.jimenez
 */
public class SWBLabelTag implements SWBFormLayer {

    private static Logger log = SWBUtils.getLogger(SWBLabelTag.class);
    private HtmlStreamTokenizer tok = null;
    private String htmlType = "dojo";
    HttpServletRequest request = null;
    SWBParamRequest paramRequest = null;
    SWBProcessFormMgr mgr = null;
    Resource base = null;
    User user = null;
    String stag="";
    HashMap hmapClasses=null;
    HashMap hMapProperties=null;
    String sTagClassComplete = null;
    String sTagProp = null;
    String sformElement=null;
    String smode=null;
    String sPrefix=null;
    String sTagClass=null;



    public SWBLabelTag(HttpServletRequest request, SWBParamRequest paramRequest, HashMap hmapClasses, HashMap <String, String> hMapProperties, SWBProcessFormMgr mgr, HtmlStreamTokenizer tok, String htmlType) {
        this.tok = tok;
        stag=tok.getRawString();
        this.request = request;
        this.paramRequest = paramRequest;
        if(htmlType!=null) this.htmlType=htmlType;
        //base = paramRequest.getResourceBase();
        //user = paramRequest.getUser();
        this.mgr = mgr;
        this.hmapClasses=hmapClasses;
        this.hMapProperties=hMapProperties;
        setAttributes();
    }

    private void setAttributes(){
        Iterator <String> itTagKeys=hMapProperties.keySet().iterator();
        while(itTagKeys.hasNext()){
            String sTagKey=itTagKeys.next();
            if(sTagKey.equalsIgnoreCase("class")){
                sTagClassComplete=(String)hMapProperties.get(sTagKey);
                if(sTagClassComplete!=null){
                    int pos=sTagClassComplete.indexOf(":");
                    if(pos>-1){
                        sPrefix=sTagClassComplete.substring(0,pos);
                        sTagClass=sTagClassComplete.substring(pos+1);
                    }
                }

            }else if(sTagKey.equalsIgnoreCase("prop")){
                sTagProp=(String)hMapProperties.get(sTagKey);
            }
        }

    }


    public String getHtml() {
        String renderElement = null;
        if (request != null) {
            try {
                Iterator <SemanticClass> itClasses=hmapClasses.keySet().iterator();
                while(itClasses.hasNext()){
                    SemanticClass cls=itClasses.next();
                    if(sPrefix.equalsIgnoreCase(cls.getPrefix())){
                        if(cls.getURI().endsWith(sTagClass)){
                            Iterator <SemanticProperty> itClassProps=((ArrayList)hmapClasses.get(cls)).iterator();
                            while(itClassProps.hasNext()){
                                SemanticProperty semProp=itClassProps.next();
                                if(semProp.getURI().endsWith(sTagProp)){
                                    renderElement=mgr.renderLabel(request, semProp, mgr.MODE_VIEW);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error(e);
            }
        }
        return renderElement;
    }

    public String getTag() {
        return stag;
    }

    public String getTagClass() {
        return sTagClass;
    }

    public String getMoreAttr() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setMoreAttr(String moreattr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

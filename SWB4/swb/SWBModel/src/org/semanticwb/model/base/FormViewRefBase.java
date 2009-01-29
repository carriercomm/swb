package org.semanticwb.model.base;


public class FormViewRefBase extends org.semanticwb.model.Reference implements org.semanticwb.model.Activeable
{
    public static final org.semanticwb.platform.SemanticClass swbxf_FormView=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/xforms/ontology#FormView");
    public static final org.semanticwb.platform.SemanticProperty swb_formView=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/ontology#formView");
    public static final org.semanticwb.platform.SemanticProperty swb_formMode=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/ontology#formMode");
    public static final org.semanticwb.platform.SemanticClass swbxf_FormViewRef=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/xforms/ontology#FormViewRef");
    public static final org.semanticwb.platform.SemanticClass sclass=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/xforms/ontology#FormViewRef");

    public FormViewRefBase(org.semanticwb.platform.SemanticObject base)
    {
        super(base);
    }

    public static org.semanticwb.model.FormViewRef getFormViewRef(String id, org.semanticwb.model.SWBModel model)
    {
        return (org.semanticwb.model.FormViewRef)model.getSemanticObject().getModel().getGenericObject(model.getSemanticObject().getModel().getObjectUri(id,sclass),sclass);
    }

    public static java.util.Iterator<org.semanticwb.model.FormViewRef> listFormViewRefs(org.semanticwb.model.SWBModel model)
    {
        java.util.Iterator it=model.getSemanticObject().getModel().listInstancesOfClass(sclass);
        return new org.semanticwb.model.GenericIterator<org.semanticwb.model.FormViewRef>(org.semanticwb.model.FormViewRef.class, it, true);
    }

    public static java.util.Iterator<org.semanticwb.model.FormViewRef> listFormViewRefs()
    {
        java.util.Iterator it=sclass.listInstances();
        return new org.semanticwb.model.GenericIterator<org.semanticwb.model.FormViewRef>(org.semanticwb.model.FormViewRef.class, it, true);
    }

    public static org.semanticwb.model.FormViewRef createFormViewRef(String id, org.semanticwb.model.SWBModel model)
    {
        return (org.semanticwb.model.FormViewRef)model.getSemanticObject().getModel().createGenericObject(model.getSemanticObject().getModel().getObjectUri(id, sclass), sclass);
    }

    public static void removeFormViewRef(String id, org.semanticwb.model.SWBModel model)
    {
        model.getSemanticObject().getModel().removeSemanticObject(model.getSemanticObject().getModel().getObjectUri(id,sclass));
    }

    public static boolean hasFormViewRef(String id, org.semanticwb.model.SWBModel model)
    {
        return (getFormViewRef(id, model)!=null);
    }

    public void setFormView(org.semanticwb.model.FormView formview)
    {
        getSemanticObject().setObjectProperty(swb_formView, formview.getSemanticObject());
    }

    public void removeFormView()
    {
        getSemanticObject().removeProperty(swb_formView);
    }

    public org.semanticwb.model.FormView getFormView()
    {
         org.semanticwb.model.FormView ret=null;
         org.semanticwb.platform.SemanticObject obj=getSemanticObject().getObjectProperty(swb_formView);
         if(obj!=null)
         {
             ret=(org.semanticwb.model.FormView)obj.getSemanticClass().newGenericInstance(obj);
         }
         return ret;
    }

    public String getFormMode()
    {
        return getSemanticObject().getProperty(swb_formMode);
    }

    public void setFormMode(String formMode)
    {
        getSemanticObject().setProperty(swb_formMode, formMode);
    }
}

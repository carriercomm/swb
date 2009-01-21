package org.semanticwb.repository.base;


public class FrozenNodeBase extends org.semanticwb.repository.BaseNode implements org.semanticwb.repository.Referenceable
{
    public static final org.semanticwb.platform.SemanticProperty jcr_frozenPrimaryType=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.jcp.org/jcr/1.0#frozenPrimaryType");
    public static final org.semanticwb.platform.SemanticProperty jcr_frozenMixinTypes=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.jcp.org/jcr/1.0#frozenMixinTypes");
    public static final org.semanticwb.platform.SemanticProperty jcr_frozenUuid=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.jcp.org/jcr/1.0#frozenUuid");
    public static final org.semanticwb.platform.SemanticProperty jcr_uuid=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.jcp.org/jcr/1.0#uuid");
    public static final org.semanticwb.platform.SemanticClass nt_FrozenNode=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.jcp.org/jcr/nt/1.0#frozenNode");


    public static org.semanticwb.repository.FrozenNode createFrozenNode(String id, org.semanticwb.model.SWBModel model)
    {
        return (org.semanticwb.repository.FrozenNode)model.getSemanticObject().getModel().createGenericObject(model.getSemanticObject().getModel().getObjectUri(id, nt_FrozenNode), nt_FrozenNode);
    }

    public FrozenNodeBase(org.semanticwb.platform.SemanticObject base)
    {
        super(base);
    }

    public String getFrozenPrimaryType()
    {
        return getSemanticObject().getProperty(jcr_frozenPrimaryType);
    }

    public void setFrozenPrimaryType(String frozenPrimaryType)
    {
        getSemanticObject().setProperty(jcr_frozenPrimaryType, frozenPrimaryType);
    }

    public String getFrozenMixinTypes()
    {
        return getSemanticObject().getProperty(jcr_frozenMixinTypes);
    }

    public void setFrozenMixinTypes(String frozenMixinTypes)
    {
        getSemanticObject().setProperty(jcr_frozenMixinTypes, frozenMixinTypes);
    }

    public String getFrozenUuid()
    {
        return getSemanticObject().getProperty(jcr_frozenUuid);
    }

    public void setFrozenUuid(String frozenUuid)
    {
        getSemanticObject().setProperty(jcr_frozenUuid, frozenUuid);
    }

    public String getUuid()
    {
        return getSemanticObject().getProperty(jcr_uuid);
    }

    public void setUuid(String uuid)
    {
        getSemanticObject().setProperty(jcr_uuid, uuid);
    }
}

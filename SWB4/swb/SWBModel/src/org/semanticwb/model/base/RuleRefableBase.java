package org.semanticwb.model.base;

public interface RuleRefableBase extends org.semanticwb.model.Referensable
{
   /**
   * Referencia a un objeto de tipo Rule 
   */
    public static final org.semanticwb.platform.SemanticClass swb_RuleRef=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/ontology#RuleRef");
    public static final org.semanticwb.platform.SemanticProperty swb_hasRuleRef=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/ontology#hasRuleRef");
    public static final org.semanticwb.platform.SemanticClass swb_RuleRefable=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/ontology#RuleRefable");

    public org.semanticwb.model.GenericIterator<org.semanticwb.model.RuleRef> listRuleRefs();
    public boolean hasRuleRef(org.semanticwb.model.RuleRef value);
    public org.semanticwb.model.GenericIterator<org.semanticwb.model.RuleRef> listInheritRuleRefs();

   /**
   * Adds the RuleRef
   * @param value An instance of org.semanticwb.model.RuleRef
   */
    public void addRuleRef(org.semanticwb.model.RuleRef value);

   /**
   * Remove all the values for the property RuleRef
   */
    public void removeAllRuleRef();

   /**
   * Remove a value from the property RuleRef
   * @param value An instance of org.semanticwb.model.RuleRef
   */
    public void removeRuleRef(org.semanticwb.model.RuleRef value);

/**
* Gets the RuleRef
* @return a instance of org.semanticwb.model.RuleRef
*/
    public org.semanticwb.model.RuleRef getRuleRef();
}

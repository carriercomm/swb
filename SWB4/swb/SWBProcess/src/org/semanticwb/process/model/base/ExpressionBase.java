package org.semanticwb.process.model.base;


public abstract class ExpressionBase extends org.semanticwb.process.model.SupportingElement implements org.semanticwb.model.Descriptiveable
{
       public static final org.semanticwb.platform.SemanticProperty swp_expressionBody=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/swp#expressionBody");
       public static final org.semanticwb.platform.SemanticProperty swp_expressionLanguage=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/swp#expressionLanguage");
       public static final org.semanticwb.platform.SemanticClass swp_Expression=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/swp#Expression");
       public static final org.semanticwb.platform.SemanticClass sclass=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/swp#Expression");
    public static class ClassMgr
    {

       public static java.util.Iterator<org.semanticwb.process.model.Expression> listExpressions(org.semanticwb.model.SWBModel model)
       {
           java.util.Iterator it=model.getSemanticObject().getModel().listInstancesOfClass(sclass);
           return new org.semanticwb.model.GenericIterator<org.semanticwb.process.model.Expression>(it, true);
       }

       public static java.util.Iterator<org.semanticwb.process.model.Expression> listExpressions()
       {
           java.util.Iterator it=sclass.listInstances();
           return new org.semanticwb.model.GenericIterator<org.semanticwb.process.model.Expression>(it, true);
       }

       public static org.semanticwb.process.model.Expression createExpression(org.semanticwb.model.SWBModel model)
       {
           long id=model.getSemanticObject().getModel().getCounter(sclass);
           return org.semanticwb.process.model.Expression.ClassMgr.createExpression(String.valueOf(id), model);
       }

       public static org.semanticwb.process.model.Expression getExpression(String id, org.semanticwb.model.SWBModel model)
       {
           return (org.semanticwb.process.model.Expression)model.getSemanticObject().getModel().getGenericObject(model.getSemanticObject().getModel().getObjectUri(id,sclass),sclass);
       }

       public static org.semanticwb.process.model.Expression createExpression(String id, org.semanticwb.model.SWBModel model)
       {
           return (org.semanticwb.process.model.Expression)model.getSemanticObject().getModel().createGenericObject(model.getSemanticObject().getModel().getObjectUri(id, sclass), sclass);
       }

       public static void removeExpression(String id, org.semanticwb.model.SWBModel model)
       {
           model.getSemanticObject().getModel().removeSemanticObject(model.getSemanticObject().getModel().getObjectUri(id,sclass));
       }

       public static boolean hasExpression(String id, org.semanticwb.model.SWBModel model)
       {
           return (getExpression(id, model)!=null);
       }
   public static java.util.Iterator<org.semanticwb.process.model.Expression> listExpressionByCategory(org.semanticwb.process.model.Category hascategory,org.semanticwb.model.SWBModel model)
   {
       org.semanticwb.model.GenericIterator<org.semanticwb.process.model.Expression> it=new org.semanticwb.model.GenericIterator(model.getSemanticObject().getModel().listSubjects(swp_hasCategory, hascategory.getSemanticObject()));
       return it;
   }

   public static java.util.Iterator<org.semanticwb.process.model.Expression> listExpressionByCategory(org.semanticwb.process.model.Category hascategory)
   {
       org.semanticwb.model.GenericIterator<org.semanticwb.process.model.Expression> it=new org.semanticwb.model.GenericIterator(hascategory.getSemanticObject().getModel().listSubjects(swp_hasCategory,hascategory.getSemanticObject()));
       return it;
   }
    }

    public ExpressionBase(org.semanticwb.platform.SemanticObject base)
    {
        super(base);
    }

    public String getExpressionBody()
    {
        return getSemanticObject().getProperty(swp_expressionBody);
    }

    public void setExpressionBody(String value)
    {
        getSemanticObject().setProperty(swp_expressionBody, value);
    }

    public String getExpressionLanguage()
    {
        return getSemanticObject().getProperty(swp_expressionLanguage);
    }

    public void setExpressionLanguage(String value)
    {
        getSemanticObject().setProperty(swp_expressionLanguage, value);
    }
}

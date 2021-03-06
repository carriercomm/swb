package org.semanticwb.process.schema.base;


public abstract class URLBase extends org.semanticwb.process.model.DataTypes 
{
    public static final org.semanticwb.platform.SemanticClass swp_RepositoryURL=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process#RepositoryURL");
    public static final org.semanticwb.platform.SemanticProperty swp_repositoryURLRef=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/process#repositoryURLRef");
    public static final org.semanticwb.platform.SemanticProperty swp_urlValue=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/process#urlValue");
    public static final org.semanticwb.platform.SemanticClass swps_URL=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process/schema#URL");
   /**
   * The semantic class that represents the currentObject
   */
    public static final org.semanticwb.platform.SemanticClass sclass=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process/schema#URL");

    public static class ClassMgr
    {
       /**
       * Returns a list of URL for a model
       * @param model Model to find
       * @return Iterator of org.semanticwb.process.schema.URL
       */

        public static java.util.Iterator<org.semanticwb.process.schema.URL> listURLs(org.semanticwb.model.SWBModel model)
        {
            java.util.Iterator it=model.getSemanticObject().getModel().listInstancesOfClass(sclass);
            return new org.semanticwb.model.GenericIterator<org.semanticwb.process.schema.URL>(it, true);
        }
       /**
       * Returns a list of org.semanticwb.process.schema.URL for all models
       * @return Iterator of org.semanticwb.process.schema.URL
       */

        public static java.util.Iterator<org.semanticwb.process.schema.URL> listURLs()
        {
            java.util.Iterator it=sclass.listInstances();
            return new org.semanticwb.model.GenericIterator<org.semanticwb.process.schema.URL>(it, true);
        }
       /**
       * Gets a org.semanticwb.process.schema.URL
       * @param id Identifier for org.semanticwb.process.schema.URL
       * @param model Model of the org.semanticwb.process.schema.URL
       * @return A org.semanticwb.process.schema.URL
       */
        public static org.semanticwb.process.schema.URL getURL(String id, org.semanticwb.model.SWBModel model)
        {
            return (org.semanticwb.process.schema.URL)model.getSemanticObject().getModel().getGenericObject(model.getSemanticObject().getModel().getObjectUri(id,sclass),sclass);
        }
       /**
       * Create a org.semanticwb.process.schema.URL
       * @param id Identifier for org.semanticwb.process.schema.URL
       * @param model Model of the org.semanticwb.process.schema.URL
       * @return A org.semanticwb.process.schema.URL
       */
        public static org.semanticwb.process.schema.URL createURL(String id, org.semanticwb.model.SWBModel model)
        {
            return (org.semanticwb.process.schema.URL)model.getSemanticObject().getModel().createGenericObject(model.getSemanticObject().getModel().getObjectUri(id,sclass),sclass);
        }
       /**
       * Remove a org.semanticwb.process.schema.URL
       * @param id Identifier for org.semanticwb.process.schema.URL
       * @param model Model of the org.semanticwb.process.schema.URL
       */
        public static void removeURL(String id, org.semanticwb.model.SWBModel model)
        {
            model.getSemanticObject().getModel().removeSemanticObject(model.getSemanticObject().getModel().getObjectUri(id,sclass));
        }
       /**
       * Returns true if exists a org.semanticwb.process.schema.URL
       * @param id Identifier for org.semanticwb.process.schema.URL
       * @param model Model of the org.semanticwb.process.schema.URL
       * @return true if the org.semanticwb.process.schema.URL exists, false otherwise
       */

        public static boolean hasURL(String id, org.semanticwb.model.SWBModel model)
        {
            return (getURL(id, model)!=null);
        }
       /**
       * Gets all org.semanticwb.process.schema.URL with a determined RepositoryURL
       * @param value RepositoryURL of the type org.semanticwb.process.model.RepositoryURL
       * @param model Model of the org.semanticwb.process.schema.URL
       * @return Iterator with all the org.semanticwb.process.schema.URL
       */

        public static java.util.Iterator<org.semanticwb.process.schema.URL> listURLByRepositoryURL(org.semanticwb.process.model.RepositoryURL value,org.semanticwb.model.SWBModel model)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.process.schema.URL> it=new org.semanticwb.model.GenericIterator(model.getSemanticObject().getModel().listSubjectsByClass(swp_repositoryURLRef, value.getSemanticObject(),sclass));
            return it;
        }
       /**
       * Gets all org.semanticwb.process.schema.URL with a determined RepositoryURL
       * @param value RepositoryURL of the type org.semanticwb.process.model.RepositoryURL
       * @return Iterator with all the org.semanticwb.process.schema.URL
       */

        public static java.util.Iterator<org.semanticwb.process.schema.URL> listURLByRepositoryURL(org.semanticwb.process.model.RepositoryURL value)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.process.schema.URL> it=new org.semanticwb.model.GenericIterator(value.getSemanticObject().getModel().listSubjectsByClass(swp_repositoryURLRef,value.getSemanticObject(),sclass));
            return it;
        }
    }

    public static URLBase.ClassMgr getURLClassMgr()
    {
        return new URLBase.ClassMgr();
    }

   /**
   * Constructs a URLBase with a SemanticObject
   * @param base The SemanticObject with the properties for the URL
   */
    public URLBase(org.semanticwb.platform.SemanticObject base)
    {
        super(base);
    }
   /**
   * Sets the value for the property RepositoryURL
   * @param value RepositoryURL to set
   */

    public void setRepositoryURL(org.semanticwb.process.model.RepositoryURL value)
    {
        if(value!=null)
        {
            getSemanticObject().setObjectProperty(swp_repositoryURLRef, value.getSemanticObject());
        }else
        {
            removeRepositoryURL();
        }
    }
   /**
   * Remove the value for RepositoryURL property
   */

    public void removeRepositoryURL()
    {
        getSemanticObject().removeProperty(swp_repositoryURLRef);
    }

   /**
   * Gets the RepositoryURL
   * @return a org.semanticwb.process.model.RepositoryURL
   */
    public org.semanticwb.process.model.RepositoryURL getRepositoryURL()
    {
         org.semanticwb.process.model.RepositoryURL ret=null;
         org.semanticwb.platform.SemanticObject obj=getSemanticObject().getObjectProperty(swp_repositoryURLRef);
         if(obj!=null)
         {
             ret=(org.semanticwb.process.model.RepositoryURL)obj.createGenericInstance();
         }
         return ret;
    }

/**
* Gets the Value property
* @return String with the Value
*/
    public String getValue()
    {
        return getSemanticObject().getProperty(swp_urlValue);
    }

/**
* Sets the Value property
* @param value long with the Value
*/
    public void setValue(String value)
    {
        getSemanticObject().setProperty(swp_urlValue, value);
    }
}

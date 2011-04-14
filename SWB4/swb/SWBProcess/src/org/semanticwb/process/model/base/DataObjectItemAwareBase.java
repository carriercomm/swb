package org.semanticwb.process.model.base;


public abstract class DataObjectItemAwareBase extends org.semanticwb.process.model.ItemAware implements org.semanticwb.model.Traceable,org.semanticwb.model.Descriptiveable
{
    public static final org.semanticwb.platform.SemanticClass swb_Class=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/ontology#Class");
    public static final org.semanticwb.platform.SemanticProperty swp_dataObjectClass=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/process#dataObjectClass");
    public static final org.semanticwb.platform.SemanticClass owl_DatatypeProperty=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.w3.org/2002/07/owl#DatatypeProperty");
    public static final org.semanticwb.platform.SemanticProperty swp_dataObjectProperty=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/process#dataObjectProperty");
    public static final org.semanticwb.platform.SemanticClass swp_DataObjectItemAware=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process#DataObjectItemAware");
   /**
   * The semantic class that represents the currentObject
   */
    public static final org.semanticwb.platform.SemanticClass sclass=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process#DataObjectItemAware");

    public static class ClassMgr
    {
       /**
       * Returns a list of DataObjectItemAware for a model
       * @param model Model to find
       * @return Iterator of org.semanticwb.process.model.DataObjectItemAware
       */

        public static java.util.Iterator<org.semanticwb.process.model.DataObjectItemAware> listDataObjectItemAwares(org.semanticwb.model.SWBModel model)
        {
            java.util.Iterator it=model.getSemanticObject().getModel().listInstancesOfClass(sclass);
            return new org.semanticwb.model.GenericIterator<org.semanticwb.process.model.DataObjectItemAware>(it, true);
        }
       /**
       * Returns a list of org.semanticwb.process.model.DataObjectItemAware for all models
       * @return Iterator of org.semanticwb.process.model.DataObjectItemAware
       */

        public static java.util.Iterator<org.semanticwb.process.model.DataObjectItemAware> listDataObjectItemAwares()
        {
            java.util.Iterator it=sclass.listInstances();
            return new org.semanticwb.model.GenericIterator<org.semanticwb.process.model.DataObjectItemAware>(it, true);
        }
       /**
       * Gets a org.semanticwb.process.model.DataObjectItemAware
       * @param id Identifier for org.semanticwb.process.model.DataObjectItemAware
       * @param model Model of the org.semanticwb.process.model.DataObjectItemAware
       * @return A org.semanticwb.process.model.DataObjectItemAware
       */
        public static org.semanticwb.process.model.DataObjectItemAware getDataObjectItemAware(String id, org.semanticwb.model.SWBModel model)
        {
            return (org.semanticwb.process.model.DataObjectItemAware)model.getSemanticObject().getModel().getGenericObject(model.getSemanticObject().getModel().getObjectUri(id,sclass),sclass);
        }
       /**
       * Create a org.semanticwb.process.model.DataObjectItemAware
       * @param id Identifier for org.semanticwb.process.model.DataObjectItemAware
       * @param model Model of the org.semanticwb.process.model.DataObjectItemAware
       * @return A org.semanticwb.process.model.DataObjectItemAware
       */
        public static org.semanticwb.process.model.DataObjectItemAware createDataObjectItemAware(String id, org.semanticwb.model.SWBModel model)
        {
            return (org.semanticwb.process.model.DataObjectItemAware)model.getSemanticObject().getModel().createGenericObject(model.getSemanticObject().getModel().getObjectUri(id,sclass),sclass);
        }
       /**
       * Remove a org.semanticwb.process.model.DataObjectItemAware
       * @param id Identifier for org.semanticwb.process.model.DataObjectItemAware
       * @param model Model of the org.semanticwb.process.model.DataObjectItemAware
       */
        public static void removeDataObjectItemAware(String id, org.semanticwb.model.SWBModel model)
        {
            model.getSemanticObject().getModel().removeSemanticObject(model.getSemanticObject().getModel().getObjectUri(id,sclass));
        }
       /**
       * Returns true if exists a org.semanticwb.process.model.DataObjectItemAware
       * @param id Identifier for org.semanticwb.process.model.DataObjectItemAware
       * @param model Model of the org.semanticwb.process.model.DataObjectItemAware
       * @return true if the org.semanticwb.process.model.DataObjectItemAware exists, false otherwise
       */

        public static boolean hasDataObjectItemAware(String id, org.semanticwb.model.SWBModel model)
        {
            return (getDataObjectItemAware(id, model)!=null);
        }
       /**
       * Gets all org.semanticwb.process.model.DataObjectItemAware with a determined Container
       * @param value Container of the type org.semanticwb.process.model.Containerable
       * @param model Model of the org.semanticwb.process.model.DataObjectItemAware
       * @return Iterator with all the org.semanticwb.process.model.DataObjectItemAware
       */

        public static java.util.Iterator<org.semanticwb.process.model.DataObjectItemAware> listDataObjectItemAwareByContainer(org.semanticwb.process.model.Containerable value,org.semanticwb.model.SWBModel model)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.process.model.DataObjectItemAware> it=new org.semanticwb.model.GenericIterator(model.getSemanticObject().getModel().listSubjectsByClass(swp_container, value.getSemanticObject(),sclass));
            return it;
        }
       /**
       * Gets all org.semanticwb.process.model.DataObjectItemAware with a determined Container
       * @param value Container of the type org.semanticwb.process.model.Containerable
       * @return Iterator with all the org.semanticwb.process.model.DataObjectItemAware
       */

        public static java.util.Iterator<org.semanticwb.process.model.DataObjectItemAware> listDataObjectItemAwareByContainer(org.semanticwb.process.model.Containerable value)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.process.model.DataObjectItemAware> it=new org.semanticwb.model.GenericIterator(value.getSemanticObject().getModel().listSubjectsByClass(swp_container,value.getSemanticObject(),sclass));
            return it;
        }
       /**
       * Gets all org.semanticwb.process.model.DataObjectItemAware with a determined ModifiedBy
       * @param value ModifiedBy of the type org.semanticwb.model.User
       * @param model Model of the org.semanticwb.process.model.DataObjectItemAware
       * @return Iterator with all the org.semanticwb.process.model.DataObjectItemAware
       */

        public static java.util.Iterator<org.semanticwb.process.model.DataObjectItemAware> listDataObjectItemAwareByModifiedBy(org.semanticwb.model.User value,org.semanticwb.model.SWBModel model)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.process.model.DataObjectItemAware> it=new org.semanticwb.model.GenericIterator(model.getSemanticObject().getModel().listSubjectsByClass(swb_modifiedBy, value.getSemanticObject(),sclass));
            return it;
        }
       /**
       * Gets all org.semanticwb.process.model.DataObjectItemAware with a determined ModifiedBy
       * @param value ModifiedBy of the type org.semanticwb.model.User
       * @return Iterator with all the org.semanticwb.process.model.DataObjectItemAware
       */

        public static java.util.Iterator<org.semanticwb.process.model.DataObjectItemAware> listDataObjectItemAwareByModifiedBy(org.semanticwb.model.User value)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.process.model.DataObjectItemAware> it=new org.semanticwb.model.GenericIterator(value.getSemanticObject().getModel().listSubjectsByClass(swb_modifiedBy,value.getSemanticObject(),sclass));
            return it;
        }
       /**
       * Gets all org.semanticwb.process.model.DataObjectItemAware with a determined OutputConnectionObject
       * @param value OutputConnectionObject of the type org.semanticwb.process.model.ConnectionObject
       * @param model Model of the org.semanticwb.process.model.DataObjectItemAware
       * @return Iterator with all the org.semanticwb.process.model.DataObjectItemAware
       */

        public static java.util.Iterator<org.semanticwb.process.model.DataObjectItemAware> listDataObjectItemAwareByOutputConnectionObject(org.semanticwb.process.model.ConnectionObject value,org.semanticwb.model.SWBModel model)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.process.model.DataObjectItemAware> it=new org.semanticwb.model.GenericIterator(model.getSemanticObject().getModel().listSubjectsByClass(swp_hasOutputConnectionObjectInv, value.getSemanticObject(),sclass));
            return it;
        }
       /**
       * Gets all org.semanticwb.process.model.DataObjectItemAware with a determined OutputConnectionObject
       * @param value OutputConnectionObject of the type org.semanticwb.process.model.ConnectionObject
       * @return Iterator with all the org.semanticwb.process.model.DataObjectItemAware
       */

        public static java.util.Iterator<org.semanticwb.process.model.DataObjectItemAware> listDataObjectItemAwareByOutputConnectionObject(org.semanticwb.process.model.ConnectionObject value)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.process.model.DataObjectItemAware> it=new org.semanticwb.model.GenericIterator(value.getSemanticObject().getModel().listSubjectsByClass(swp_hasOutputConnectionObjectInv,value.getSemanticObject(),sclass));
            return it;
        }
       /**
       * Gets all org.semanticwb.process.model.DataObjectItemAware with a determined InputConnectionObject
       * @param value InputConnectionObject of the type org.semanticwb.process.model.ConnectionObject
       * @param model Model of the org.semanticwb.process.model.DataObjectItemAware
       * @return Iterator with all the org.semanticwb.process.model.DataObjectItemAware
       */

        public static java.util.Iterator<org.semanticwb.process.model.DataObjectItemAware> listDataObjectItemAwareByInputConnectionObject(org.semanticwb.process.model.ConnectionObject value,org.semanticwb.model.SWBModel model)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.process.model.DataObjectItemAware> it=new org.semanticwb.model.GenericIterator(model.getSemanticObject().getModel().listSubjectsByClass(swp_hasInputConnectionObjectInv, value.getSemanticObject(),sclass));
            return it;
        }
       /**
       * Gets all org.semanticwb.process.model.DataObjectItemAware with a determined InputConnectionObject
       * @param value InputConnectionObject of the type org.semanticwb.process.model.ConnectionObject
       * @return Iterator with all the org.semanticwb.process.model.DataObjectItemAware
       */

        public static java.util.Iterator<org.semanticwb.process.model.DataObjectItemAware> listDataObjectItemAwareByInputConnectionObject(org.semanticwb.process.model.ConnectionObject value)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.process.model.DataObjectItemAware> it=new org.semanticwb.model.GenericIterator(value.getSemanticObject().getModel().listSubjectsByClass(swp_hasInputConnectionObjectInv,value.getSemanticObject(),sclass));
            return it;
        }
       /**
       * Gets all org.semanticwb.process.model.DataObjectItemAware with a determined Child
       * @param value Child of the type org.semanticwb.process.model.GraphicalElement
       * @param model Model of the org.semanticwb.process.model.DataObjectItemAware
       * @return Iterator with all the org.semanticwb.process.model.DataObjectItemAware
       */

        public static java.util.Iterator<org.semanticwb.process.model.DataObjectItemAware> listDataObjectItemAwareByChild(org.semanticwb.process.model.GraphicalElement value,org.semanticwb.model.SWBModel model)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.process.model.DataObjectItemAware> it=new org.semanticwb.model.GenericIterator(model.getSemanticObject().getModel().listSubjectsByClass(swp_hasChildInv, value.getSemanticObject(),sclass));
            return it;
        }
       /**
       * Gets all org.semanticwb.process.model.DataObjectItemAware with a determined Child
       * @param value Child of the type org.semanticwb.process.model.GraphicalElement
       * @return Iterator with all the org.semanticwb.process.model.DataObjectItemAware
       */

        public static java.util.Iterator<org.semanticwb.process.model.DataObjectItemAware> listDataObjectItemAwareByChild(org.semanticwb.process.model.GraphicalElement value)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.process.model.DataObjectItemAware> it=new org.semanticwb.model.GenericIterator(value.getSemanticObject().getModel().listSubjectsByClass(swp_hasChildInv,value.getSemanticObject(),sclass));
            return it;
        }
       /**
       * Gets all org.semanticwb.process.model.DataObjectItemAware with a determined Creator
       * @param value Creator of the type org.semanticwb.model.User
       * @param model Model of the org.semanticwb.process.model.DataObjectItemAware
       * @return Iterator with all the org.semanticwb.process.model.DataObjectItemAware
       */

        public static java.util.Iterator<org.semanticwb.process.model.DataObjectItemAware> listDataObjectItemAwareByCreator(org.semanticwb.model.User value,org.semanticwb.model.SWBModel model)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.process.model.DataObjectItemAware> it=new org.semanticwb.model.GenericIterator(model.getSemanticObject().getModel().listSubjectsByClass(swb_creator, value.getSemanticObject(),sclass));
            return it;
        }
       /**
       * Gets all org.semanticwb.process.model.DataObjectItemAware with a determined Creator
       * @param value Creator of the type org.semanticwb.model.User
       * @return Iterator with all the org.semanticwb.process.model.DataObjectItemAware
       */

        public static java.util.Iterator<org.semanticwb.process.model.DataObjectItemAware> listDataObjectItemAwareByCreator(org.semanticwb.model.User value)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.process.model.DataObjectItemAware> it=new org.semanticwb.model.GenericIterator(value.getSemanticObject().getModel().listSubjectsByClass(swb_creator,value.getSemanticObject(),sclass));
            return it;
        }
       /**
       * Gets all org.semanticwb.process.model.DataObjectItemAware with a determined Parent
       * @param value Parent of the type org.semanticwb.process.model.GraphicalElement
       * @param model Model of the org.semanticwb.process.model.DataObjectItemAware
       * @return Iterator with all the org.semanticwb.process.model.DataObjectItemAware
       */

        public static java.util.Iterator<org.semanticwb.process.model.DataObjectItemAware> listDataObjectItemAwareByParent(org.semanticwb.process.model.GraphicalElement value,org.semanticwb.model.SWBModel model)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.process.model.DataObjectItemAware> it=new org.semanticwb.model.GenericIterator(model.getSemanticObject().getModel().listSubjectsByClass(swp_parent, value.getSemanticObject(),sclass));
            return it;
        }
       /**
       * Gets all org.semanticwb.process.model.DataObjectItemAware with a determined Parent
       * @param value Parent of the type org.semanticwb.process.model.GraphicalElement
       * @return Iterator with all the org.semanticwb.process.model.DataObjectItemAware
       */

        public static java.util.Iterator<org.semanticwb.process.model.DataObjectItemAware> listDataObjectItemAwareByParent(org.semanticwb.process.model.GraphicalElement value)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.process.model.DataObjectItemAware> it=new org.semanticwb.model.GenericIterator(value.getSemanticObject().getModel().listSubjectsByClass(swp_parent,value.getSemanticObject(),sclass));
            return it;
        }
    }

   /**
   * Constructs a DataObjectItemAwareBase with a SemanticObject
   * @param base The SemanticObject with the properties for the DataObjectItemAware
   */
    public DataObjectItemAwareBase(org.semanticwb.platform.SemanticObject base)
    {
        super(base);
    }

    public void setDataObjectClass(org.semanticwb.platform.SemanticObject value)
    {
        getSemanticObject().setObjectProperty(swp_dataObjectClass, value);
    }

    public void removeDataObjectClass()
    {
        getSemanticObject().removeProperty(swp_dataObjectClass);
    }

/**
* Gets the DataObjectClass property
* @return the value for the property as org.semanticwb.platform.SemanticObject
*/
    public org.semanticwb.platform.SemanticObject getDataObjectClass()
    {
         org.semanticwb.platform.SemanticObject ret=null;
         ret=getSemanticObject().getObjectProperty(swp_dataObjectClass);
         return ret;
    }

    public void setDataObjectProperty(org.semanticwb.platform.SemanticObject value)
    {
        getSemanticObject().setObjectProperty(swp_dataObjectProperty, value);
    }

    public void removeDataObjectProperty()
    {
        getSemanticObject().removeProperty(swp_dataObjectProperty);
    }

/**
* Gets the DataObjectProperty property
* @return the value for the property as org.semanticwb.platform.SemanticObject
*/
    public org.semanticwb.platform.SemanticObject getDataObjectProperty()
    {
         org.semanticwb.platform.SemanticObject ret=null;
         ret=getSemanticObject().getObjectProperty(swp_dataObjectProperty);
         return ret;
    }
}

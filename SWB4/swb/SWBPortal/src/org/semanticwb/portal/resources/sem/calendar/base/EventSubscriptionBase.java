package org.semanticwb.portal.resources.sem.calendar.base;


   /**
   * Almacena la subscripción de un usuario a un Evento 
   */
public abstract class EventSubscriptionBase extends org.semanticwb.model.SWBClass implements org.semanticwb.model.Traceable
{
   /**
   * Un usuario es una persona que tiene relación con el portal a través de un método de acceso.
   */
    public static final org.semanticwb.platform.SemanticClass swb_User=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/ontology#User");
   /**
   * Almacena el usuario subscrito a un Evento
   */
    public static final org.semanticwb.platform.SemanticProperty cal_userEventSubscription=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/calendar#userEventSubscription");
   /**
   * Define la estructura de datos de los eventos a mostrar en el calendario.
   */
    public static final org.semanticwb.platform.SemanticClass cal_Event=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/calendar#Event");
   /**
   * Almacena el evento al que se subscribió un usuario
   */
    public static final org.semanticwb.platform.SemanticProperty cal_eventSubscription=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/calendar#eventSubscription");
   /**
   * Almacena la subscripción de un usuario a un Evento
   */
    public static final org.semanticwb.platform.SemanticClass cal_EventSubscription=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/calendar#EventSubscription");
   /**
   * The semantic class that represents the currentObject
   */
    public static final org.semanticwb.platform.SemanticClass sclass=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/calendar#EventSubscription");

    public static class ClassMgr
    {
       /**
       * Returns a list of EventSubscription for a model
       * @param model Model to find
       * @return Iterator of org.semanticwb.portal.resources.sem.calendar.EventSubscription
       */

        public static java.util.Iterator<org.semanticwb.portal.resources.sem.calendar.EventSubscription> listEventSubscriptions(org.semanticwb.model.SWBModel model)
        {
            java.util.Iterator it=model.getSemanticObject().getModel().listInstancesOfClass(sclass);
            return new org.semanticwb.model.GenericIterator<org.semanticwb.portal.resources.sem.calendar.EventSubscription>(it, true);
        }
       /**
       * Returns a list of org.semanticwb.portal.resources.sem.calendar.EventSubscription for all models
       * @return Iterator of org.semanticwb.portal.resources.sem.calendar.EventSubscription
       */

        public static java.util.Iterator<org.semanticwb.portal.resources.sem.calendar.EventSubscription> listEventSubscriptions()
        {
            java.util.Iterator it=sclass.listInstances();
            return new org.semanticwb.model.GenericIterator<org.semanticwb.portal.resources.sem.calendar.EventSubscription>(it, true);
        }

        public static org.semanticwb.portal.resources.sem.calendar.EventSubscription createEventSubscription(org.semanticwb.model.SWBModel model)
        {
            long id=model.getSemanticObject().getModel().getCounter(sclass);
            return org.semanticwb.portal.resources.sem.calendar.EventSubscription.ClassMgr.createEventSubscription(String.valueOf(id), model);
        }
       /**
       * Gets a org.semanticwb.portal.resources.sem.calendar.EventSubscription
       * @param id Identifier for org.semanticwb.portal.resources.sem.calendar.EventSubscription
       * @param model Model of the org.semanticwb.portal.resources.sem.calendar.EventSubscription
       * @return A org.semanticwb.portal.resources.sem.calendar.EventSubscription
       */
        public static org.semanticwb.portal.resources.sem.calendar.EventSubscription getEventSubscription(String id, org.semanticwb.model.SWBModel model)
        {
            return (org.semanticwb.portal.resources.sem.calendar.EventSubscription)model.getSemanticObject().getModel().getGenericObject(model.getSemanticObject().getModel().getObjectUri(id,sclass),sclass);
        }
       /**
       * Create a org.semanticwb.portal.resources.sem.calendar.EventSubscription
       * @param id Identifier for org.semanticwb.portal.resources.sem.calendar.EventSubscription
       * @param model Model of the org.semanticwb.portal.resources.sem.calendar.EventSubscription
       * @return A org.semanticwb.portal.resources.sem.calendar.EventSubscription
       */
        public static org.semanticwb.portal.resources.sem.calendar.EventSubscription createEventSubscription(String id, org.semanticwb.model.SWBModel model)
        {
            return (org.semanticwb.portal.resources.sem.calendar.EventSubscription)model.getSemanticObject().getModel().createGenericObject(model.getSemanticObject().getModel().getObjectUri(id,sclass),sclass);
        }
       /**
       * Remove a org.semanticwb.portal.resources.sem.calendar.EventSubscription
       * @param id Identifier for org.semanticwb.portal.resources.sem.calendar.EventSubscription
       * @param model Model of the org.semanticwb.portal.resources.sem.calendar.EventSubscription
       */
        public static void removeEventSubscription(String id, org.semanticwb.model.SWBModel model)
        {
            model.getSemanticObject().getModel().removeSemanticObject(model.getSemanticObject().getModel().getObjectUri(id,sclass));
        }
       /**
       * Returns true if exists a org.semanticwb.portal.resources.sem.calendar.EventSubscription
       * @param id Identifier for org.semanticwb.portal.resources.sem.calendar.EventSubscription
       * @param model Model of the org.semanticwb.portal.resources.sem.calendar.EventSubscription
       * @return true if the org.semanticwb.portal.resources.sem.calendar.EventSubscription exists, false otherwise
       */

        public static boolean hasEventSubscription(String id, org.semanticwb.model.SWBModel model)
        {
            return (getEventSubscription(id, model)!=null);
        }
       /**
       * Gets all org.semanticwb.portal.resources.sem.calendar.EventSubscription with a determined UserEventSubscription
       * @param value UserEventSubscription of the type org.semanticwb.model.User
       * @param model Model of the org.semanticwb.portal.resources.sem.calendar.EventSubscription
       * @return Iterator with all the org.semanticwb.portal.resources.sem.calendar.EventSubscription
       */

        public static java.util.Iterator<org.semanticwb.portal.resources.sem.calendar.EventSubscription> listEventSubscriptionByUserEventSubscription(org.semanticwb.model.User value,org.semanticwb.model.SWBModel model)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.portal.resources.sem.calendar.EventSubscription> it=new org.semanticwb.model.GenericIterator(model.getSemanticObject().getModel().listSubjectsByClass(cal_userEventSubscription, value.getSemanticObject(),sclass));
            return it;
        }
       /**
       * Gets all org.semanticwb.portal.resources.sem.calendar.EventSubscription with a determined UserEventSubscription
       * @param value UserEventSubscription of the type org.semanticwb.model.User
       * @return Iterator with all the org.semanticwb.portal.resources.sem.calendar.EventSubscription
       */

        public static java.util.Iterator<org.semanticwb.portal.resources.sem.calendar.EventSubscription> listEventSubscriptionByUserEventSubscription(org.semanticwb.model.User value)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.portal.resources.sem.calendar.EventSubscription> it=new org.semanticwb.model.GenericIterator(value.getSemanticObject().getModel().listSubjectsByClass(cal_userEventSubscription,value.getSemanticObject(),sclass));
            return it;
        }
       /**
       * Gets all org.semanticwb.portal.resources.sem.calendar.EventSubscription with a determined ModifiedBy
       * @param value ModifiedBy of the type org.semanticwb.model.User
       * @param model Model of the org.semanticwb.portal.resources.sem.calendar.EventSubscription
       * @return Iterator with all the org.semanticwb.portal.resources.sem.calendar.EventSubscription
       */

        public static java.util.Iterator<org.semanticwb.portal.resources.sem.calendar.EventSubscription> listEventSubscriptionByModifiedBy(org.semanticwb.model.User value,org.semanticwb.model.SWBModel model)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.portal.resources.sem.calendar.EventSubscription> it=new org.semanticwb.model.GenericIterator(model.getSemanticObject().getModel().listSubjectsByClass(swb_modifiedBy, value.getSemanticObject(),sclass));
            return it;
        }
       /**
       * Gets all org.semanticwb.portal.resources.sem.calendar.EventSubscription with a determined ModifiedBy
       * @param value ModifiedBy of the type org.semanticwb.model.User
       * @return Iterator with all the org.semanticwb.portal.resources.sem.calendar.EventSubscription
       */

        public static java.util.Iterator<org.semanticwb.portal.resources.sem.calendar.EventSubscription> listEventSubscriptionByModifiedBy(org.semanticwb.model.User value)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.portal.resources.sem.calendar.EventSubscription> it=new org.semanticwb.model.GenericIterator(value.getSemanticObject().getModel().listSubjectsByClass(swb_modifiedBy,value.getSemanticObject(),sclass));
            return it;
        }
       /**
       * Gets all org.semanticwb.portal.resources.sem.calendar.EventSubscription with a determined Creator
       * @param value Creator of the type org.semanticwb.model.User
       * @param model Model of the org.semanticwb.portal.resources.sem.calendar.EventSubscription
       * @return Iterator with all the org.semanticwb.portal.resources.sem.calendar.EventSubscription
       */

        public static java.util.Iterator<org.semanticwb.portal.resources.sem.calendar.EventSubscription> listEventSubscriptionByCreator(org.semanticwb.model.User value,org.semanticwb.model.SWBModel model)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.portal.resources.sem.calendar.EventSubscription> it=new org.semanticwb.model.GenericIterator(model.getSemanticObject().getModel().listSubjectsByClass(swb_creator, value.getSemanticObject(),sclass));
            return it;
        }
       /**
       * Gets all org.semanticwb.portal.resources.sem.calendar.EventSubscription with a determined Creator
       * @param value Creator of the type org.semanticwb.model.User
       * @return Iterator with all the org.semanticwb.portal.resources.sem.calendar.EventSubscription
       */

        public static java.util.Iterator<org.semanticwb.portal.resources.sem.calendar.EventSubscription> listEventSubscriptionByCreator(org.semanticwb.model.User value)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.portal.resources.sem.calendar.EventSubscription> it=new org.semanticwb.model.GenericIterator(value.getSemanticObject().getModel().listSubjectsByClass(swb_creator,value.getSemanticObject(),sclass));
            return it;
        }
       /**
       * Gets all org.semanticwb.portal.resources.sem.calendar.EventSubscription with a determined EventSubscription
       * @param value EventSubscription of the type org.semanticwb.portal.resources.sem.calendar.Event
       * @param model Model of the org.semanticwb.portal.resources.sem.calendar.EventSubscription
       * @return Iterator with all the org.semanticwb.portal.resources.sem.calendar.EventSubscription
       */

        public static java.util.Iterator<org.semanticwb.portal.resources.sem.calendar.EventSubscription> listEventSubscriptionByEventSubscription(org.semanticwb.portal.resources.sem.calendar.Event value,org.semanticwb.model.SWBModel model)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.portal.resources.sem.calendar.EventSubscription> it=new org.semanticwb.model.GenericIterator(model.getSemanticObject().getModel().listSubjectsByClass(cal_eventSubscription, value.getSemanticObject(),sclass));
            return it;
        }
       /**
       * Gets all org.semanticwb.portal.resources.sem.calendar.EventSubscription with a determined EventSubscription
       * @param value EventSubscription of the type org.semanticwb.portal.resources.sem.calendar.Event
       * @return Iterator with all the org.semanticwb.portal.resources.sem.calendar.EventSubscription
       */

        public static java.util.Iterator<org.semanticwb.portal.resources.sem.calendar.EventSubscription> listEventSubscriptionByEventSubscription(org.semanticwb.portal.resources.sem.calendar.Event value)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.portal.resources.sem.calendar.EventSubscription> it=new org.semanticwb.model.GenericIterator(value.getSemanticObject().getModel().listSubjectsByClass(cal_eventSubscription,value.getSemanticObject(),sclass));
            return it;
        }
    }

   /**
   * Constructs a EventSubscriptionBase with a SemanticObject
   * @param base The SemanticObject with the properties for the EventSubscription
   */
    public EventSubscriptionBase(org.semanticwb.platform.SemanticObject base)
    {
        super(base);
    }
   /**
   * Sets the value for the property UserEventSubscription
   * @param value UserEventSubscription to set
   */

    public void setUserEventSubscription(org.semanticwb.model.User value)
    {
        if(value!=null)
        {
            getSemanticObject().setObjectProperty(cal_userEventSubscription, value.getSemanticObject());
        }else
        {
            removeUserEventSubscription();
        }
    }
   /**
   * Remove the value for UserEventSubscription property
   */

    public void removeUserEventSubscription()
    {
        getSemanticObject().removeProperty(cal_userEventSubscription);
    }

   /**
   * Gets the UserEventSubscription
   * @return a org.semanticwb.model.User
   */
    public org.semanticwb.model.User getUserEventSubscription()
    {
         org.semanticwb.model.User ret=null;
         org.semanticwb.platform.SemanticObject obj=getSemanticObject().getObjectProperty(cal_userEventSubscription);
         if(obj!=null)
         {
             ret=(org.semanticwb.model.User)obj.createGenericInstance();
         }
         return ret;
    }
   /**
   * Sets the value for the property ModifiedBy
   * @param value ModifiedBy to set
   */

    public void setModifiedBy(org.semanticwb.model.User value)
    {
        if(value!=null)
        {
            getSemanticObject().setObjectProperty(swb_modifiedBy, value.getSemanticObject());
        }else
        {
            removeModifiedBy();
        }
    }
   /**
   * Remove the value for ModifiedBy property
   */

    public void removeModifiedBy()
    {
        getSemanticObject().removeProperty(swb_modifiedBy);
    }

   /**
   * Gets the ModifiedBy
   * @return a org.semanticwb.model.User
   */
    public org.semanticwb.model.User getModifiedBy()
    {
         org.semanticwb.model.User ret=null;
         org.semanticwb.platform.SemanticObject obj=getSemanticObject().getObjectProperty(swb_modifiedBy);
         if(obj!=null)
         {
             ret=(org.semanticwb.model.User)obj.createGenericInstance();
         }
         return ret;
    }
   /**
   * Sets the value for the property Creator
   * @param value Creator to set
   */

    public void setCreator(org.semanticwb.model.User value)
    {
        if(value!=null)
        {
            getSemanticObject().setObjectProperty(swb_creator, value.getSemanticObject());
        }else
        {
            removeCreator();
        }
    }
   /**
   * Remove the value for Creator property
   */

    public void removeCreator()
    {
        getSemanticObject().removeProperty(swb_creator);
    }

   /**
   * Gets the Creator
   * @return a org.semanticwb.model.User
   */
    public org.semanticwb.model.User getCreator()
    {
         org.semanticwb.model.User ret=null;
         org.semanticwb.platform.SemanticObject obj=getSemanticObject().getObjectProperty(swb_creator);
         if(obj!=null)
         {
             ret=(org.semanticwb.model.User)obj.createGenericInstance();
         }
         return ret;
    }

/**
* Gets the Updated property
* @return java.util.Date with the Updated
*/
    public java.util.Date getUpdated()
    {
        return getSemanticObject().getDateProperty(swb_updated);
    }

/**
* Sets the Updated property
* @param value long with the Updated
*/
    public void setUpdated(java.util.Date value)
    {
        getSemanticObject().setDateProperty(swb_updated, value);
    }

/**
* Gets the Created property
* @return java.util.Date with the Created
*/
    public java.util.Date getCreated()
    {
        return getSemanticObject().getDateProperty(swb_created);
    }

/**
* Sets the Created property
* @param value long with the Created
*/
    public void setCreated(java.util.Date value)
    {
        getSemanticObject().setDateProperty(swb_created, value);
    }
   /**
   * Sets the value for the property EventSubscription
   * @param value EventSubscription to set
   */

    public void setEventSubscription(org.semanticwb.portal.resources.sem.calendar.Event value)
    {
        if(value!=null)
        {
            getSemanticObject().setObjectProperty(cal_eventSubscription, value.getSemanticObject());
        }else
        {
            removeEventSubscription();
        }
    }
   /**
   * Remove the value for EventSubscription property
   */

    public void removeEventSubscription()
    {
        getSemanticObject().removeProperty(cal_eventSubscription);
    }

   /**
   * Gets the EventSubscription
   * @return a org.semanticwb.portal.resources.sem.calendar.Event
   */
    public org.semanticwb.portal.resources.sem.calendar.Event getEventSubscription()
    {
         org.semanticwb.portal.resources.sem.calendar.Event ret=null;
         org.semanticwb.platform.SemanticObject obj=getSemanticObject().getObjectProperty(cal_eventSubscription);
         if(obj!=null)
         {
             ret=(org.semanticwb.portal.resources.sem.calendar.Event)obj.createGenericInstance();
         }
         return ret;
    }
}

package org.semanticwb.process.base;


public class ProcessSiteBase extends org.semanticwb.model.WebSite implements org.semanticwb.model.Indexable,org.semanticwb.model.Traceable,org.semanticwb.model.Activeable,org.semanticwb.model.Localeable,org.semanticwb.model.Filterable,org.semanticwb.model.Undeleteable,org.semanticwb.model.Trashable,org.semanticwb.model.Descriptiveable
{
    public static class ClassMgr
    {
       public static final org.semanticwb.platform.SemanticProperty swb_deleted=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/ontology#deleted");
       public static final org.semanticwb.platform.SemanticProperty swb_active=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/ontology#active");
       public static final org.semanticwb.platform.SemanticClass swb_Language=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/ontology#Language");
       public static final org.semanticwb.platform.SemanticProperty swb_language=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/ontology#language");
       public static final org.semanticwb.platform.SemanticClass swb_SWBModel=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/ontology#SWBModel");
       public static final org.semanticwb.platform.SemanticProperty swb_hasSubModel=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/ontology#hasSubModel");
       public static final org.semanticwb.platform.SemanticProperty swb_undeleteable=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/ontology#undeleteable");
       public static final org.semanticwb.platform.SemanticProperty swb_created=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/ontology#created");
       public static final org.semanticwb.platform.SemanticClass swb_User=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/ontology#User");
       public static final org.semanticwb.platform.SemanticProperty swb_modifiedBy=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/ontology#modifiedBy");
       public static final org.semanticwb.platform.SemanticProperty swb_title=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/ontology#title");
       public static final org.semanticwb.platform.SemanticProperty swb_updated=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/ontology#updated");
       public static final org.semanticwb.platform.SemanticClass swb_UserRepository=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/ontology#UserRepository");
       public static final org.semanticwb.platform.SemanticProperty swb_userRepository=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/ontology#userRepository");
       public static final org.semanticwb.platform.SemanticProperty swb_indexable=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/ontology#indexable");
       public static final org.semanticwb.platform.SemanticProperty swb_creator=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/ontology#creator");
       public static final org.semanticwb.platform.SemanticClass swb_WebPage=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/ontology#WebPage");
       public static final org.semanticwb.platform.SemanticProperty swb_homePage=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/ontology#homePage");
       public static final org.semanticwb.platform.SemanticClass swb_Template=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/ontology#Template");
       public static final org.semanticwb.platform.SemanticProperty swb_defaultTemplate=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/ontology#defaultTemplate");
       public static final org.semanticwb.platform.SemanticProperty swb_description=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/ontology#description");
       public static final org.semanticwb.platform.SemanticClass swbps_XOREventGateWay=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process#XOREventGateWay");
       public static final org.semanticwb.platform.SemanticClass swbps_XORGateWay=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process#XORGateWay");
       public static final org.semanticwb.platform.SemanticClass swbps_ProcessInstance=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process#ProcessInstance");
       public static final org.semanticwb.platform.SemanticClass swbxf_FormView=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/xforms/ontology#FormView");
       public static final org.semanticwb.platform.SemanticClass swbps_InitEvent=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process#InitEvent");
       public static final org.semanticwb.platform.SemanticClass swbps_ProcessObject=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process#ProcessObject");
       public static final org.semanticwb.platform.SemanticClass swbps_FlowObjectInstance=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process#FlowObjectInstance");
       public static final org.semanticwb.platform.SemanticClass swbps_AutoTask=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process#AutoTask");
       public static final org.semanticwb.platform.SemanticClass swbps_MessageFlow=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process#MessageFlow");
       public static final org.semanticwb.platform.SemanticClass swbps_EndEvent=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process#EndEvent");
       public static final org.semanticwb.platform.SemanticClass swbps_XORDataGateWay=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process#XORDataGateWay");
       public static final org.semanticwb.platform.SemanticClass swbps_Process=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process#Process");
       public static final org.semanticwb.platform.SemanticClass swbps_ANDGateWay=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process#ANDGateWay");
       public static final org.semanticwb.platform.SemanticClass swbps_SequenceFlow=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process#SequenceFlow");
       public static final org.semanticwb.platform.SemanticClass swbps_InterEvent=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process#InterEvent");
       public static final org.semanticwb.platform.SemanticClass swbps_Event=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process#Event");
       public static final org.semanticwb.platform.SemanticClass swbps_ComplexGateWay=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process#ComplexGateWay");
       public static final org.semanticwb.platform.SemanticClass swbps_UserTask=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process#UserTask");
       public static final org.semanticwb.platform.SemanticClass swbps_ORGateWay=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process#ORGateWay");
       public static final org.semanticwb.platform.SemanticClass swbps_GateWay=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process#GateWay");
       public static final org.semanticwb.platform.SemanticClass swbps_AssociationFlow=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process#AssociationFlow");
       public static final org.semanticwb.platform.SemanticClass swbps_ConnectionObject=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process#ConnectionObject");
       public static final org.semanticwb.platform.SemanticClass swbps_ProcessSite=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process#ProcessSite");
       public static final org.semanticwb.platform.SemanticClass sclass=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/process#ProcessSite");

       public static java.util.Iterator<org.semanticwb.process.ProcessSite> listProcessSites(org.semanticwb.model.SWBModel model)
       {
           java.util.Iterator it=model.getSemanticObject().getModel().listInstancesOfClass(sclass);
           return new org.semanticwb.model.GenericIterator<org.semanticwb.process.ProcessSite>(it, true);
       }

       public static java.util.Iterator<org.semanticwb.process.ProcessSite> listProcessSites()
       {
           java.util.Iterator it=sclass.listInstances();
           return new org.semanticwb.model.GenericIterator<org.semanticwb.process.ProcessSite>(it, true);
       }

       public static org.semanticwb.process.ProcessSite getProcessSite(String id)
       {
           org.semanticwb.platform.SemanticMgr mgr=org.semanticwb.SWBPlatform.getSemanticMgr();
           org.semanticwb.process.ProcessSite ret=null;
           org.semanticwb.platform.SemanticModel model=mgr.getModel(id);
           if(model!=null)
           {
               org.semanticwb.platform.SemanticObject obj=model.getSemanticObject(model.getObjectUri(id,sclass));
               if(obj!=null)
               {
                   ret=(org.semanticwb.process.ProcessSite)obj.createGenericInstance();
               }
           }
           return ret;
       }

       public static org.semanticwb.process.ProcessSite createProcessSite(String id, String namespace)
       {
           org.semanticwb.platform.SemanticMgr mgr=org.semanticwb.SWBPlatform.getSemanticMgr();
           org.semanticwb.platform.SemanticModel model=mgr.createModel(id, namespace);
           return (org.semanticwb.process.ProcessSite)model.createGenericObject(model.getObjectUri(id, sclass), sclass);
       }

       public static void removeProcessSite(String id)
       {
           org.semanticwb.process.ProcessSite obj=getProcessSite(id);
           if(obj!=null)
           {
               obj.remove();
           }
       }

       public static boolean hasProcessSite(String id)
       {
           return (getProcessSite(id)!=null);
       }
    }

    public ProcessSiteBase(org.semanticwb.platform.SemanticObject base)
    {
        super(base);
    }

    public org.semanticwb.process.XOREventGateWay getXOREventGateWay(String id)
    {
        return org.semanticwb.process.XOREventGateWay.ClassMgr.getXOREventGateWay(id, this);
    }

    public java.util.Iterator<org.semanticwb.process.XOREventGateWay> listXOREventGateWays()
    {
        return org.semanticwb.process.XOREventGateWay.ClassMgr.listXOREventGateWays(this);
    }

    public org.semanticwb.process.XOREventGateWay createXOREventGateWay(String id)
    {
        return org.semanticwb.process.XOREventGateWay.ClassMgr.createXOREventGateWay(id,this);
    }

    public org.semanticwb.process.XOREventGateWay createXOREventGateWay()
    {
        long id=getSemanticObject().getModel().getCounter(ClassMgr.swbps_XOREventGateWay);
        return org.semanticwb.process.XOREventGateWay.ClassMgr.createXOREventGateWay(String.valueOf(id),this);
    } 

    public void removeXOREventGateWay(String id)
    {
        org.semanticwb.process.XOREventGateWay.ClassMgr.removeXOREventGateWay(id, this);
    }
    public boolean hasXOREventGateWay(String id)
    {
        return org.semanticwb.process.XOREventGateWay.ClassMgr.hasXOREventGateWay(id, this);
    }

    public org.semanticwb.process.XORGateWay getXORGateWay(String id)
    {
        return org.semanticwb.process.XORGateWay.ClassMgr.getXORGateWay(id, this);
    }

    public java.util.Iterator<org.semanticwb.process.XORGateWay> listXORGateWays()
    {
        return org.semanticwb.process.XORGateWay.ClassMgr.listXORGateWays(this);
    }

    public org.semanticwb.process.XORGateWay createXORGateWay(String id)
    {
        return org.semanticwb.process.XORGateWay.ClassMgr.createXORGateWay(id,this);
    }

    public org.semanticwb.process.XORGateWay createXORGateWay()
    {
        long id=getSemanticObject().getModel().getCounter(ClassMgr.swbps_XORGateWay);
        return org.semanticwb.process.XORGateWay.ClassMgr.createXORGateWay(String.valueOf(id),this);
    } 

    public void removeXORGateWay(String id)
    {
        org.semanticwb.process.XORGateWay.ClassMgr.removeXORGateWay(id, this);
    }
    public boolean hasXORGateWay(String id)
    {
        return org.semanticwb.process.XORGateWay.ClassMgr.hasXORGateWay(id, this);
    }

    public org.semanticwb.process.ProcessInstance getProcessInstance(String id)
    {
        return org.semanticwb.process.ProcessInstance.ClassMgr.getProcessInstance(id, this);
    }

    public java.util.Iterator<org.semanticwb.process.ProcessInstance> listProcessInstances()
    {
        return org.semanticwb.process.ProcessInstance.ClassMgr.listProcessInstances(this);
    }

    public org.semanticwb.process.ProcessInstance createProcessInstance(String id)
    {
        return org.semanticwb.process.ProcessInstance.ClassMgr.createProcessInstance(id,this);
    }

    public org.semanticwb.process.ProcessInstance createProcessInstance()
    {
        long id=getSemanticObject().getModel().getCounter(ClassMgr.swbps_ProcessInstance);
        return org.semanticwb.process.ProcessInstance.ClassMgr.createProcessInstance(String.valueOf(id),this);
    } 

    public void removeProcessInstance(String id)
    {
        org.semanticwb.process.ProcessInstance.ClassMgr.removeProcessInstance(id, this);
    }
    public boolean hasProcessInstance(String id)
    {
        return org.semanticwb.process.ProcessInstance.ClassMgr.hasProcessInstance(id, this);
    }

    public org.semanticwb.model.FormView getFormView(String id)
    {
        return org.semanticwb.model.FormView.ClassMgr.getFormView(id, this);
    }

    public java.util.Iterator<org.semanticwb.model.FormView> listFormViews()
    {
        return org.semanticwb.model.FormView.ClassMgr.listFormViews(this);
    }

    public org.semanticwb.model.FormView createFormView(String id)
    {
        return org.semanticwb.model.FormView.ClassMgr.createFormView(id,this);
    }

    public org.semanticwb.model.FormView createFormView()
    {
        long id=getSemanticObject().getModel().getCounter(ClassMgr.swbxf_FormView);
        return org.semanticwb.model.FormView.ClassMgr.createFormView(String.valueOf(id),this);
    } 

    public void removeFormView(String id)
    {
        org.semanticwb.model.FormView.ClassMgr.removeFormView(id, this);
    }
    public boolean hasFormView(String id)
    {
        return org.semanticwb.model.FormView.ClassMgr.hasFormView(id, this);
    }

    public org.semanticwb.process.InitEvent getInitEvent(String id)
    {
        return org.semanticwb.process.InitEvent.ClassMgr.getInitEvent(id, this);
    }

    public java.util.Iterator<org.semanticwb.process.InitEvent> listInitEvents()
    {
        return org.semanticwb.process.InitEvent.ClassMgr.listInitEvents(this);
    }

    public org.semanticwb.process.InitEvent createInitEvent(String id)
    {
        return org.semanticwb.process.InitEvent.ClassMgr.createInitEvent(id,this);
    }

    public org.semanticwb.process.InitEvent createInitEvent()
    {
        long id=getSemanticObject().getModel().getCounter(ClassMgr.swbps_InitEvent);
        return org.semanticwb.process.InitEvent.ClassMgr.createInitEvent(String.valueOf(id),this);
    } 

    public void removeInitEvent(String id)
    {
        org.semanticwb.process.InitEvent.ClassMgr.removeInitEvent(id, this);
    }
    public boolean hasInitEvent(String id)
    {
        return org.semanticwb.process.InitEvent.ClassMgr.hasInitEvent(id, this);
    }

    public org.semanticwb.process.ProcessObject getProcessObject(String id)
    {
        return org.semanticwb.process.ProcessObject.ClassMgr.getProcessObject(id, this);
    }

    public java.util.Iterator<org.semanticwb.process.ProcessObject> listProcessObjects()
    {
        return org.semanticwb.process.ProcessObject.ClassMgr.listProcessObjects(this);
    }

    public org.semanticwb.process.ProcessObject createProcessObject(String id)
    {
        return org.semanticwb.process.ProcessObject.ClassMgr.createProcessObject(id,this);
    }

    public void removeProcessObject(String id)
    {
        org.semanticwb.process.ProcessObject.ClassMgr.removeProcessObject(id, this);
    }
    public boolean hasProcessObject(String id)
    {
        return org.semanticwb.process.ProcessObject.ClassMgr.hasProcessObject(id, this);
    }

    public org.semanticwb.process.FlowObjectInstance getFlowObjectInstance(String id)
    {
        return org.semanticwb.process.FlowObjectInstance.ClassMgr.getFlowObjectInstance(id, this);
    }

    public java.util.Iterator<org.semanticwb.process.FlowObjectInstance> listFlowObjectInstances()
    {
        return org.semanticwb.process.FlowObjectInstance.ClassMgr.listFlowObjectInstances(this);
    }

    public org.semanticwb.process.FlowObjectInstance createFlowObjectInstance(String id)
    {
        return org.semanticwb.process.FlowObjectInstance.ClassMgr.createFlowObjectInstance(id,this);
    }

    public org.semanticwb.process.FlowObjectInstance createFlowObjectInstance()
    {
        long id=getSemanticObject().getModel().getCounter(ClassMgr.swbps_FlowObjectInstance);
        return org.semanticwb.process.FlowObjectInstance.ClassMgr.createFlowObjectInstance(String.valueOf(id),this);
    } 

    public void removeFlowObjectInstance(String id)
    {
        org.semanticwb.process.FlowObjectInstance.ClassMgr.removeFlowObjectInstance(id, this);
    }
    public boolean hasFlowObjectInstance(String id)
    {
        return org.semanticwb.process.FlowObjectInstance.ClassMgr.hasFlowObjectInstance(id, this);
    }

    public org.semanticwb.process.AutoTask getAutoTask(String id)
    {
        return org.semanticwb.process.AutoTask.ClassMgr.getAutoTask(id, this);
    }

    public java.util.Iterator<org.semanticwb.process.AutoTask> listAutoTasks()
    {
        return org.semanticwb.process.AutoTask.ClassMgr.listAutoTasks(this);
    }

    public org.semanticwb.process.AutoTask createAutoTask(String id)
    {
        return org.semanticwb.process.AutoTask.ClassMgr.createAutoTask(id,this);
    }

    public org.semanticwb.process.AutoTask createAutoTask()
    {
        long id=getSemanticObject().getModel().getCounter(ClassMgr.swbps_AutoTask);
        return org.semanticwb.process.AutoTask.ClassMgr.createAutoTask(String.valueOf(id),this);
    } 

    public void removeAutoTask(String id)
    {
        org.semanticwb.process.AutoTask.ClassMgr.removeAutoTask(id, this);
    }
    public boolean hasAutoTask(String id)
    {
        return org.semanticwb.process.AutoTask.ClassMgr.hasAutoTask(id, this);
    }

    public org.semanticwb.process.MessageFlow getMessageFlow(String id)
    {
        return org.semanticwb.process.MessageFlow.ClassMgr.getMessageFlow(id, this);
    }

    public java.util.Iterator<org.semanticwb.process.MessageFlow> listMessageFlows()
    {
        return org.semanticwb.process.MessageFlow.ClassMgr.listMessageFlows(this);
    }

    public org.semanticwb.process.MessageFlow createMessageFlow(String id)
    {
        return org.semanticwb.process.MessageFlow.ClassMgr.createMessageFlow(id,this);
    }

    public org.semanticwb.process.MessageFlow createMessageFlow()
    {
        long id=getSemanticObject().getModel().getCounter(ClassMgr.swbps_MessageFlow);
        return org.semanticwb.process.MessageFlow.ClassMgr.createMessageFlow(String.valueOf(id),this);
    } 

    public void removeMessageFlow(String id)
    {
        org.semanticwb.process.MessageFlow.ClassMgr.removeMessageFlow(id, this);
    }
    public boolean hasMessageFlow(String id)
    {
        return org.semanticwb.process.MessageFlow.ClassMgr.hasMessageFlow(id, this);
    }

    public org.semanticwb.process.EndEvent getEndEvent(String id)
    {
        return org.semanticwb.process.EndEvent.ClassMgr.getEndEvent(id, this);
    }

    public java.util.Iterator<org.semanticwb.process.EndEvent> listEndEvents()
    {
        return org.semanticwb.process.EndEvent.ClassMgr.listEndEvents(this);
    }

    public org.semanticwb.process.EndEvent createEndEvent(String id)
    {
        return org.semanticwb.process.EndEvent.ClassMgr.createEndEvent(id,this);
    }

    public org.semanticwb.process.EndEvent createEndEvent()
    {
        long id=getSemanticObject().getModel().getCounter(ClassMgr.swbps_EndEvent);
        return org.semanticwb.process.EndEvent.ClassMgr.createEndEvent(String.valueOf(id),this);
    } 

    public void removeEndEvent(String id)
    {
        org.semanticwb.process.EndEvent.ClassMgr.removeEndEvent(id, this);
    }
    public boolean hasEndEvent(String id)
    {
        return org.semanticwb.process.EndEvent.ClassMgr.hasEndEvent(id, this);
    }

    public org.semanticwb.process.XORDataGateWay getXORDataGateWay(String id)
    {
        return org.semanticwb.process.XORDataGateWay.ClassMgr.getXORDataGateWay(id, this);
    }

    public java.util.Iterator<org.semanticwb.process.XORDataGateWay> listXORDataGateWays()
    {
        return org.semanticwb.process.XORDataGateWay.ClassMgr.listXORDataGateWays(this);
    }

    public org.semanticwb.process.XORDataGateWay createXORDataGateWay(String id)
    {
        return org.semanticwb.process.XORDataGateWay.ClassMgr.createXORDataGateWay(id,this);
    }

    public org.semanticwb.process.XORDataGateWay createXORDataGateWay()
    {
        long id=getSemanticObject().getModel().getCounter(ClassMgr.swbps_XORDataGateWay);
        return org.semanticwb.process.XORDataGateWay.ClassMgr.createXORDataGateWay(String.valueOf(id),this);
    } 

    public void removeXORDataGateWay(String id)
    {
        org.semanticwb.process.XORDataGateWay.ClassMgr.removeXORDataGateWay(id, this);
    }
    public boolean hasXORDataGateWay(String id)
    {
        return org.semanticwb.process.XORDataGateWay.ClassMgr.hasXORDataGateWay(id, this);
    }

    public org.semanticwb.process.Process getProcess(String id)
    {
        return org.semanticwb.process.Process.ClassMgr.getProcess(id, this);
    }

    public java.util.Iterator<org.semanticwb.process.Process> listProcesses()
    {
        return org.semanticwb.process.Process.ClassMgr.listProcesses(this);
    }

    public org.semanticwb.process.Process createProcess(String id)
    {
        return org.semanticwb.process.Process.ClassMgr.createProcess(id,this);
    }

    public org.semanticwb.process.Process createProcess()
    {
        long id=getSemanticObject().getModel().getCounter(ClassMgr.swbps_Process);
        return org.semanticwb.process.Process.ClassMgr.createProcess(String.valueOf(id),this);
    } 

    public void removeProcess(String id)
    {
        org.semanticwb.process.Process.ClassMgr.removeProcess(id, this);
    }
    public boolean hasProcess(String id)
    {
        return org.semanticwb.process.Process.ClassMgr.hasProcess(id, this);
    }

    public org.semanticwb.process.ANDGateWay getANDGateWay(String id)
    {
        return org.semanticwb.process.ANDGateWay.ClassMgr.getANDGateWay(id, this);
    }

    public java.util.Iterator<org.semanticwb.process.ANDGateWay> listANDGateWays()
    {
        return org.semanticwb.process.ANDGateWay.ClassMgr.listANDGateWays(this);
    }

    public org.semanticwb.process.ANDGateWay createANDGateWay(String id)
    {
        return org.semanticwb.process.ANDGateWay.ClassMgr.createANDGateWay(id,this);
    }

    public org.semanticwb.process.ANDGateWay createANDGateWay()
    {
        long id=getSemanticObject().getModel().getCounter(ClassMgr.swbps_ANDGateWay);
        return org.semanticwb.process.ANDGateWay.ClassMgr.createANDGateWay(String.valueOf(id),this);
    } 

    public void removeANDGateWay(String id)
    {
        org.semanticwb.process.ANDGateWay.ClassMgr.removeANDGateWay(id, this);
    }
    public boolean hasANDGateWay(String id)
    {
        return org.semanticwb.process.ANDGateWay.ClassMgr.hasANDGateWay(id, this);
    }

    public org.semanticwb.process.SequenceFlow getSequenceFlow(String id)
    {
        return org.semanticwb.process.SequenceFlow.ClassMgr.getSequenceFlow(id, this);
    }

    public java.util.Iterator<org.semanticwb.process.SequenceFlow> listSequenceFlows()
    {
        return org.semanticwb.process.SequenceFlow.ClassMgr.listSequenceFlows(this);
    }

    public org.semanticwb.process.SequenceFlow createSequenceFlow(String id)
    {
        return org.semanticwb.process.SequenceFlow.ClassMgr.createSequenceFlow(id,this);
    }

    public org.semanticwb.process.SequenceFlow createSequenceFlow()
    {
        long id=getSemanticObject().getModel().getCounter(ClassMgr.swbps_SequenceFlow);
        return org.semanticwb.process.SequenceFlow.ClassMgr.createSequenceFlow(String.valueOf(id),this);
    } 

    public void removeSequenceFlow(String id)
    {
        org.semanticwb.process.SequenceFlow.ClassMgr.removeSequenceFlow(id, this);
    }
    public boolean hasSequenceFlow(String id)
    {
        return org.semanticwb.process.SequenceFlow.ClassMgr.hasSequenceFlow(id, this);
    }

    public org.semanticwb.process.InterEvent getInterEvent(String id)
    {
        return org.semanticwb.process.InterEvent.ClassMgr.getInterEvent(id, this);
    }

    public java.util.Iterator<org.semanticwb.process.InterEvent> listInterEvents()
    {
        return org.semanticwb.process.InterEvent.ClassMgr.listInterEvents(this);
    }

    public org.semanticwb.process.InterEvent createInterEvent(String id)
    {
        return org.semanticwb.process.InterEvent.ClassMgr.createInterEvent(id,this);
    }

    public org.semanticwb.process.InterEvent createInterEvent()
    {
        long id=getSemanticObject().getModel().getCounter(ClassMgr.swbps_InterEvent);
        return org.semanticwb.process.InterEvent.ClassMgr.createInterEvent(String.valueOf(id),this);
    } 

    public void removeInterEvent(String id)
    {
        org.semanticwb.process.InterEvent.ClassMgr.removeInterEvent(id, this);
    }
    public boolean hasInterEvent(String id)
    {
        return org.semanticwb.process.InterEvent.ClassMgr.hasInterEvent(id, this);
    }

    public org.semanticwb.process.Event getEvent(String id)
    {
        return org.semanticwb.process.Event.ClassMgr.getEvent(id, this);
    }

    public java.util.Iterator<org.semanticwb.process.Event> listEvents()
    {
        return org.semanticwb.process.Event.ClassMgr.listEvents(this);
    }

    public org.semanticwb.process.Event createEvent(String id)
    {
        return org.semanticwb.process.Event.ClassMgr.createEvent(id,this);
    }

    public void removeEvent(String id)
    {
        org.semanticwb.process.Event.ClassMgr.removeEvent(id, this);
    }
    public boolean hasEvent(String id)
    {
        return org.semanticwb.process.Event.ClassMgr.hasEvent(id, this);
    }

    public org.semanticwb.process.ComplexGateWay getComplexGateWay(String id)
    {
        return org.semanticwb.process.ComplexGateWay.ClassMgr.getComplexGateWay(id, this);
    }

    public java.util.Iterator<org.semanticwb.process.ComplexGateWay> listComplexGateWays()
    {
        return org.semanticwb.process.ComplexGateWay.ClassMgr.listComplexGateWays(this);
    }

    public org.semanticwb.process.ComplexGateWay createComplexGateWay(String id)
    {
        return org.semanticwb.process.ComplexGateWay.ClassMgr.createComplexGateWay(id,this);
    }

    public org.semanticwb.process.ComplexGateWay createComplexGateWay()
    {
        long id=getSemanticObject().getModel().getCounter(ClassMgr.swbps_ComplexGateWay);
        return org.semanticwb.process.ComplexGateWay.ClassMgr.createComplexGateWay(String.valueOf(id),this);
    } 

    public void removeComplexGateWay(String id)
    {
        org.semanticwb.process.ComplexGateWay.ClassMgr.removeComplexGateWay(id, this);
    }
    public boolean hasComplexGateWay(String id)
    {
        return org.semanticwb.process.ComplexGateWay.ClassMgr.hasComplexGateWay(id, this);
    }

    public org.semanticwb.process.UserTask getUserTask(String id)
    {
        return org.semanticwb.process.UserTask.ClassMgr.getUserTask(id, this);
    }

    public java.util.Iterator<org.semanticwb.process.UserTask> listUserTasks()
    {
        return org.semanticwb.process.UserTask.ClassMgr.listUserTasks(this);
    }

    public org.semanticwb.process.UserTask createUserTask(String id)
    {
        return org.semanticwb.process.UserTask.ClassMgr.createUserTask(id,this);
    }

    public void removeUserTask(String id)
    {
        org.semanticwb.process.UserTask.ClassMgr.removeUserTask(id, this);
    }
    public boolean hasUserTask(String id)
    {
        return org.semanticwb.process.UserTask.ClassMgr.hasUserTask(id, this);
    }

    public org.semanticwb.process.ORGateWay getORGateWay(String id)
    {
        return org.semanticwb.process.ORGateWay.ClassMgr.getORGateWay(id, this);
    }

    public java.util.Iterator<org.semanticwb.process.ORGateWay> listORGateWays()
    {
        return org.semanticwb.process.ORGateWay.ClassMgr.listORGateWays(this);
    }

    public org.semanticwb.process.ORGateWay createORGateWay(String id)
    {
        return org.semanticwb.process.ORGateWay.ClassMgr.createORGateWay(id,this);
    }

    public org.semanticwb.process.ORGateWay createORGateWay()
    {
        long id=getSemanticObject().getModel().getCounter(ClassMgr.swbps_ORGateWay);
        return org.semanticwb.process.ORGateWay.ClassMgr.createORGateWay(String.valueOf(id),this);
    } 

    public void removeORGateWay(String id)
    {
        org.semanticwb.process.ORGateWay.ClassMgr.removeORGateWay(id, this);
    }
    public boolean hasORGateWay(String id)
    {
        return org.semanticwb.process.ORGateWay.ClassMgr.hasORGateWay(id, this);
    }

    public org.semanticwb.process.GateWay getGateWay(String id)
    {
        return org.semanticwb.process.GateWay.ClassMgr.getGateWay(id, this);
    }

    public java.util.Iterator<org.semanticwb.process.GateWay> listGateWays()
    {
        return org.semanticwb.process.GateWay.ClassMgr.listGateWays(this);
    }

    public org.semanticwb.process.GateWay createGateWay(String id)
    {
        return org.semanticwb.process.GateWay.ClassMgr.createGateWay(id,this);
    }

    public org.semanticwb.process.GateWay createGateWay()
    {
        long id=getSemanticObject().getModel().getCounter(ClassMgr.swbps_GateWay);
        return org.semanticwb.process.GateWay.ClassMgr.createGateWay(String.valueOf(id),this);
    } 

    public void removeGateWay(String id)
    {
        org.semanticwb.process.GateWay.ClassMgr.removeGateWay(id, this);
    }
    public boolean hasGateWay(String id)
    {
        return org.semanticwb.process.GateWay.ClassMgr.hasGateWay(id, this);
    }

    public org.semanticwb.process.AssociationFlow getAssociationFlow(String id)
    {
        return org.semanticwb.process.AssociationFlow.ClassMgr.getAssociationFlow(id, this);
    }

    public java.util.Iterator<org.semanticwb.process.AssociationFlow> listAssociationFlows()
    {
        return org.semanticwb.process.AssociationFlow.ClassMgr.listAssociationFlows(this);
    }

    public org.semanticwb.process.AssociationFlow createAssociationFlow(String id)
    {
        return org.semanticwb.process.AssociationFlow.ClassMgr.createAssociationFlow(id,this);
    }

    public org.semanticwb.process.AssociationFlow createAssociationFlow()
    {
        long id=getSemanticObject().getModel().getCounter(ClassMgr.swbps_AssociationFlow);
        return org.semanticwb.process.AssociationFlow.ClassMgr.createAssociationFlow(String.valueOf(id),this);
    } 

    public void removeAssociationFlow(String id)
    {
        org.semanticwb.process.AssociationFlow.ClassMgr.removeAssociationFlow(id, this);
    }
    public boolean hasAssociationFlow(String id)
    {
        return org.semanticwb.process.AssociationFlow.ClassMgr.hasAssociationFlow(id, this);
    }

    public org.semanticwb.process.ConnectionObject getConnectionObject(String id)
    {
        return org.semanticwb.process.ConnectionObject.ClassMgr.getConnectionObject(id, this);
    }

    public java.util.Iterator<org.semanticwb.process.ConnectionObject> listConnectionObjects()
    {
        return org.semanticwb.process.ConnectionObject.ClassMgr.listConnectionObjects(this);
    }

    public org.semanticwb.process.ConnectionObject createConnectionObject(String id)
    {
        return org.semanticwb.process.ConnectionObject.ClassMgr.createConnectionObject(id,this);
    }

    public void removeConnectionObject(String id)
    {
        org.semanticwb.process.ConnectionObject.ClassMgr.removeConnectionObject(id, this);
    }
    public boolean hasConnectionObject(String id)
    {
        return org.semanticwb.process.ConnectionObject.ClassMgr.hasConnectionObject(id, this);
    }
}

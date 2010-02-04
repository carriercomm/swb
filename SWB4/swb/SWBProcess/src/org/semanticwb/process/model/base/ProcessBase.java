package org.semanticwb.process.model.base;


public abstract class ProcessBase extends org.semanticwb.process.model.SupportingElement implements org.semanticwb.process.model.Modelable,org.semanticwb.model.Descriptiveable,org.semanticwb.process.model.Observable,org.semanticwb.process.model.Performable,org.semanticwb.process.model.IOAble,org.semanticwb.process.model.Diagramable,org.semanticwb.process.model.Assignable,org.semanticwb.process.model.AdHocable
{
       public static final org.semanticwb.platform.SemanticProperty swp_processType=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/swp#processType");
       public static final org.semanticwb.platform.SemanticClass swp_Process=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/swp#Process");
       public static final org.semanticwb.platform.SemanticClass sclass=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/swp#Process");
    public static class ClassMgr
    {

       public static java.util.Iterator<org.semanticwb.process.model.Process> listProcesses(org.semanticwb.model.SWBModel model)
       {
           java.util.Iterator it=model.getSemanticObject().getModel().listInstancesOfClass(sclass);
           return new org.semanticwb.model.GenericIterator<org.semanticwb.process.model.Process>(it, true);
       }

       public static java.util.Iterator<org.semanticwb.process.model.Process> listProcesses()
       {
           java.util.Iterator it=sclass.listInstances();
           return new org.semanticwb.model.GenericIterator<org.semanticwb.process.model.Process>(it, true);
       }

       public static org.semanticwb.process.model.Process createProcess(org.semanticwb.model.SWBModel model)
       {
           long id=model.getSemanticObject().getModel().getCounter(sclass);
           return org.semanticwb.process.model.Process.ClassMgr.createProcess(String.valueOf(id), model);
       }

       public static org.semanticwb.process.model.Process getProcess(String id, org.semanticwb.model.SWBModel model)
       {
           return (org.semanticwb.process.model.Process)model.getSemanticObject().getModel().getGenericObject(model.getSemanticObject().getModel().getObjectUri(id,sclass),sclass);
       }

       public static org.semanticwb.process.model.Process createProcess(String id, org.semanticwb.model.SWBModel model)
       {
           return (org.semanticwb.process.model.Process)model.getSemanticObject().getModel().createGenericObject(model.getSemanticObject().getModel().getObjectUri(id, sclass), sclass);
       }

       public static void removeProcess(String id, org.semanticwb.model.SWBModel model)
       {
           model.getSemanticObject().getModel().removeSemanticObject(model.getSemanticObject().getModel().getObjectUri(id,sclass));
       }

       public static boolean hasProcess(String id, org.semanticwb.model.SWBModel model)
       {
           return (getProcess(id, model)!=null);
       }
   public static java.util.Iterator<org.semanticwb.process.model.Process> listProcessByProperty(org.semanticwb.process.model.Property hasproperty,org.semanticwb.model.SWBModel model)
   {
       org.semanticwb.model.GenericIterator<org.semanticwb.process.model.Process> it=new org.semanticwb.model.GenericIterator(model.getSemanticObject().getModel().listSubjects(swp_hasProperty, hasproperty.getSemanticObject()));
       return it;
   }

   public static java.util.Iterator<org.semanticwb.process.model.Process> listProcessByProperty(org.semanticwb.process.model.Property hasproperty)
   {
       org.semanticwb.model.GenericIterator<org.semanticwb.process.model.Process> it=new org.semanticwb.model.GenericIterator(hasproperty.getSemanticObject().getModel().listSubjects(swp_hasProperty,hasproperty.getSemanticObject()));
       return it;
   }
   public static java.util.Iterator<org.semanticwb.process.model.Process> listProcessByGraphicalElement(org.semanticwb.process.model.GraphicalElement hasgraphicalelement,org.semanticwb.model.SWBModel model)
   {
       org.semanticwb.model.GenericIterator<org.semanticwb.process.model.Process> it=new org.semanticwb.model.GenericIterator(model.getSemanticObject().getModel().listSubjects(swp_hasGraphicalElement, hasgraphicalelement.getSemanticObject()));
       return it;
   }

   public static java.util.Iterator<org.semanticwb.process.model.Process> listProcessByGraphicalElement(org.semanticwb.process.model.GraphicalElement hasgraphicalelement)
   {
       org.semanticwb.model.GenericIterator<org.semanticwb.process.model.Process> it=new org.semanticwb.model.GenericIterator(hasgraphicalelement.getSemanticObject().getModel().listSubjects(swp_hasGraphicalElement,hasgraphicalelement.getSemanticObject()));
       return it;
   }
   public static java.util.Iterator<org.semanticwb.process.model.Process> listProcessByOutputSet(org.semanticwb.process.model.OutputSet hasoutputset,org.semanticwb.model.SWBModel model)
   {
       org.semanticwb.model.GenericIterator<org.semanticwb.process.model.Process> it=new org.semanticwb.model.GenericIterator(model.getSemanticObject().getModel().listSubjects(swp_hasOutputSet, hasoutputset.getSemanticObject()));
       return it;
   }

   public static java.util.Iterator<org.semanticwb.process.model.Process> listProcessByOutputSet(org.semanticwb.process.model.OutputSet hasoutputset)
   {
       org.semanticwb.model.GenericIterator<org.semanticwb.process.model.Process> it=new org.semanticwb.model.GenericIterator(hasoutputset.getSemanticObject().getModel().listSubjects(swp_hasOutputSet,hasoutputset.getSemanticObject()));
       return it;
   }
   public static java.util.Iterator<org.semanticwb.process.model.Process> listProcessByInputSet(org.semanticwb.process.model.InputSet hasinputset,org.semanticwb.model.SWBModel model)
   {
       org.semanticwb.model.GenericIterator<org.semanticwb.process.model.Process> it=new org.semanticwb.model.GenericIterator(model.getSemanticObject().getModel().listSubjects(swp_hasInputSet, hasinputset.getSemanticObject()));
       return it;
   }

   public static java.util.Iterator<org.semanticwb.process.model.Process> listProcessByInputSet(org.semanticwb.process.model.InputSet hasinputset)
   {
       org.semanticwb.model.GenericIterator<org.semanticwb.process.model.Process> it=new org.semanticwb.model.GenericIterator(hasinputset.getSemanticObject().getModel().listSubjects(swp_hasInputSet,hasinputset.getSemanticObject()));
       return it;
   }
   public static java.util.Iterator<org.semanticwb.process.model.Process> listProcessByAdHocCompletionCondition(org.semanticwb.process.model.Expression adhoccompletioncondition,org.semanticwb.model.SWBModel model)
   {
       org.semanticwb.model.GenericIterator<org.semanticwb.process.model.Process> it=new org.semanticwb.model.GenericIterator(model.getSemanticObject().getModel().listSubjects(swp_adHocCompletionCondition, adhoccompletioncondition.getSemanticObject()));
       return it;
   }

   public static java.util.Iterator<org.semanticwb.process.model.Process> listProcessByAdHocCompletionCondition(org.semanticwb.process.model.Expression adhoccompletioncondition)
   {
       org.semanticwb.model.GenericIterator<org.semanticwb.process.model.Process> it=new org.semanticwb.model.GenericIterator(adhoccompletioncondition.getSemanticObject().getModel().listSubjects(swp_adHocCompletionCondition,adhoccompletioncondition.getSemanticObject()));
       return it;
   }
   public static java.util.Iterator<org.semanticwb.process.model.Process> listProcessByCategory(org.semanticwb.process.model.Category hascategory,org.semanticwb.model.SWBModel model)
   {
       org.semanticwb.model.GenericIterator<org.semanticwb.process.model.Process> it=new org.semanticwb.model.GenericIterator(model.getSemanticObject().getModel().listSubjects(swp_hasCategory, hascategory.getSemanticObject()));
       return it;
   }

   public static java.util.Iterator<org.semanticwb.process.model.Process> listProcessByCategory(org.semanticwb.process.model.Category hascategory)
   {
       org.semanticwb.model.GenericIterator<org.semanticwb.process.model.Process> it=new org.semanticwb.model.GenericIterator(hascategory.getSemanticObject().getModel().listSubjects(swp_hasCategory,hascategory.getSemanticObject()));
       return it;
   }
   public static java.util.Iterator<org.semanticwb.process.model.Process> listProcessByAssignment(org.semanticwb.process.model.Assignment hasassignment,org.semanticwb.model.SWBModel model)
   {
       org.semanticwb.model.GenericIterator<org.semanticwb.process.model.Process> it=new org.semanticwb.model.GenericIterator(model.getSemanticObject().getModel().listSubjects(swp_hasAssignment, hasassignment.getSemanticObject()));
       return it;
   }

   public static java.util.Iterator<org.semanticwb.process.model.Process> listProcessByAssignment(org.semanticwb.process.model.Assignment hasassignment)
   {
       org.semanticwb.model.GenericIterator<org.semanticwb.process.model.Process> it=new org.semanticwb.model.GenericIterator(hasassignment.getSemanticObject().getModel().listSubjects(swp_hasAssignment,hasassignment.getSemanticObject()));
       return it;
   }
    }

    public ProcessBase(org.semanticwb.platform.SemanticObject base)
    {
        super(base);
    }

    public org.semanticwb.model.GenericIterator<org.semanticwb.process.model.Property> listProperties()
    {
        return new org.semanticwb.model.GenericIterator<org.semanticwb.process.model.Property>(getSemanticObject().listObjectProperties(swp_hasProperty));
    }

    public boolean hasProperty(org.semanticwb.process.model.Property property)
    {
        if(property==null)return false;
        return getSemanticObject().hasObjectProperty(swp_hasProperty,property.getSemanticObject());
    }

    public void addProperty(org.semanticwb.process.model.Property value)
    {
        getSemanticObject().addObjectProperty(swp_hasProperty, value.getSemanticObject());
    }

    public void removeAllProperty()
    {
        getSemanticObject().removeProperty(swp_hasProperty);
    }

    public void removeProperty(org.semanticwb.process.model.Property property)
    {
        getSemanticObject().removeObjectProperty(swp_hasProperty,property.getSemanticObject());
    }


    public org.semanticwb.process.model.Property getProperty()
    {
         org.semanticwb.process.model.Property ret=null;
         org.semanticwb.platform.SemanticObject obj=getSemanticObject().getObjectProperty(swp_hasProperty);
         if(obj!=null)
         {
             ret=(org.semanticwb.process.model.Property)obj.createGenericInstance();
         }
         return ret;
    }

    public org.semanticwb.model.GenericIterator<org.semanticwb.process.model.GraphicalElement> listGraphicalElements()
    {
        return new org.semanticwb.model.GenericIterator<org.semanticwb.process.model.GraphicalElement>(getSemanticObject().listObjectProperties(swp_hasGraphicalElement));
    }

    public boolean hasGraphicalElement(org.semanticwb.process.model.GraphicalElement graphicalelement)
    {
        if(graphicalelement==null)return false;
        return getSemanticObject().hasObjectProperty(swp_hasGraphicalElement,graphicalelement.getSemanticObject());
    }

    public void addGraphicalElement(org.semanticwb.process.model.GraphicalElement value)
    {
        getSemanticObject().addObjectProperty(swp_hasGraphicalElement, value.getSemanticObject());
    }

    public void removeAllGraphicalElement()
    {
        getSemanticObject().removeProperty(swp_hasGraphicalElement);
    }

    public void removeGraphicalElement(org.semanticwb.process.model.GraphicalElement graphicalelement)
    {
        getSemanticObject().removeObjectProperty(swp_hasGraphicalElement,graphicalelement.getSemanticObject());
    }


    public org.semanticwb.process.model.GraphicalElement getGraphicalElement()
    {
         org.semanticwb.process.model.GraphicalElement ret=null;
         org.semanticwb.platform.SemanticObject obj=getSemanticObject().getObjectProperty(swp_hasGraphicalElement);
         if(obj!=null)
         {
             ret=(org.semanticwb.process.model.GraphicalElement)obj.createGenericInstance();
         }
         return ret;
    }

    public org.semanticwb.model.GenericIterator<org.semanticwb.process.model.OutputSet> listOutputSets()
    {
        return new org.semanticwb.model.GenericIterator<org.semanticwb.process.model.OutputSet>(getSemanticObject().listObjectProperties(swp_hasOutputSet));
    }

    public boolean hasOutputSet(org.semanticwb.process.model.OutputSet outputset)
    {
        if(outputset==null)return false;
        return getSemanticObject().hasObjectProperty(swp_hasOutputSet,outputset.getSemanticObject());
    }

    public void addOutputSet(org.semanticwb.process.model.OutputSet value)
    {
        getSemanticObject().addObjectProperty(swp_hasOutputSet, value.getSemanticObject());
    }

    public void removeAllOutputSet()
    {
        getSemanticObject().removeProperty(swp_hasOutputSet);
    }

    public void removeOutputSet(org.semanticwb.process.model.OutputSet outputset)
    {
        getSemanticObject().removeObjectProperty(swp_hasOutputSet,outputset.getSemanticObject());
    }


    public org.semanticwb.process.model.OutputSet getOutputSet()
    {
         org.semanticwb.process.model.OutputSet ret=null;
         org.semanticwb.platform.SemanticObject obj=getSemanticObject().getObjectProperty(swp_hasOutputSet);
         if(obj!=null)
         {
             ret=(org.semanticwb.process.model.OutputSet)obj.createGenericInstance();
         }
         return ret;
    }

    public org.semanticwb.model.GenericIterator<org.semanticwb.process.model.InputSet> listInputSets()
    {
        return new org.semanticwb.model.GenericIterator<org.semanticwb.process.model.InputSet>(getSemanticObject().listObjectProperties(swp_hasInputSet));
    }

    public boolean hasInputSet(org.semanticwb.process.model.InputSet inputset)
    {
        if(inputset==null)return false;
        return getSemanticObject().hasObjectProperty(swp_hasInputSet,inputset.getSemanticObject());
    }

    public void addInputSet(org.semanticwb.process.model.InputSet value)
    {
        getSemanticObject().addObjectProperty(swp_hasInputSet, value.getSemanticObject());
    }

    public void removeAllInputSet()
    {
        getSemanticObject().removeProperty(swp_hasInputSet);
    }

    public void removeInputSet(org.semanticwb.process.model.InputSet inputset)
    {
        getSemanticObject().removeObjectProperty(swp_hasInputSet,inputset.getSemanticObject());
    }


    public org.semanticwb.process.model.InputSet getInputSet()
    {
         org.semanticwb.process.model.InputSet ret=null;
         org.semanticwb.platform.SemanticObject obj=getSemanticObject().getObjectProperty(swp_hasInputSet);
         if(obj!=null)
         {
             ret=(org.semanticwb.process.model.InputSet)obj.createGenericInstance();
         }
         return ret;
    }

    public String getProcessType()
    {
        return getSemanticObject().getProperty(swp_processType);
    }

    public void setProcessType(String value)
    {
        getSemanticObject().setProperty(swp_processType, value);
    }

    public void setAdHocCompletionCondition(org.semanticwb.process.model.Expression value)
    {
        getSemanticObject().setObjectProperty(swp_adHocCompletionCondition, value.getSemanticObject());
    }

    public void removeAdHocCompletionCondition()
    {
        getSemanticObject().removeProperty(swp_adHocCompletionCondition);
    }


    public org.semanticwb.process.model.Expression getAdHocCompletionCondition()
    {
         org.semanticwb.process.model.Expression ret=null;
         org.semanticwb.platform.SemanticObject obj=getSemanticObject().getObjectProperty(swp_adHocCompletionCondition);
         if(obj!=null)
         {
             ret=(org.semanticwb.process.model.Expression)obj.createGenericInstance();
         }
         return ret;
    }

    public String getStatus()
    {
        return getSemanticObject().getProperty(swp_status);
    }

    public void setStatus(String value)
    {
        getSemanticObject().setProperty(swp_status, value);
    }

    public String getAdHocOrdering()
    {
        return getSemanticObject().getProperty(swp_adHocOrdering);
    }

    public void setAdHocOrdering(String value)
    {
        getSemanticObject().setProperty(swp_adHocOrdering, value);
    }

    public org.semanticwb.model.GenericIterator<org.semanticwb.process.model.Assignment> listAssignments()
    {
        return new org.semanticwb.model.GenericIterator<org.semanticwb.process.model.Assignment>(getSemanticObject().listObjectProperties(swp_hasAssignment));
    }

    public boolean hasAssignment(org.semanticwb.process.model.Assignment assignment)
    {
        if(assignment==null)return false;
        return getSemanticObject().hasObjectProperty(swp_hasAssignment,assignment.getSemanticObject());
    }

    public void addAssignment(org.semanticwb.process.model.Assignment value)
    {
        getSemanticObject().addObjectProperty(swp_hasAssignment, value.getSemanticObject());
    }

    public void removeAllAssignment()
    {
        getSemanticObject().removeProperty(swp_hasAssignment);
    }

    public void removeAssignment(org.semanticwb.process.model.Assignment assignment)
    {
        getSemanticObject().removeObjectProperty(swp_hasAssignment,assignment.getSemanticObject());
    }


    public org.semanticwb.process.model.Assignment getAssignment()
    {
         org.semanticwb.process.model.Assignment ret=null;
         org.semanticwb.platform.SemanticObject obj=getSemanticObject().getObjectProperty(swp_hasAssignment);
         if(obj!=null)
         {
             ret=(org.semanticwb.process.model.Assignment)obj.createGenericInstance();
         }
         return ret;
    }

    public java.util.Iterator<String> listPerformers()
    {
        java.util.ArrayList<String> values=new java.util.ArrayList<String>();
        java.util.Iterator<org.semanticwb.platform.SemanticLiteral> it=getSemanticObject().listLiteralProperties(swp_hasPerformer);
        while(it.hasNext())
        {
                org.semanticwb.platform.SemanticLiteral literal=it.next();
                values.add(literal.getString());
        }
        return values.iterator();
    }

    public void addPerformer(String performer)
    {
        getSemanticObject().setProperty(swp_hasPerformer, performer);
    }

    public void removeAllPerformer()
    {
        getSemanticObject().removeProperty(swp_hasPerformer);
    }

    public void removePerformer(String performer)
    {
        getSemanticObject().removeProperty(swp_hasPerformer,performer);
    }

    public boolean isAdHoc()
    {
        return getSemanticObject().getBooleanProperty(swp_adHoc);
    }

    public void setAdHoc(boolean value)
    {
        getSemanticObject().setBooleanProperty(swp_adHoc, value);
    }
}

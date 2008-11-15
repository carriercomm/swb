package org.semanticwb.repository.base;

import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import org.semanticwb.model.base.GenericObjectBase;
import org.semanticwb.model.SWBVocabulary;
import org.semanticwb.model.SWBContext;
import org.semanticwb.model.GenericObject;
import org.semanticwb.model.GenericIterator;
import org.semanticwb.repository.*;
import com.hp.hpl.jena.rdf.model.*;
import org.semanticwb.*;
import org.semanticwb.platform.*;

public class WorkspaceBase extends GenericObjectBase 
{
    public static SWBVocabulary vocabulary=SWBContext.getVocabulary();

    public WorkspaceBase(SemanticObject base)
    {
        super(base);
    }

    public void setRoot(org.semanticwb.repository.BaseNode basenode)
    {
        getSemanticObject().setObjectProperty(vocabulary.jcr_root, basenode.getSemanticObject());
    }

    public void removeRoot()
    {
        getSemanticObject().removeProperty(vocabulary.jcr_root);
    }

    public BaseNode getRoot()
    {
         BaseNode ret=null;
         SemanticObject obj=getSemanticObject().getObjectProperty(vocabulary.jcr_root);
         if(obj!=null)
         {
             ret=(BaseNode)vocabulary.nt_BaseNode.newGenericInstance(obj);
         }
         return ret;
    }

    public String getName()
    {
        return getSemanticObject().getProperty(vocabulary.swbrep_Name);
    }

    public void setName(String Name)
    {
        getSemanticObject().setProperty(vocabulary.swbrep_Name, Name);
    }

    public BaseNode getBaseNode(String id)
    {
        return (BaseNode)getSemanticObject().getModel().getGenericObject(getSemanticObject().getModel().getObjectUri(id,vocabulary.nt_BaseNode),vocabulary.nt_BaseNode);
    }

    public Iterator<BaseNode> listBaseNodes()
    {
        Property rdf=getSemanticObject().getModel().getRDFModel().getProperty(SemanticVocabulary.RDF_TYPE);
        StmtIterator stit=getSemanticObject().getModel().getRDFModel().listStatements(null, rdf, vocabulary.nt_BaseNode.getOntClass());
        return new GenericIterator<BaseNode>(BaseNode.class, stit, true);
    }

    public BaseNode createBaseNode(String id)
    {
        return (BaseNode)getSemanticObject().getModel().createGenericObject(getSemanticObject().getModel().getObjectUri(id, vocabulary.nt_BaseNode), vocabulary.nt_BaseNode);
    }

    public BaseNode createBaseNode()
    {
        long id=SWBPlatform.getSemanticMgr().getCounter(getSemanticObject().getModel().getName()+"/"+vocabulary.nt_BaseNode.getName());
        return createBaseNode(""+id);
    } 

    public void removeBaseNode(String id)
    {
        getSemanticObject().getModel().removeSemanticObject(getSemanticObject().getModel().getObjectUri(id,vocabulary.nt_BaseNode));
    }
    public boolean hasBaseNode(String id)
    {
        return (getBaseNode(id)!=null);
    }
}

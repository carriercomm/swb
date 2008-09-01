package org.semanticwb.model.base;

import java.util.Date;
import java.util.Iterator;
import org.semanticwb.model.*;
import com.hp.hpl.jena.rdf.model.*;
import org.semanticwb.*;
import org.semanticwb.platform.*;

public class RoleRefBase extends GenericObjectBase implements Activeable
{
    SWBVocabulary vocabulary=SWBContext.getVocabulary();

    public RoleRefBase(SemanticObject base)
    {
        super(base);
    }

    public boolean isActive()
    {
        return getSemanticObject().getBooleanProperty(vocabulary.active);
    }

    public void setActive(boolean active)
    {
        getSemanticObject().setBooleanProperty(vocabulary.active, active);
    }

    public void setRole(org.semanticwb.model.Role role)
    {
        getSemanticObject().addObjectProperty(vocabulary.role, role.getSemanticObject());
    }

    public void removeRole()
    {
        getSemanticObject().removeProperty(vocabulary.role);
    }

    public Role getRole()
    {
         Role ret=null;
         StmtIterator stit=getSemanticObject().getRDFResource().listProperties(vocabulary.role.getRDFProperty());
         GenericIterator<org.semanticwb.model.Role> it=new GenericIterator<org.semanticwb.model.Role>(Role.class, stit);
         if(it.hasNext())
         {
             ret=it.next();
         }
         return ret;
    }

    public WebSite getWebSite()
    {
        return new WebSite(getSemanticObject().getModel().getModelObject());
    }
}

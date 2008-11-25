package org.semanticwb.model.base;

import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import org.semanticwb.model.base.GenericObjectBase;
import org.semanticwb.model.SWBVocabulary;
import org.semanticwb.model.SWBContext;
import org.semanticwb.model.GenericObject;
import org.semanticwb.model.GenericIterator;
import org.semanticwb.model.*;
import com.hp.hpl.jena.rdf.model.*;
import org.semanticwb.*;
import org.semanticwb.platform.*;

public class RoleBase extends GenericObjectBase implements Groupable,Descriptiveable,Traceable
{
    public static SWBVocabulary vocabulary=SWBContext.getVocabulary();

    public RoleBase(SemanticObject base)
    {
        super(base);
    }

    public Date getCreated()
    {
        return getSemanticObject().getDateProperty(vocabulary.swb_created);
    }

    public void setCreated(Date created)
    {
        getSemanticObject().setDateProperty(vocabulary.swb_created, created);
    }

    public void setModifiedBy(org.semanticwb.model.User user)
    {
        getSemanticObject().setObjectProperty(vocabulary.swb_modifiedBy, user.getSemanticObject());
    }

    public void removeModifiedBy()
    {
        getSemanticObject().removeProperty(vocabulary.swb_modifiedBy);
    }

    public User getModifiedBy()
    {
         User ret=null;
         SemanticObject obj=getSemanticObject().getObjectProperty(vocabulary.swb_modifiedBy);
         if(obj!=null)
         {
             ret=(User)vocabulary.swb_User.newGenericInstance(obj);
         }
         return ret;
    }

    public String getTitle()
    {
        return getSemanticObject().getProperty(vocabulary.swb_title);
    }

    public void setTitle(String title)
    {
        getSemanticObject().setProperty(vocabulary.swb_title, title);
    }

    public String getTitle(String lang)
    {
        return getSemanticObject().getProperty(vocabulary.swb_title, null, lang);
    }

    public String getDisplayTitle(String lang)
    {
        return getSemanticObject().getLocaleProperty(vocabulary.swb_title, lang);
    }

    public void setTitle(String title, String lang)
    {
        getSemanticObject().setProperty(vocabulary.swb_title, title, lang);
    }

    public Date getUpdated()
    {
        return getSemanticObject().getDateProperty(vocabulary.swb_updated);
    }

    public void setUpdated(Date updated)
    {
        getSemanticObject().setDateProperty(vocabulary.swb_updated, updated);
    }

    public void setCreator(org.semanticwb.model.User user)
    {
        getSemanticObject().setObjectProperty(vocabulary.swb_creator, user.getSemanticObject());
    }

    public void removeCreator()
    {
        getSemanticObject().removeProperty(vocabulary.swb_creator);
    }

    public User getCreator()
    {
         User ret=null;
         SemanticObject obj=getSemanticObject().getObjectProperty(vocabulary.swb_creator);
         if(obj!=null)
         {
             ret=(User)vocabulary.swb_User.newGenericInstance(obj);
         }
         return ret;
    }

    public GenericIterator<org.semanticwb.model.Permission> listPermissions()
    {
        return new GenericIterator<org.semanticwb.model.Permission>(org.semanticwb.model.Permission.class, getSemanticObject().listObjectProperties(vocabulary.swb_hasPermission));    }

    public void addPermission(org.semanticwb.model.Permission permission)
    {
        getSemanticObject().addObjectProperty(vocabulary.swb_hasPermission, permission.getSemanticObject());
    }

    public void removeAllPermission()
    {
        getSemanticObject().removeProperty(vocabulary.swb_hasPermission);
    }

    public void removePermission(org.semanticwb.model.Permission permission)
    {
        getSemanticObject().removeObjectProperty(vocabulary.swb_hasPermission,permission.getSemanticObject());
    }

    public Permission getPermission()
    {
         Permission ret=null;
         SemanticObject obj=getSemanticObject().getObjectProperty(vocabulary.swb_hasPermission);
         if(obj!=null)
         {
             ret=(Permission)vocabulary.swb_Permission.newGenericInstance(obj);
         }
         return ret;
    }

    public String getDescription()
    {
        return getSemanticObject().getProperty(vocabulary.swb_description);
    }

    public void setDescription(String description)
    {
        getSemanticObject().setProperty(vocabulary.swb_description, description);
    }

    public String getDescription(String lang)
    {
        return getSemanticObject().getProperty(vocabulary.swb_description, null, lang);
    }

    public String getDisplayDescription(String lang)
    {
        return getSemanticObject().getLocaleProperty(vocabulary.swb_description, lang);
    }

    public void setDescription(String description, String lang)
    {
        getSemanticObject().setProperty(vocabulary.swb_description, description, lang);
    }

    public GenericIterator<org.semanticwb.model.ObjectGroup> listGroups()
    {
        return new GenericIterator<org.semanticwb.model.ObjectGroup>(org.semanticwb.model.ObjectGroup.class, getSemanticObject().listObjectProperties(vocabulary.swb_hasGroup));    }

    public void addGroup(org.semanticwb.model.ObjectGroup objectgroup)
    {
        getSemanticObject().addObjectProperty(vocabulary.swb_hasGroup, objectgroup.getSemanticObject());
    }

    public void removeAllGroup()
    {
        getSemanticObject().removeProperty(vocabulary.swb_hasGroup);
    }

    public void removeGroup(org.semanticwb.model.ObjectGroup objectgroup)
    {
        getSemanticObject().removeObjectProperty(vocabulary.swb_hasGroup,objectgroup.getSemanticObject());
    }

    public ObjectGroup getGroup()
    {
         ObjectGroup ret=null;
         SemanticObject obj=getSemanticObject().getObjectProperty(vocabulary.swb_hasGroup);
         if(obj!=null)
         {
             ret=(ObjectGroup)vocabulary.swb_ObjectGroup.newGenericInstance(obj);
         }
         return ret;
    }

    public void remove()
    {
        getSemanticObject().remove();
    }

    public Iterator<GenericObject> listRelatedObjects()
    {
        return new GenericIterator((SemanticClass)null, getSemanticObject().listRelatedObjects(),true);
    }

    public UserRepository getUserRepository()
    {
        return new UserRepository(getSemanticObject().getModel().getModelObject());
    }
}

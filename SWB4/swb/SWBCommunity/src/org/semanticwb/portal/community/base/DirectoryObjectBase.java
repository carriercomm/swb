package org.semanticwb.portal.community.base;


public abstract class DirectoryObjectBase extends org.semanticwb.model.base.GenericObjectBase implements org.semanticwb.model.Searchable,org.semanticwb.model.Tagable,org.semanticwb.model.Traceable,org.semanticwb.portal.community.Interactiveable,org.semanticwb.model.Rankable,org.semanticwb.model.Descriptiveable
{
    public static final org.semanticwb.platform.SemanticClass swbcomm_DirectoryResource=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/community#DirectoryResource");
    public static final org.semanticwb.platform.SemanticProperty swbcomm_directoryResource=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/community#directoryResource");
    public static final org.semanticwb.platform.SemanticClass swb_WebPage=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/ontology#WebPage");
    public static final org.semanticwb.platform.SemanticProperty swbcomm_hasDirProfileWebPage=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/community#hasDirProfileWebPage");
    public static final org.semanticwb.platform.SemanticProperty swbcomm_dirHasExtraPhoto=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/community#dirHasExtraPhoto");
    public static final org.semanticwb.platform.SemanticProperty swbcomm_dirWebPage=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/community#dirWebPage");
    public static final org.semanticwb.platform.SemanticProperty swbcomm_hasDirTopicWebPage=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/community#hasDirTopicWebPage");
    public static final org.semanticwb.platform.SemanticProperty swbcomm_dirPhoto=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/community#dirPhoto");
    public static final org.semanticwb.platform.SemanticClass swbcomm_DirectoryObject=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/community#DirectoryObject");
    public static final org.semanticwb.platform.SemanticClass sclass=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/community#DirectoryObject");

    public static class ClassMgr
    {

        public static java.util.Iterator<org.semanticwb.portal.community.DirectoryObject> listDirectoryObjects(org.semanticwb.model.SWBModel model)
        {
            java.util.Iterator it=model.getSemanticObject().getModel().listInstancesOfClass(sclass);
            return new org.semanticwb.model.GenericIterator<org.semanticwb.portal.community.DirectoryObject>(it, true);
        }

        public static java.util.Iterator<org.semanticwb.portal.community.DirectoryObject> listDirectoryObjects()
        {
            java.util.Iterator it=sclass.listInstances();
            return new org.semanticwb.model.GenericIterator<org.semanticwb.portal.community.DirectoryObject>(it, true);
        }

        public static org.semanticwb.portal.community.DirectoryObject getDirectoryObject(String id, org.semanticwb.model.SWBModel model)
        {
            return (org.semanticwb.portal.community.DirectoryObject)model.getSemanticObject().getModel().getGenericObject(model.getSemanticObject().getModel().getObjectUri(id,sclass),sclass);
        }

        public static org.semanticwb.portal.community.DirectoryObject createDirectoryObject(String id, org.semanticwb.model.SWBModel model)
        {
            return (org.semanticwb.portal.community.DirectoryObject)model.getSemanticObject().getModel().createGenericObject(model.getSemanticObject().getModel().getObjectUri(id, sclass), sclass);
        }

        public static void removeDirectoryObject(String id, org.semanticwb.model.SWBModel model)
        {
            model.getSemanticObject().getModel().removeSemanticObject(model.getSemanticObject().getModel().getObjectUri(id,sclass));
        }

        public static boolean hasDirectoryObject(String id, org.semanticwb.model.SWBModel model)
        {
            return (getDirectoryObject(id, model)!=null);
        }

        public static java.util.Iterator<org.semanticwb.portal.community.DirectoryObject> listDirectoryObjectByDirectoryResource(org.semanticwb.portal.community.DirectoryResource directoryresource,org.semanticwb.model.SWBModel model)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.portal.community.DirectoryObject> it=new org.semanticwb.model.GenericIterator(model.getSemanticObject().getModel().listSubjects(swbcomm_directoryResource, directoryresource.getSemanticObject()));
            return it;
        }

        public static java.util.Iterator<org.semanticwb.portal.community.DirectoryObject> listDirectoryObjectByDirectoryResource(org.semanticwb.portal.community.DirectoryResource directoryresource)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.portal.community.DirectoryObject> it=new org.semanticwb.model.GenericIterator(directoryresource.getSemanticObject().getModel().listSubjects(swbcomm_directoryResource,directoryresource.getSemanticObject()));
            return it;
        }

        public static java.util.Iterator<org.semanticwb.portal.community.DirectoryObject> listDirectoryObjectByProfile(org.semanticwb.model.WebPage hasdirprofilewebpage,org.semanticwb.model.SWBModel model)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.portal.community.DirectoryObject> it=new org.semanticwb.model.GenericIterator(model.getSemanticObject().getModel().listSubjects(swbcomm_hasDirProfileWebPage, hasdirprofilewebpage.getSemanticObject()));
            return it;
        }

        public static java.util.Iterator<org.semanticwb.portal.community.DirectoryObject> listDirectoryObjectByProfile(org.semanticwb.model.WebPage hasdirprofilewebpage)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.portal.community.DirectoryObject> it=new org.semanticwb.model.GenericIterator(hasdirprofilewebpage.getSemanticObject().getModel().listSubjects(swbcomm_hasDirProfileWebPage,hasdirprofilewebpage.getSemanticObject()));
            return it;
        }

        public static java.util.Iterator<org.semanticwb.portal.community.DirectoryObject> listDirectoryObjectByModifiedBy(org.semanticwb.model.User modifiedby,org.semanticwb.model.SWBModel model)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.portal.community.DirectoryObject> it=new org.semanticwb.model.GenericIterator(model.getSemanticObject().getModel().listSubjects(swb_modifiedBy, modifiedby.getSemanticObject()));
            return it;
        }

        public static java.util.Iterator<org.semanticwb.portal.community.DirectoryObject> listDirectoryObjectByModifiedBy(org.semanticwb.model.User modifiedby)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.portal.community.DirectoryObject> it=new org.semanticwb.model.GenericIterator(modifiedby.getSemanticObject().getModel().listSubjects(swb_modifiedBy,modifiedby.getSemanticObject()));
            return it;
        }

        public static java.util.Iterator<org.semanticwb.portal.community.DirectoryObject> listDirectoryObjectByWebPage(org.semanticwb.model.WebPage dirwebpage,org.semanticwb.model.SWBModel model)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.portal.community.DirectoryObject> it=new org.semanticwb.model.GenericIterator(model.getSemanticObject().getModel().listSubjects(swbcomm_dirWebPage, dirwebpage.getSemanticObject()));
            return it;
        }

        public static java.util.Iterator<org.semanticwb.portal.community.DirectoryObject> listDirectoryObjectByWebPage(org.semanticwb.model.WebPage dirwebpage)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.portal.community.DirectoryObject> it=new org.semanticwb.model.GenericIterator(dirwebpage.getSemanticObject().getModel().listSubjects(swbcomm_dirWebPage,dirwebpage.getSemanticObject()));
            return it;
        }

        public static java.util.Iterator<org.semanticwb.portal.community.DirectoryObject> listDirectoryObjectByTopicWebPage(org.semanticwb.model.WebPage hasdirtopicwebpage,org.semanticwb.model.SWBModel model)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.portal.community.DirectoryObject> it=new org.semanticwb.model.GenericIterator(model.getSemanticObject().getModel().listSubjects(swbcomm_hasDirTopicWebPage, hasdirtopicwebpage.getSemanticObject()));
            return it;
        }

        public static java.util.Iterator<org.semanticwb.portal.community.DirectoryObject> listDirectoryObjectByTopicWebPage(org.semanticwb.model.WebPage hasdirtopicwebpage)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.portal.community.DirectoryObject> it=new org.semanticwb.model.GenericIterator(hasdirtopicwebpage.getSemanticObject().getModel().listSubjects(swbcomm_hasDirTopicWebPage,hasdirtopicwebpage.getSemanticObject()));
            return it;
        }

        public static java.util.Iterator<org.semanticwb.portal.community.DirectoryObject> listDirectoryObjectByCreator(org.semanticwb.model.User creator,org.semanticwb.model.SWBModel model)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.portal.community.DirectoryObject> it=new org.semanticwb.model.GenericIterator(model.getSemanticObject().getModel().listSubjects(swb_creator, creator.getSemanticObject()));
            return it;
        }

        public static java.util.Iterator<org.semanticwb.portal.community.DirectoryObject> listDirectoryObjectByCreator(org.semanticwb.model.User creator)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.portal.community.DirectoryObject> it=new org.semanticwb.model.GenericIterator(creator.getSemanticObject().getModel().listSubjects(swb_creator,creator.getSemanticObject()));
            return it;
        }

        public static java.util.Iterator<org.semanticwb.portal.community.DirectoryObject> listDirectoryObjectByComment(org.semanticwb.portal.community.Comment hascomment,org.semanticwb.model.SWBModel model)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.portal.community.DirectoryObject> it=new org.semanticwb.model.GenericIterator(model.getSemanticObject().getModel().listSubjects(swbcomm_hasComment, hascomment.getSemanticObject()));
            return it;
        }

        public static java.util.Iterator<org.semanticwb.portal.community.DirectoryObject> listDirectoryObjectByComment(org.semanticwb.portal.community.Comment hascomment)
        {
            org.semanticwb.model.GenericIterator<org.semanticwb.portal.community.DirectoryObject> it=new org.semanticwb.model.GenericIterator(hascomment.getSemanticObject().getModel().listSubjects(swbcomm_hasComment,hascomment.getSemanticObject()));
            return it;
        }
    }

    public DirectoryObjectBase(org.semanticwb.platform.SemanticObject base)
    {
        super(base);
    }

    public long getReviews()
    {
        return getSemanticObject().getLongProperty(swb_reviews);
    }

    public void setReviews(long value)
    {
        getSemanticObject().setLongProperty(swb_reviews, value);
    }

    public double getRank()
    {
        return getSemanticObject().getDoubleProperty(swb_rank);
    }

    public void setRank(double value)
    {
        getSemanticObject().setDoubleProperty(swb_rank, value);
    }

    public String getTags()
    {
        return getSemanticObject().getProperty(swb_tags);
    }

    public void setTags(String value)
    {
        getSemanticObject().setProperty(swb_tags, value);
    }

    public int getAbused()
    {
        return getSemanticObject().getIntProperty(swbcomm_abused);
    }

    public void setAbused(int value)
    {
        getSemanticObject().setIntProperty(swbcomm_abused, value);
    }

    public void setDirectoryResource(org.semanticwb.portal.community.DirectoryResource value)
    {
        getSemanticObject().setObjectProperty(swbcomm_directoryResource, value.getSemanticObject());
    }

    public void removeDirectoryResource()
    {
        getSemanticObject().removeProperty(swbcomm_directoryResource);
    }

    public org.semanticwb.portal.community.DirectoryResource getDirectoryResource()
    {
         org.semanticwb.portal.community.DirectoryResource ret=null;
         org.semanticwb.platform.SemanticObject obj=getSemanticObject().getObjectProperty(swbcomm_directoryResource);
         if(obj!=null)
         {
             ret=(org.semanticwb.portal.community.DirectoryResource)obj.createGenericInstance();
         }
         return ret;
    }

    public org.semanticwb.model.GenericIterator<org.semanticwb.model.WebPage> listProfiles()
    {
        return new org.semanticwb.model.GenericIterator<org.semanticwb.model.WebPage>(getSemanticObject().listObjectProperties(swbcomm_hasDirProfileWebPage));
    }

    public boolean hasProfile(org.semanticwb.model.WebPage webpage)
    {
        boolean ret=false;
        if(webpage!=null)
        {
           ret=getSemanticObject().hasObjectProperty(swbcomm_hasDirProfileWebPage,webpage.getSemanticObject());
        }
        return ret;
    }

    public void addProfile(org.semanticwb.model.WebPage value)
    {
        getSemanticObject().addObjectProperty(swbcomm_hasDirProfileWebPage, value.getSemanticObject());
    }

    public void removeAllProfile()
    {
        getSemanticObject().removeProperty(swbcomm_hasDirProfileWebPage);
    }

    public void removeProfile(org.semanticwb.model.WebPage webpage)
    {
        getSemanticObject().removeObjectProperty(swbcomm_hasDirProfileWebPage,webpage.getSemanticObject());
    }

    public org.semanticwb.model.WebPage getProfile()
    {
         org.semanticwb.model.WebPage ret=null;
         org.semanticwb.platform.SemanticObject obj=getSemanticObject().getObjectProperty(swbcomm_hasDirProfileWebPage);
         if(obj!=null)
         {
             ret=(org.semanticwb.model.WebPage)obj.createGenericInstance();
         }
         return ret;
    }

    public java.util.Iterator<String> listExtraPhotos()
    {
        java.util.ArrayList<String> values=new java.util.ArrayList<String>();
        java.util.Iterator<org.semanticwb.platform.SemanticLiteral> it=getSemanticObject().listLiteralProperties(swbcomm_dirHasExtraPhoto);
        while(it.hasNext())
        {
                org.semanticwb.platform.SemanticLiteral literal=it.next();
                values.add(literal.getString());
        }
        return values.iterator();
    }

    public void addExtraPhoto(String extraphoto)
    {
        getSemanticObject().setProperty(swbcomm_dirHasExtraPhoto, extraphoto);
    }

    public void removeAllExtraPhoto()
    {
        getSemanticObject().removeProperty(swbcomm_dirHasExtraPhoto);
    }

    public void removeExtraPhoto(String extraphoto)
    {
        getSemanticObject().removeProperty(swbcomm_dirHasExtraPhoto,extraphoto);
    }

    public java.util.Date getCreated()
    {
        return getSemanticObject().getDateProperty(swb_created);
    }

    public void setCreated(java.util.Date value)
    {
        getSemanticObject().setDateProperty(swb_created, value);
    }

    public void setModifiedBy(org.semanticwb.model.User value)
    {
        getSemanticObject().setObjectProperty(swb_modifiedBy, value.getSemanticObject());
    }

    public void removeModifiedBy()
    {
        getSemanticObject().removeProperty(swb_modifiedBy);
    }

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

    public String getTitle()
    {
        return getSemanticObject().getProperty(swb_title);
    }

    public void setTitle(String value)
    {
        getSemanticObject().setProperty(swb_title, value);
    }

    public String getTitle(String lang)
    {
        return getSemanticObject().getProperty(swb_title, null, lang);
    }

    public String getDisplayTitle(String lang)
    {
        return getSemanticObject().getLocaleProperty(swb_title, lang);
    }

    public void setTitle(String title, String lang)
    {
        getSemanticObject().setProperty(swb_title, title, lang);
    }

    public void setWebPage(org.semanticwb.model.WebPage value)
    {
        getSemanticObject().setObjectProperty(swbcomm_dirWebPage, value.getSemanticObject());
    }

    public void removeWebPage()
    {
        getSemanticObject().removeProperty(swbcomm_dirWebPage);
    }

    public org.semanticwb.model.WebPage getWebPage()
    {
         org.semanticwb.model.WebPage ret=null;
         org.semanticwb.platform.SemanticObject obj=getSemanticObject().getObjectProperty(swbcomm_dirWebPage);
         if(obj!=null)
         {
             ret=(org.semanticwb.model.WebPage)obj.createGenericInstance();
         }
         return ret;
    }

    public java.util.Date getUpdated()
    {
        return getSemanticObject().getDateProperty(swb_updated);
    }

    public void setUpdated(java.util.Date value)
    {
        getSemanticObject().setDateProperty(swb_updated, value);
    }

    public org.semanticwb.model.GenericIterator<org.semanticwb.model.WebPage> listTopicWebPages()
    {
        return new org.semanticwb.model.GenericIterator<org.semanticwb.model.WebPage>(getSemanticObject().listObjectProperties(swbcomm_hasDirTopicWebPage));
    }

    public boolean hasTopicWebPage(org.semanticwb.model.WebPage webpage)
    {
        boolean ret=false;
        if(webpage!=null)
        {
           ret=getSemanticObject().hasObjectProperty(swbcomm_hasDirTopicWebPage,webpage.getSemanticObject());
        }
        return ret;
    }

    public void addTopicWebPage(org.semanticwb.model.WebPage value)
    {
        getSemanticObject().addObjectProperty(swbcomm_hasDirTopicWebPage, value.getSemanticObject());
    }

    public void removeAllTopicWebPage()
    {
        getSemanticObject().removeProperty(swbcomm_hasDirTopicWebPage);
    }

    public void removeTopicWebPage(org.semanticwb.model.WebPage webpage)
    {
        getSemanticObject().removeObjectProperty(swbcomm_hasDirTopicWebPage,webpage.getSemanticObject());
    }

    public org.semanticwb.model.WebPage getTopicWebPage()
    {
         org.semanticwb.model.WebPage ret=null;
         org.semanticwb.platform.SemanticObject obj=getSemanticObject().getObjectProperty(swbcomm_hasDirTopicWebPage);
         if(obj!=null)
         {
             ret=(org.semanticwb.model.WebPage)obj.createGenericInstance();
         }
         return ret;
    }

    public String getPhoto()
    {
        return getSemanticObject().getProperty(swbcomm_dirPhoto);
    }

    public void setPhoto(String value)
    {
        getSemanticObject().setProperty(swbcomm_dirPhoto, value);
    }

    public void setCreator(org.semanticwb.model.User value)
    {
        getSemanticObject().setObjectProperty(swb_creator, value.getSemanticObject());
    }

    public void removeCreator()
    {
        getSemanticObject().removeProperty(swb_creator);
    }

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

    public org.semanticwb.model.GenericIterator<org.semanticwb.portal.community.Comment> listComments()
    {
        return new org.semanticwb.model.GenericIterator<org.semanticwb.portal.community.Comment>(getSemanticObject().listObjectProperties(swbcomm_hasComment));
    }

    public boolean hasComment(org.semanticwb.portal.community.Comment comment)
    {
        boolean ret=false;
        if(comment!=null)
        {
           ret=getSemanticObject().hasObjectProperty(swbcomm_hasComment,comment.getSemanticObject());
        }
        return ret;
    }

    public void addComment(org.semanticwb.portal.community.Comment value)
    {
        getSemanticObject().addObjectProperty(swbcomm_hasComment, value.getSemanticObject());
    }

    public void removeAllComment()
    {
        getSemanticObject().removeProperty(swbcomm_hasComment);
    }

    public void removeComment(org.semanticwb.portal.community.Comment comment)
    {
        getSemanticObject().removeObjectProperty(swbcomm_hasComment,comment.getSemanticObject());
    }

    public org.semanticwb.portal.community.Comment getComment()
    {
         org.semanticwb.portal.community.Comment ret=null;
         org.semanticwb.platform.SemanticObject obj=getSemanticObject().getObjectProperty(swbcomm_hasComment);
         if(obj!=null)
         {
             ret=(org.semanticwb.portal.community.Comment)obj.createGenericInstance();
         }
         return ret;
    }

    public String getDescription()
    {
        return getSemanticObject().getProperty(swb_description);
    }

    public void setDescription(String value)
    {
        getSemanticObject().setProperty(swb_description, value);
    }

    public String getDescription(String lang)
    {
        return getSemanticObject().getProperty(swb_description, null, lang);
    }

    public String getDisplayDescription(String lang)
    {
        return getSemanticObject().getLocaleProperty(swb_description, lang);
    }

    public void setDescription(String description, String lang)
    {
        getSemanticObject().setProperty(swb_description, description, lang);
    }

    public void remove()
    {
        getSemanticObject().remove();
    }

    public java.util.Iterator<org.semanticwb.model.GenericObject> listRelatedObjects()
    {
        return new org.semanticwb.model.GenericIterator(getSemanticObject().listRelatedObjects(),true);
    }
    
    public String getTags(String lang)
    {
        return getSemanticObject().getProperty(swb_tags, null, lang);
    }

    public String getDisplayTags(String lang)
    {
        return getSemanticObject().getLocaleProperty(swb_tags, lang);
    }

    public void setTags(String tags, String lang)
    {
        getSemanticObject().setProperty(swb_tags, tags, lang);
    }    
}

package org.semanticwb.model.base;

import java.util.Iterator;
import org.semanticwb.model.*;
import org.semanticwb.SWBPlatform;
import org.semanticwb.platform.SemanticMgr;
import org.semanticwb.platform.SemanticModel;
import org.semanticwb.platform.SemanticObject;
import org.semanticwb.platform.SemanticClass;
import org.semanticwb.repository.File;
import org.semanticwb.repository.FrozenNode;
import org.semanticwb.repository.Unstructured;
import org.semanticwb.repository.BaseNode;
import org.semanticwb.repository.HierarchyNode;
import org.semanticwb.repository.VersionLabels;
import org.semanticwb.repository.VersionHistory;
import org.semanticwb.repository.Resource;
import org.semanticwb.repository.Folder;
import org.semanticwb.repository.Version;
import org.semanticwb.repository.Workspace;
public class SWBContextBase
{
    private static SemanticMgr mgr=org.semanticwb.SWBPlatform.getSemanticMgr();
    public static final SemanticClass swb_WebSite=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/ontology#WebSite");

    public static WebSite getWebSite(String name)
    {
        return WebSite.getWebSite(name);
    }

    public static java.util.Iterator<org.semanticwb.model.WebSite> listWebSites()
    {
        return (java.util.Iterator<org.semanticwb.model.WebSite>)swb_WebSite.listGenericInstances();
    }

    public static void removeWebSite(String name)
    {
        WebSite.removeWebSite(name);
    }

    public static WebSite createWebSite(String name, String namespace)
    {
       return WebSite.createWebSite(name, namespace);
    }
    public static final SemanticClass swb_UserRepository=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/ontology#UserRepository");

    public static UserRepository getUserRepository(String name)
    {
        return UserRepository.getUserRepository(name);
    }

    public static java.util.Iterator<org.semanticwb.model.UserRepository> listUserRepositorys()
    {
        return (java.util.Iterator<org.semanticwb.model.UserRepository>)swb_UserRepository.listGenericInstances();
    }

    public static void removeUserRepository(String name)
    {
        UserRepository.removeUserRepository(name);
    }

    public static UserRepository createUserRepository(String name, String namespace)
    {
       return UserRepository.createUserRepository(name, namespace);
    }
    public static final SemanticClass swbrep_Workspace=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/repository#Workspace");

    public static Workspace getWorkspace(String name)
    {
        return Workspace.getWorkspace(name);
    }

    public static java.util.Iterator<org.semanticwb.repository.Workspace> listWorkspaces()
    {
        return (java.util.Iterator<org.semanticwb.repository.Workspace>)swbrep_Workspace.listGenericInstances();
    }

    public static void removeWorkspace(String name)
    {
        Workspace.removeWorkspace(name);
    }

    public static Workspace createWorkspace(String name, String namespace)
    {
       return Workspace.createWorkspace(name, namespace);
    }
}

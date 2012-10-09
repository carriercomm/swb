/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.semanticwb.social.components.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.semanticwb.SWBPlatform;
import org.semanticwb.SWBPortal;
import org.semanticwb.model.Descriptiveable;
import org.semanticwb.model.DisplayObject;
import org.semanticwb.model.GenericObject;
import org.semanticwb.model.SWBModel;
import org.semanticwb.model.User;
import org.semanticwb.model.WebPage;
import org.semanticwb.model.WebSite;
import org.semanticwb.platform.SemanticClass;
import org.semanticwb.portal.api.SWBParamRequest;
import org.semanticwb.social.Childrenable;
import org.semanticwb.social.SocialSite;
import org.semanticwb.social.TreeNodePage;

/**
 *
 * @author jorge.jimenez
 * @date 10/03/2012
 */

 /*
  * Clase controladora del modelo de datos del árbol de navegación
  */
 public class ElementList {

    private ElementTreeNode root;
    private WebSite modelAdmin=null;
    private User user=null;
    private String ImgAdminPathBase=null;

    /*
     * Constructor de la clase
     * @paramRequest SWBParamRequest que llega desde el recurso que controla la plantilla de la
     * administración de la herramienta de swbsocial
     */
    public ElementList(SWBParamRequest paramRequest) {
        modelAdmin=paramRequest.getWebPage().getWebSite();
        ImgAdminPathBase="/work/models/"+modelAdmin.getId()+"/admin/img/";
        user=paramRequest.getUser();
        root=new ElementTreeNode(new Element("Company Brands","",""),getSocialSites(), true);
    }

    /*
     * Obtención de sitios de tipo SocialSite
     */
    private ElementTreeNode[] getSocialSites()
    {
        DisplayObject displayObj=(DisplayObject)SocialSite.sclass.getDisplayObject().createGenericInstance();
        ArrayList<SocialSite> alist=new ArrayList();
        int cont=0;
        Iterator <SocialSite> itSocialSites=SocialSite.ClassMgr.listSocialSites();
        while(itSocialSites.hasNext())
        {
            SocialSite socialSite=itSocialSites.next();
            alist.add(socialSite);
            cont++;
        }
        ElementTreeNode[] elementTreeNode=new ElementTreeNode[cont];
        itSocialSites=alist.iterator();
        int cont2=0;
        while(itSocialSites.hasNext())
        {
            SocialSite socialSite=itSocialSites.next();
            elementTreeNode[cont2]=new ElementTreeNode(new Element(socialSite.getTitle(), socialSite.getURI(), ImgAdminPathBase+displayObj.getIconClass()),getTreeCategoryNodes(socialSite),true);
            cont2++;
        }
        return elementTreeNode;
    }

    /*
     * Obtención de las categorías del árbol, estas descienden del nodo TreeCategoryNodes del sitio
     * de administración
     * @param model SWBModel que es enviado desde el metodo anterior (getSocialSites)
     */
    private ElementTreeNode[] getTreeCategoryNodes(SWBModel model)
    {
        ArrayList<TreeNodePage> alist=new ArrayList();
        WebSite adminWSite=WebSite.ClassMgr.getWebSite(modelAdmin.getId());
        WebPage treePageHome=adminWSite.getWebPage("TreeCategoryNodes");
        int cont=0;
        Iterator <WebPage> itChilds=treePageHome.listVisibleChilds(user.getLanguage());
        while(itChilds.hasNext())
        {
            WebPage page=itChilds.next();
            if(page instanceof TreeNodePage)
            {
                TreeNodePage treeNodePage=(TreeNodePage)page;
                alist.add(treeNodePage);
                cont++;
            }
        }
        ElementTreeNode[] elementTreeNode=new ElementTreeNode[cont];
        Iterator<TreeNodePage> itTreeNodes=alist.iterator();
        int cont2=0;
        while(itTreeNodes.hasNext())
        {
            TreeNodePage treeNode=itTreeNodes.next();
            String iconImgPath=SWBPortal.getWebWorkPath()+treeNode.getWorkPath()+"/"+treeNode.social_wpImg.getName()+"_"+treeNode.getId()+"_"+treeNode.getWpImg();
            elementTreeNode[cont2]=new ElementTreeNode(new Element(treeNode.getTitle(), treeNode.getId(), treeNode.getZulResourcePath(), iconImgPath, null, model.getId()),getTreeNodeElements(treeNode.getClassUri(), model, treeNode.getId()), true);
            cont2++;
        }
        return elementTreeNode;
    }

    /*
     * Obtención de instancias del tipo de clase que contiene la categoría en su propiedad "tree_classUri"
     * @param classUri String uri de la categoría
     * @param model SWBModel de la categoría
     * @param categoryID String id de la categoría
     */
    private ElementTreeNode[] getTreeNodeElements(String classUri, SWBModel model, String categoryID)
    {
        if(classUri==null) return new ElementTreeNode[0];
        HashMap<GenericObject, String> hmapElements=new HashMap();
        int cont=0;
        SemanticClass swbClass = SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass(classUri);

        if(swbClass==null) return new ElementTreeNode[0];
        Iterator <GenericObject> itGenObjs=model.listInstancesOfClass(swbClass);
        while(itGenObjs.hasNext())
        {
            GenericObject genObj=itGenObjs.next();
            if(genObj instanceof Childrenable)
            {
                Childrenable child=(Childrenable)genObj;
                //Si no tiene padre, se agrega al principal
                if(child.getParentObj()==null)
                {
                    hmapElements.put(genObj,"true");
                    cont++;
                }
            }else
            {
                hmapElements.put(genObj,"false");
                cont++;
            }
        }
        ElementTreeNode[] elementTreeNode=new ElementTreeNode[cont];
        itGenObjs=hmapElements.keySet().iterator();
        int cont2=0;
        while(itGenObjs.hasNext())
        {
            GenericObject genObj=itGenObjs.next();
            DisplayObject displayObj=(DisplayObject)genObj.getSemanticObject().getSemanticClass().getDisplayObject().createGenericInstance();
            if(hmapElements.get(genObj)!=null && hmapElements.get(genObj).trim().equalsIgnoreCase("true"))
            {
                elementTreeNode[cont2]=new ElementTreeNode(new Element(genObj.getSemanticObject().getProperty(Descriptiveable.swb_title), genObj.getURI(), null, ImgAdminPathBase+displayObj.getIconClass(), categoryID, model.getId()), getTreeNodeChildrenElements(genObj, model, categoryID), true);
            }else
            {
                elementTreeNode[cont2]=new ElementTreeNode(new Element(genObj.getSemanticObject().getProperty(Descriptiveable.swb_title), genObj.getURI(), null,ImgAdminPathBase+displayObj.getIconClass(), categoryID, model.getId()));
            }
            cont2++;
        }
        return elementTreeNode;
    }


    /*
     * Obtención de nodos o elementos hijos de un GenericObject que es de tipo Childrenable, esto, de manera recursiva
     * @param genObj GenericObject de tipo childrenable a desplegar en el árbol de navegación
     * @param model SWBModel de la categoría
     * @param categoryID String del id de la categoría en cuestión
     */
    private ElementTreeNode[] getTreeNodeChildrenElements(GenericObject genObj, SWBModel model, String categoryID)
    {
        if(genObj instanceof Childrenable)
        {
            ArrayList<Childrenable> alist=new ArrayList();
            int cont=0;
            Childrenable childrenable = (Childrenable) genObj;
            Iterator<Childrenable> itChildren=childrenable.listChildrenObjInvs();
            while(itChildren.hasNext())
            {
                Childrenable child=itChildren.next();
                alist.add(child);
                cont++;
            }
            ElementTreeNode[] elementTreeNode=new ElementTreeNode[cont];
            itChildren=alist.iterator();
            int cont2=0;
            while(itChildren.hasNext())
            {
                Childrenable child=itChildren.next();
                GenericObject childGenObj=child.getSemanticObject().getGenericInstance();
                DisplayObject displayObj=(DisplayObject)childGenObj.getSemanticObject().getSemanticClass().getDisplayObject().createGenericInstance();
                elementTreeNode[cont2]=new ElementTreeNode(new Element(childGenObj.getSemanticObject().getProperty(Descriptiveable.swb_title), childGenObj.getURI(), null, ImgAdminPathBase+displayObj.getIconClass(), categoryID, model.getId()),getTreeNodeChildrenElements(childGenObj, model, categoryID), true);
                cont2++;
            }
            return elementTreeNode;
        }
        return null;
    }

    /*
     * Obtención del modelo de datos generado por la clase.
     */
    public ElementTreeNode getRoot() {
        return root;
    }
}
/*
 * SemanticWebBuilder es una plataforma para el desarrollo de portales y aplicaciones de integración,
 * colaboración y conocimiento, que gracias al uso de tecnología semántica puede generar contextos de
 * información alrededor de algún tema de interés o bien integrar información y aplicaciones de diferentes
 * fuentes, donde a la información se le asigna un significado, de forma que pueda ser interpretada y
 * procesada por personas y/o sistemas, es una creación original del Fondo de Información y Documentación
 * para la Industria INFOTEC, cuyo registro se encuentra actualmente en trámite.
 *
 * INFOTEC pone a su disposición la herramienta SemanticWebBuilder a través de su licenciamiento abierto al público (‘open source’),
 * en virtud del cual, usted podrá usarlo en las mismas condiciones con que INFOTEC lo ha diseñado y puesto a su disposición;
 * aprender de él; distribuirlo a terceros; acceder a su código fuente y modificarlo, y combinarlo o enlazarlo con otro software,
 * todo ello de conformidad con los términos y condiciones de la LICENCIA ABIERTA AL PÚBLICO que otorga INFOTEC para la utilización
 * del SemanticWebBuilder 4.0.
 *
 * INFOTEC no otorga garantía sobre SemanticWebBuilder, de ninguna especie y naturaleza, ni implícita ni explícita,
 * siendo usted completamente responsable de la utilización que le dé y asumiendo la totalidad de los riesgos que puedan derivar
 * de la misma.
 *
 * Si usted tiene cualquier duda o comentario sobre SemanticWebBuilder, INFOTEC pone a su disposición la siguiente
 * dirección electrónica:
 *  http://www.semanticwebbuilder.org
 */
package org.semanticwb.model.base;


   /**
   * Elemento que muestra un componente grafico para seleccionar una opcion dentro de elementos herarquizados 
   */
public abstract class SelectTreeBase extends org.semanticwb.model.SelectOne 
{
    public static final org.semanticwb.platform.SemanticProperty swbxf_st_startElement=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/xforms/ontology#st_startElement");
   /**
   * Elemento que muestra un componente grafico para seleccionar una opcion dentro de elementos herarquizados
   */
    public static final org.semanticwb.platform.SemanticClass swbxf_SelectTree=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/xforms/ontology#SelectTree");
   /**
   * The semantic class that represents the currentObject
   */
    public static final org.semanticwb.platform.SemanticClass sclass=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/xforms/ontology#SelectTree");

    public static class ClassMgr
    {
       /**
       * Returns a list of SelectTree for a model
       * @param model Model to find
       * @return Iterator of org.semanticwb.model.SelectTree
       */

        public static java.util.Iterator<org.semanticwb.model.SelectTree> listSelectTrees(org.semanticwb.model.SWBModel model)
        {
            java.util.Iterator it=model.getSemanticObject().getModel().listInstancesOfClass(sclass);
            return new org.semanticwb.model.GenericIterator<org.semanticwb.model.SelectTree>(it, true);
        }
       /**
       * Returns a list of org.semanticwb.model.SelectTree for all models
       * @return Iterator of org.semanticwb.model.SelectTree
       */

        public static java.util.Iterator<org.semanticwb.model.SelectTree> listSelectTrees()
        {
            java.util.Iterator it=sclass.listInstances();
            return new org.semanticwb.model.GenericIterator<org.semanticwb.model.SelectTree>(it, true);
        }
       /**
       * Gets a org.semanticwb.model.SelectTree
       * @param id Identifier for org.semanticwb.model.SelectTree
       * @param model Model of the org.semanticwb.model.SelectTree
       * @return A org.semanticwb.model.SelectTree
       */
        public static org.semanticwb.model.SelectTree getSelectTree(String id, org.semanticwb.model.SWBModel model)
        {
            return (org.semanticwb.model.SelectTree)model.getSemanticObject().getModel().getGenericObject(model.getSemanticObject().getModel().getObjectUri(id,sclass),sclass);
        }
       /**
       * Create a org.semanticwb.model.SelectTree
       * @param id Identifier for org.semanticwb.model.SelectTree
       * @param model Model of the org.semanticwb.model.SelectTree
       * @return A org.semanticwb.model.SelectTree
       */
        public static org.semanticwb.model.SelectTree createSelectTree(String id, org.semanticwb.model.SWBModel model)
        {
            return (org.semanticwb.model.SelectTree)model.getSemanticObject().getModel().createGenericObject(model.getSemanticObject().getModel().getObjectUri(id,sclass),sclass);
        }
       /**
       * Remove a org.semanticwb.model.SelectTree
       * @param id Identifier for org.semanticwb.model.SelectTree
       * @param model Model of the org.semanticwb.model.SelectTree
       */
        public static void removeSelectTree(String id, org.semanticwb.model.SWBModel model)
        {
            model.getSemanticObject().getModel().removeSemanticObject(model.getSemanticObject().getModel().getObjectUri(id,sclass));
        }
       /**
       * Returns true if exists a org.semanticwb.model.SelectTree
       * @param id Identifier for org.semanticwb.model.SelectTree
       * @param model Model of the org.semanticwb.model.SelectTree
       * @return true if the org.semanticwb.model.SelectTree exists, false otherwise
       */

        public static boolean hasSelectTree(String id, org.semanticwb.model.SWBModel model)
        {
            return (getSelectTree(id, model)!=null);
        }
    }

    public static SelectTreeBase.ClassMgr getSelectTreeClassMgr()
    {
        return new SelectTreeBase.ClassMgr();
    }

   /**
   * Constructs a SelectTreeBase with a SemanticObject
   * @param base The SemanticObject with the properties for the SelectTree
   */
    public SelectTreeBase(org.semanticwb.platform.SemanticObject base)
    {
        super(base);
    }

/**
* Gets the StartElement property
* @return String with the StartElement
*/
    public String getStartElement()
    {
        return getSemanticObject().getProperty(swbxf_st_startElement);
    }

/**
* Sets the StartElement property
* @param value long with the StartElement
*/
    public void setStartElement(String value)
    {
        getSemanticObject().setProperty(swbxf_st_startElement, value);
    }
}

/**  
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
 **/
package org.semanticwb.model.base;

   // TODO: Auto-generated Javadoc
/**
    * Interfaz que define propiedades para elementos que pueden describirse.
    */
public interface DescriptiveableBase extends org.semanticwb.model.GenericObject
{
   
   /** Nombre del elemento. */
    public static final org.semanticwb.platform.SemanticProperty swb_title=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/ontology#title");
   
   /** Descripción del elemento. */
    public static final org.semanticwb.platform.SemanticProperty swb_description=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/ontology#description");
   
   /** Interfaz que define propiedades para elementos que pueden describirse. */
    public static final org.semanticwb.platform.SemanticClass swb_Descriptiveable=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/ontology#Descriptiveable");

    /**
     * Gets the title.
     * 
     * @return the title
     */
    public String getTitle();

    /**
     * Sets the title.
     * 
     * @param value the new title
     */
    public void setTitle(String value);

    /**
     * Gets the title.
     * 
     * @param lang the lang
     * @return the title
     */
    public String getTitle(String lang);

    /**
     * Gets the display title.
     * 
     * @param lang the lang
     * @return the display title
     */
    public String getDisplayTitle(String lang);

    /**
     * Sets the title.
     * 
     * @param title the title
     * @param lang the lang
     */
    public void setTitle(String title, String lang);

    /**
     * Gets the description.
     * 
     * @return the description
     */
    public String getDescription();

    /**
     * Sets the description.
     * 
     * @param value the new description
     */
    public void setDescription(String value);

    /**
     * Gets the description.
     * 
     * @param lang the lang
     * @return the description
     */
    public String getDescription(String lang);

    /**
     * Gets the display description.
     * 
     * @param lang the lang
     * @return the display description
     */
    public String getDisplayDescription(String lang);

    /**
     * Sets the description.
     * 
     * @param description the description
     * @param lang the lang
     */
    public void setDescription(String description, String lang);
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.semanticwb.platform;

import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import org.semanticwb.SWBPlatform;
import org.semanticwb.base.util.URLEncoder;

/**
 *
 * @author Jei
 */
public class SemanticProperty 
{
    private Property m_prop;
    private SemanticProperty m_inverse;
    
    private Boolean isObjectProperty=null;
    private Boolean isDataTypeProperty=null;
    private Boolean hasInverse=null;
    private boolean isInverse=false;
    private Boolean isExternalInvocation=null;

    private int cardinality=0;
    private boolean cardinalityCheck=false;
    
    public SemanticProperty(Property prop)
    {
        this.m_prop=prop;
        if(hasInverse())
        {
            if(m_prop instanceof OntProperty)
            {
                m_inverse=SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty(((OntProperty)m_prop).getInverse());
                m_inverse.isInverse=true;
                m_inverse.m_inverse=this;
                //System.out.println(prop+" hasInverse "+m_inverse);
            }
        }
    }
    
    public Property getRDFProperty()
    {
        return m_prop;
    }
    
    public String getName()
    {
        return m_prop.getLocalName();
    }
    
    public String getPrefix()
    {
        return m_prop.getModel().getNsURIPrefix(m_prop.getNameSpace());
    }    
    
    public String getLabel()
    {
        String ret=null;
        if(m_prop instanceof OntProperty)
        {
            ret=((OntProperty)m_prop).getLabel(null);
        }
        return ret;
    }
    
    public String getURI()
    {
        return m_prop.getURI();
    }
    
    /**
     * Regresa URI codificado para utilizar en ligas de html
     * @return URI Codificado
     */
    public String getEncodedURI()
    {
        return URLEncoder.encode(getURI());
    }     
    
    public SemanticLiteral getRequiredProperty(SemanticProperty prop)
    {
        SemanticLiteral ret=null;
        Statement st=m_prop.getProperty(prop.getRDFProperty());
        if(st!=null)
        {
            ret=new SemanticLiteral(st);
        }
        return ret;
    }
    
    public boolean isLocaleable()
    {
        Statement st=m_prop.getProperty(SWBPlatform.getSemanticMgr().getOntology().getRDFOntModel().getProperty(SemanticVocabulary.SWB_PROP_LOCALEABLE));
        if(st!=null)
        {
            return st.getBoolean();
        }
        return false;
    }
    
    public boolean isUsedAsName()
    {
        Statement st=m_prop.getProperty(SWBPlatform.getSemanticMgr().getOntology().getRDFOntModel().getProperty(SemanticVocabulary.SWB_ANNOT_CANUSEDASNAME));
        if(st!=null)
        {
            return st.getBoolean();
        }
        return false;
    }

    public boolean isRequired()
    {
        Statement st=m_prop.getProperty(SWBPlatform.getSemanticMgr().getOntology().getRDFOntModel().getProperty(SemanticVocabulary.SWB_PROP_REQUIRED));
        if(st!=null)
        {
            return st.getBoolean();
        }
        return false;
    }

    /**
     * Si esta propiedad se utiliza para definir la relacio padre-hijo en el arbol de navegacion
     * @return
     */
    public boolean isHeraquicalRelation()
    {
        Statement st=m_prop.getProperty(SWBPlatform.getSemanticMgr().getOntology().getRDFOntModel().getProperty(SemanticVocabulary.SWB_PROP_HERARQUICALRELATION));
        if(st!=null)
        {
            return st.getBoolean();
        }
        return false;
    }

    /**
     * Si esta propiedad se utiliza para definir la relacio padre-hijo en el arbol de navegacion
     * @return
     */
    public boolean isRemoveDependency()
    {
        Statement st=m_prop.getProperty(SWBPlatform.getSemanticMgr().getOntology().getRDFOntModel().getProperty(SemanticVocabulary.SWB_PROP_REMOVEDEPENDENCY));
        if(st!=null)
        {
            return st.getBoolean();
        }
        return false;
    }


    /**
     * Si esta propiedad se utiliza para definir la relacio padre-hijo en el arbol de navegacion
     * @return
     */
    public boolean isInverseHeraquicalRelation()
    {
        boolean ret=false;
        SemanticProperty inv=getInverse();
        if(inv!=null && inv.isHeraquicalRelation())
        {
            ret=true;
        }
        return ret;
    }

    public boolean isExternalInvocation()
    {
        if(isExternalInvocation==null)
        {
            isExternalInvocation=false;
            Statement st=m_prop.getProperty(SWBPlatform.getSemanticMgr().getOntology().getRDFOntModel().getProperty(SemanticVocabulary.SWB_PROP_EXTERNALINVOCATION));
            if(st!=null)
            {
                isExternalInvocation=st.getBoolean();
            }
        }
        return isExternalInvocation;
    }      
    
    public SemanticObject getDisplayProperty()
    {
        SemanticObject disp=null;
        Statement st=m_prop.getProperty(SWBPlatform.getSemanticMgr().getOntology().getRDFOntModel().getProperty(SemanticVocabulary.SWB_PROP_DISPLAYPROPERTY));
        if(st!=null)
        {
            disp=SemanticObject.createSemanticObject(st.getResource());
        }
        return disp;
    }
    
    public String getDisplayName()
    {
        return getDisplayName(null);
    }
    
    public String getDisplayName(String lang)
    {
        String ret=null;
        SemanticObject obj=getDisplayProperty();
        if(obj!=null)
        {
            if(lang!=null)
            {
                ret=obj.getProperty(obj.getModel().getSemanticProperty(SemanticVocabulary.RDFS_LABEL),null,lang);
            }
            if(ret==null)ret=obj.getProperty(obj.getModel().getSemanticProperty(SemanticVocabulary.RDFS_LABEL));
        }
        if(ret==null)ret=getLabel();
        if(ret==null)ret=getName();
        //System.out.println("Prop:"+obj+" "+ret);
        return ret;
    }    
//    
//    public String getViewGroup()
//    {
//        String ret=null;
//        Statement st=m_prop.getProperty(SWBPlatform.getSemanticMgr().getOntology().getRDFOntModel().getProperty(SemanticVocabulary.SWB_ANNOT_PROPGROUP));
//        if(st!=null)
//        {
//            return st.getString();
//        }
//        return ret;
//    }       
//    
//    public int getSortIndex()
//    {
//        int ret=99999999;
//        Statement st=m_prop.getProperty(SWBPlatform.getSemanticMgr().getOntology().getRDFOntModel().getProperty(SemanticVocabulary.SWB_ANNOT_PROPINDEX));
//        if(st!=null)
//        {
//            return st.getInt();
//        }
//        return ret;
//    }    
    
    @Override
    public String toString()
    {
        return m_prop.toString();
    }

    @Override
    public int hashCode() 
    {
        return m_prop.hashCode();
    }

    @Override
    public boolean equals(Object obj) 
    {
        return hashCode()==obj.hashCode();
    }       
    
    public SemanticClass getDomainClass()
    {
        if(hasInverse())
        {
            return m_inverse.getRangeClass();
        }
        SemanticClass ret=null;
        Statement stm=m_prop.getProperty(m_prop.getModel().getProperty(SemanticVocabulary.RDFS_DOMAIN));
        if(stm!=null)
        {
            ret=SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass(stm.getResource().getURI());
        }
        return ret;    
    }
    
    public SemanticClass getRangeClass()
    {
        if(hasInverse())
        {
            return m_inverse.getDomainClass();
        }        
        SemanticClass ret=null;
        Statement stm=m_prop.getProperty(m_prop.getModel().getProperty(SemanticVocabulary.RDFS_RANGE));
        if(stm!=null)
        {
            ret=SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass(stm.getResource().getURI());
        }
        return ret;    
    }
    
    public Resource getRange()
    {
        Resource ret=null;
        Statement stm=m_prop.getProperty(m_prop.getModel().getProperty(SemanticVocabulary.RDFS_RANGE));
        if(stm!=null)
        {
            ret=stm.getResource();
        }
        return ret;          
    }
    
    public int getCardinality()
    {
        if(!cardinalityCheck)
        {
            String n=getLabel();
            if(n==null)n=getName();
            if(n.startsWith("has"))
            {
                cardinality=0;
            }else
            {
                cardinality=1;
            }
            cardinalityCheck=true;
        }
        return cardinality;
    }
    
    public boolean isObjectProperty()
    {
        if(isObjectProperty==null)
        {
            isObjectProperty=false;
            Statement stm=m_prop.getProperty(m_prop.getModel().getProperty(SemanticVocabulary.RDF_TYPE));
            if(stm!=null)
            {
                isObjectProperty=SemanticVocabulary.OWL_OBJECTPROPERTY.equals(stm.getResource().getURI());
            }
        }
        return isObjectProperty;      
    }
    
    public boolean isDataTypeProperty()
    {
        if(isDataTypeProperty==null)
        {
            isDataTypeProperty=false;
            Statement stm=m_prop.getProperty(m_prop.getModel().getProperty(SemanticVocabulary.RDF_TYPE));
            if(stm!=null)
            {
                isDataTypeProperty=SemanticVocabulary.OWL_DATATYPEPROPERTY.equals(stm.getResource().getURI());
            }
        }
        return isDataTypeProperty;
    }

    /**
     * Esta propiedad es la inversa de otra (no genera statements)
     * @return
     */
    public boolean hasInverse()
    {
        if(hasInverse==null)
        {
            hasInverse=false;
            if(m_prop instanceof OntProperty)
            {
                hasInverse=((OntProperty)m_prop).hasInverse();
            }        
        }
        return hasInverse;
    }

    /**
     * Esta propiedad es normal pero tiene una inversa
     * @return
     */
    public boolean isInverseOf()
    {
        return isInverse;
    }

    
    public SemanticProperty getInverse()
    {
        return m_inverse;
    }
    
    public boolean isBoolean()
    {
        boolean ret=false;
        Resource res=getRange();
        if(res!=null && res.getURI().equals(SemanticVocabulary.XMLS_BOOLEAN))ret=true;
        return ret;        
    }
    
    public boolean isInt()
    {
        boolean ret=false;
        Resource res=getRange();
        if(res!=null && res.getURI().equals(SemanticVocabulary.XMLS_INT))ret=true;
        return ret;        
    }    

    public boolean isBinary()
    {
         boolean ret=false;
        Resource res=getRange();
        if(res!=null && res.getURI().equals(SemanticVocabulary.XMLS_BASE64BINARY))ret=true;
        return ret;
    }
    public boolean isLong()
    {
        boolean ret=false;
        Resource res=getRange();
        if(res!=null && res.getURI().equals(SemanticVocabulary.XMLS_LONG))ret=true;
        return ret;        
    }        
    
    public boolean isDate()
    {
        boolean ret=false;
        Resource res=getRange();
        if(res!=null && res.getURI().equals(SemanticVocabulary.XMLS_DATE))ret=true;
        return ret;        
    }    
    
    public boolean isDateTime()
    {
        boolean ret=false;
        Resource res=getRange();
        if(res!=null && res.getURI().equals(SemanticVocabulary.XMLS_DATETIME))ret=true;
        return ret;        
    }       
    
    public boolean isString()
    {
        boolean ret=false;
        Resource res=getRange();
        if(res!=null && res.getURI().equals(SemanticVocabulary.XMLS_STRING))ret=true;
        return ret;        
    }       
    
    public boolean isFloat()
    {
        boolean ret=false;
        Resource res=getRange();
        if(res!=null && res.getURI().equals(SemanticVocabulary.XMLS_FLOAT))ret=true;
        return ret;        
    }       
}

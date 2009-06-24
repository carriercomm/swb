/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.semanticwb.platform;

import com.hp.hpl.jena.ontology.OntClass;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.shared.PropertyNotFoundException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.semanticwb.*;
import org.semanticwb.base.util.URLEncoder;
import org.semanticwb.model.GenericIterator;
import org.semanticwb.model.GenericObject;

/**
 *
 * @author Jei
 */
public class SemanticClass
{
    private static Logger log=SWBUtils.getLogger(SemanticClass.class);

    private OntClass m_class;                           //clase ontologia schema
    private OntClass m_ontclass;                        //clase ontologia global
    private HashMap<String,SemanticProperty> m_props;
    private Boolean m_isSWBClass=null;
    private Boolean m_isSWBInterface=null;
    private Boolean m_isSWBModel=null;
    private Boolean m_isSWBFormElement=null;
    private Boolean m_isSWBSemanticResource=null;
    private String m_classCodeName=null;
    private String m_classCodePackage=null;
    private Boolean m_autogenId=null;
    private Class m_cls=null;
    private Constructor m_constructor=null;
    private SemanticProperty displayNameProperty;
    List<SemanticProperty> herarquicalProps;
    List<SemanticProperty> inverseHerarquicalProps;
    private String m_classGroupId=null;
    private boolean m_isClassGroupIdCheck=false;

//    private Boolean isdragSupport=null;

    private boolean dispObject=false;
    private SemanticObject displayObject=null;
    private String m_className=null;


    public SemanticClass(OntClass oclass)
    {
        this.m_class=oclass;
        init();
    }

    public SemanticClass(String classuri) throws SWBException
    {
        this.m_class=SWBPlatform.getSemanticMgr().getSchema().getRDFOntModel().getOntClass(classuri);
        if(this.m_class==null) throw new SWBException("OntClass Not Found");
        init();
    }

    public SemanticObject getSemanticObject()
    {
        return SWBPlatform.getSemanticMgr().getSchema().getSemanticObject(getURI());
    }

    private void init()
    {
        m_ontclass=SWBPlatform.getSemanticMgr().getOntology().getRDFOntModel().getOntClass(m_class.getURI());
        //System.out.println(m_class+" "+m_ontclass);
        m_props=new HashMap();
        herarquicalProps=new ArrayList();
        inverseHerarquicalProps=new ArrayList();
        // super-classes
        //System.out.println("m_class:"+m_class);
        int x=0;
        for (Iterator i = m_class.listDeclaredProperties(false); i.hasNext(); )
        {
            Property prop=(Property)i.next();
            x++;
            SemanticProperty p=SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty(prop);
            if(p.isUsedAsName())displayNameProperty=p;
            if(p.isHeraquicalRelation())herarquicalProps.add(p);
            if(p.isInverseHeraquicalRelation())inverseHerarquicalProps.add(p);
            //System.out.println("p.getName():"+x+" "+p.getName()+" "+p);
            m_props.put( p.getName(), p);
        }
        //System.out.println("m_props:"+m_props.size());
        log.trace("SemanticClass:"+getName()+" "+getClassCodeName()+" "+m_class.getNameSpace()+" "+getPrefix());
        //System.out.println("Name:"+getName()+" "+getClassName()+" "+m_class.getNameSpace()+" "+getPrefix());
    }

    /**
     * regresa nombre local de la clase
     * @return
     */
    public String getName()
    {
        return m_class.getLocalName();
    }

    /**
     * Regresa Prefijo de la clase en base al NS de la ontologia
     */
    public String getPrefix()
    {
        return m_class.getOntModel().getNsURIPrefix(m_class.getNameSpace());
    }

    /**
     * Regresa nombre de la clase con paquete, siempre y cuendo sea del tipo swb:Class
     * @return
     */
    public String getClassName()
    {
        if(m_className==null)
        {
            SemanticClass cls=this;
            while(cls!=null)
            {
                if(cls.isSWB())
                {
                    m_className=getCodePackage()+"."+getClassCodeName();
                    break;
                }else
                {
                    Iterator<SemanticClass> it=cls.listSuperClasses(true);
                    cls=null;
                    SemanticClass acls=null;
                    while(it.hasNext())
                    {
                        SemanticClass aux=it.next();
                        if(aux.isSWBClass())cls=aux;
                        if(!aux.isSWB())acls=aux;
                    }
                    if(cls==null)cls=acls;
                }
            }
        }
        return m_className;
    }

    /**
     * Regresa paquete de la clase generica java definido den la ontologia
     * @return
     */
    public String getCodePackage()
    {
        if(m_classCodePackage==null)
        {
            try
            {
                Property prop=SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty(SemanticVocabulary.SWB_ANNOT_CLASSCODEPACKAGE).getRDFProperty();
                m_classCodePackage=m_class.getRequiredProperty(prop).getString();
            } catch (Exception pnf){
                m_classCodePackage=SWBPlatform.getSemanticMgr().getCodePackage().getPackage(getPrefix());
            }
        }
        return m_classCodePackage;
    }


    /**
     * Regresa nombre de la clase definida por la ontologia
     * Nombre de la clase sin paquete
     * @return
     */
    public String getClassCodeName()
    {
        if(m_classCodeName==null)
        {
            try
            {
                Property prop=SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty(SemanticVocabulary.SWB_ANNOT_CLASSCODENAME).getRDFProperty();
                //System.out.println("Class:"+m_class+" ->"+className);
                m_classCodeName=m_class.getRequiredProperty(prop).getString();
                //System.out.println("Class:"+m_class+" ->"+className);
                //if(m_classCodeName==null)m_classCodeName=SemanticObject.class.getName();
            } catch (Exception pnf){
                m_classCodeName=getName();
            }
            //TODO:corregir modelo para no hacer esto
            if(m_classCodeName!=null)
            {
                m_classCodeName=(""+m_classCodeName.charAt(0)).toUpperCase()+m_classCodeName.substring(1);
            }
            //log.trace("getClassCodeName:"+m_classCodeName);
        }
        return m_classCodeName;
    }

    public boolean isAutogenId()
    {
        if(m_autogenId==null)
        {
            Property prop=SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty(SemanticVocabulary.SWB_ANNOT_AUTOGENID).getRDFProperty();
            //System.out.println("Class:"+m_class+" ->"+className);
            try
            {
                m_autogenId=m_class.getRequiredProperty(prop).getBoolean();
            }catch(PropertyNotFoundException noe)
            {
                m_autogenId=false;
            }
        }
        //log.trace("isAutogenId:"+m_autogenId);
        return m_autogenId;
    }

//    /**
//     * si el objeto relacionado soporta drag and drop
//     * @return
//     */
//    public boolean isdragSupport()
//    {
//        if(isdragSupport==null)
//        {
//            isdragSupport=false;
//            Statement st=m_class.getProperty(SWBPlatform.getSemanticMgr().getSchema().getRDFOntModel().getProperty(SemanticVocabulary.SWB_PROP_DRAGSUPPORT));
//            if(st!=null)
//            {
//                isdragSupport=st.getBoolean();
//            }
//        }
//        return isdragSupport;
//    }

    public String getClassId()
    {
        return getPrefix()+":"+getName();
    }

    /** Usado para generar el identificador (uri) de las instancias de la clase,
     * asi como de sus subclases, tambien se usa para identificar al objeto en el URL (webpage:home)
     * @return
     */
    public String getClassGroupId()
    {
        if(!m_isClassGroupIdCheck)
        {
            SemanticProperty prop=SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty(SemanticVocabulary.SWB_PROP_CLASSGROUPID);

            SemanticClass cls=this;
            while(cls!=null)
            {
                SemanticLiteral lit=cls.getRequiredProperty(prop);
                if(lit!=null)m_classGroupId=lit.getString();

                if(m_classGroupId!=null)break;
                Iterator<SemanticClass> it=cls.listSuperClasses(true);
                cls=null;
                while(it.hasNext())
                {
                    SemanticClass aux=it.next();
                    if(aux.isSWBClass())cls=aux;
                }
            }

            m_isClassGroupIdCheck=true;
            if(m_classGroupId==null)
            {
                m_classGroupId=getPrefix()+"_"+getName();
            }
        }
        return m_classGroupId;
    }

    public SemanticLiteral getRequiredProperty(SemanticProperty prop)
    {
        SemanticLiteral ret=null;
        Property iprop=prop.getRDFProperty();
        try
        {
            ret=new SemanticLiteral(m_class.getRequiredProperty(iprop).getLiteral());
        }catch(PropertyNotFoundException noe){}
        return ret;
    }

    public Iterator<SemanticLiteral> listRequiredProperties(SemanticProperty prop)
    {
        ArrayList<SemanticLiteral> literals=new ArrayList<SemanticLiteral>();
        Property iprop=prop.getRDFProperty();
        try
        {
             StmtIterator it=m_class.listProperties(iprop);
             while(it.hasNext())
             {
                 Statement statement=it.nextStatement();
                 literals.add(new SemanticLiteral(statement));
             }
        }catch(PropertyNotFoundException noe){}
        return literals.iterator();
    }

    public Iterator<SemanticObject> listObjectRequiredProperties(SemanticProperty prop)
    {
        ArrayList<SemanticObject> objects=new ArrayList<SemanticObject>();
        Property iprop=prop.getRDFProperty();
        try
        {
             StmtIterator it=m_class.listProperties(iprop);
             while(it.hasNext())
             {
                 Statement statement=it.nextStatement();
                 Resource res=statement.getResource();
                 SemanticObject object=SemanticObject.createSemanticObject(res);
                 objects.add(object);
             }
        }catch(PropertyNotFoundException noe){}
        return objects.iterator();
    }





    /**
     * Lista las clases relacionadas a esta clase del tipo modelo con la propiedad hasClass
     * Solo si isSWBModel = true
     * @return clases relacionadas a esta clase del tipo modelo con la propiedad hasClass
     */
    public Iterator<SemanticClass> listModelClasses()
    {
        Iterator ret=(new Vector()).iterator();
        if(isSWBModel()==true)
        {
            Property prop=SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty(SemanticVocabulary.SWB_PROP_HASCLASS).getRDFProperty();
            ret=new SemanticClassIterator(m_class.listProperties(prop));
        }
        return ret;
    }

    /**
     * Lista los nodos a mostrar en el arbol de SWB
     */
    public Iterator<SemanticObject> listHerarquicalNodes()
    {
        Iterator ret=(new Vector()).iterator();
        if(isSWBModel()==true)
        {
            Property prop=SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty(SemanticVocabulary.SWB_PROP_HASHERARQUICALNODE).getRDFProperty();
            ret=new SemanticIterator(m_class.listProperties(prop));
        }
        return ret;
    }


    public Iterator<SemanticClass> listOwnerModels()
    {
        ArrayList ret=new ArrayList();
        if(isSWBModel()==false)
        {
            Property prop=SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty(SemanticVocabulary.SWB_PROP_HASCLASS).getRDFProperty();
            StmtIterator it=m_class.getModel().listStatements(null, prop, m_class);
            //System.out.println("listOwnerModels:"+prop+"-"+m_class);
            while(it.hasNext())
            {
                Statement stmt=it.nextStatement();
                ret.add(SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass(stmt.getSubject().getURI()));
            }
        }
        return ret.iterator();
    }


    public Constructor getConstructor()
    {
        if(m_constructor==null)
        {
            try
            {
                //m_constructor=getObjectClass().getDeclaredConstructor(Resource.class);
                m_constructor=getObjectClass().getDeclaredConstructor(SemanticObject.class);
            }
            catch(NoSuchMethodException nsme)
            {
               throw new IllegalArgumentException(nsme); //MAPS74 faltaba el throw
            }
        }
        return m_constructor;

    }

    public SemanticObject newInstance(String uri)
    {
        return SemanticObject.createSemanticObject(uri);
    }

    public SemanticObject newInstance(Resource res)
    {
        return SemanticObject.createSemanticObject(res);
    }

    public GenericObject newGenericInstance(Resource res)
    {
        return SemanticObject.createSemanticObject(res).createGenericInstance();
    }

    public GenericObject newGenericInstance(SemanticObject obj)
    {
        return obj.createGenericInstance();
    }

    /**
     * Crea una nueva instancia del Objeto Generico (no cache)
     * @param obj
     * @return
     */
    GenericObject construcGenericInstance(SemanticObject obj)
    {
        try
        {
            return (GenericObject)getConstructor().newInstance(obj);
        }
        catch(Exception ie)
        {
            throw new AssertionError(ie.getMessage());
        }
    }


    public Class getObjectClass()
    {
        if(m_cls==null)
        {
            try
            {
                m_cls=Class.forName(getClassName());
                //System.out.println("createClass:"+getClassName()+" "+m_cls);
            }catch(Exception e){log.error(e);}
        }
        return m_cls;
    }

    public String getURI()
    {
        return m_class.getURI();
    }

    /**
     * Regresa URI codificado para utilizar en ligas de html
     * @return URI Codificado
     */
    public String getEncodedURI()
    {
        return URLEncoder.encode(getURI());
    }

    public String getLabel(String lang)
    {
        return m_class.getLabel(lang);
    }

    public String getDisplayName(String lang)
    {
        String ret=null;
        SemanticObject obj=getDisplayObject();
        if(obj!=null)
        {
            if(lang!=null)
            {
                ret=obj.getProperty(SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty(SemanticVocabulary.RDFS_LABEL),null,lang);
            }
            if(ret==null)ret=obj.getProperty(obj.getModel().getSemanticProperty(SemanticVocabulary.RDFS_LABEL));
        }
        if(ret==null && lang!=null)ret=getLabel(lang);
        if(ret==null)ret=getLabel(null);
        if(ret==null)ret=getName();
        return ret;
    }
    
    public SemanticObject getDisplayObject()
    {
        if(!dispObject)
        {
            Statement st=m_class.getProperty(SWBPlatform.getSemanticMgr().getSchema().getRDFOntModel().getProperty(SemanticVocabulary.SWB_PROP_DISPLAYOBJECT));
            if(st!=null)
            {
                displayObject=SemanticObject.createSemanticObject(st.getResource());
                dispObject=true;
            }
        }
        return displayObject;
    }


//    public boolean isSuperClass(SemanticClass cls)
//    {
//        return m_class.hasSuperClass(cls.getOntClass(),false);
//    }
//
//    public boolean isSubClass(SemanticClass cls)
//    {
//        return m_class.hasSubClass(cls.getOntClass(),false);
//    }

    public Iterator<SemanticObject> listInstances()
    {
        return listInstances(false);
    }

    public Iterator<SemanticObject> listInstances(boolean direct)
    {
        //return SWBPlatform.getSemanticMgr().getOntology().listInstancesOfClass(this);
        if(m_ontclass!=null)
        {
            return new SemanticObjectIterator(m_ontclass.listInstances(direct));
        }else
        {
            return new SemanticObjectIterator(m_class.listInstances(direct));
        }
    }

    public Iterator<SemanticObject> listSchemaInstances()
    {
        return listSchemaInstances(false);
    }

    public Iterator<SemanticObject> listSchemaInstances(boolean direct)
    {
        return new SemanticObjectIterator(m_class.listInstances(direct));
    }

    public Iterator listGenericInstances()
    {
        return listGenericInstances(false);
    }

    public Iterator listGenericInstances(boolean direct)
    {
        return new GenericIterator(listInstances(direct));
    }

    public Iterator listSchemaGenericInstances()
    {
        return listSchemaGenericInstances(false);
    }

    public Iterator listSchemaGenericInstances(boolean direct)
    {
        return new GenericIterator(listSchemaInstances(direct));
    }

    public SemanticProperty getProperty(String name)
    {
        return m_props.get(name);
    }

    public boolean hasProperty(String name)
    {
        return m_props.containsKey(name);
    }

    public Iterator<SemanticProperty> listProperties()
    {
        return m_props.values().iterator();
    }

    public OntClass getOntClass()
    {
        return m_class;
    }

    public boolean isSuperClass(SemanticClass cls)
    {
        return isSuperClass(cls, false);
    }

    public boolean isSuperClass(SemanticClass cls, boolean direct)
    {
        return cls.isSubClass(this, direct);
    }

    public boolean isSubClass(SemanticClass cls)
    {
        return isSubClass(cls, false);
    }

    public boolean isSubClass(SemanticClass cls, boolean direct)
    {
        boolean ret=false;
        Iterator it=m_class.listSuperClasses(direct);
        while(it.hasNext())
        {
            OntClass cl=(OntClass)it.next();
            if(cl.equals(cls.getOntClass()))
            {
                ret=true;
                break;
            }
        }
        return ret;
    }

    public Iterator<SemanticClass> listSubClasses()
    {
        return listSubClasses(false);
    }

    public Iterator<SemanticClass> listSubClasses(boolean direct)
    {
        return new SemanticClassIterator(m_class.listSubClasses(direct));
    }

    public Iterator<SemanticClass> listSuperClasses()
    {
        return listSuperClasses(false);
    }

    public Iterator<SemanticClass> listSuperClasses(boolean direct)
    {
        return new SemanticClassIterator(m_class.listSuperClasses(direct));
    }

    @Override
    public String toString()
    {
        return m_class.toString();
    }

    @Override
    public int hashCode()
    {
        return m_class.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        boolean ret=false;
        if(obj!=null)
        {
            ret=(hashCode()==obj.hashCode());
        }
        return ret;
    }

    private void checkType()
    {
        m_isSWBClass=false;
        m_isSWBInterface=false;
        m_isSWBModel=false;
        m_isSWBFormElement=false;
        m_isSWBSemanticResource=false;
        for (Iterator i = m_class.listRDFTypes(false); i.hasNext(); )
        {
            Resource res=(Resource)i.next();
            String uri=res.getURI();
            if(uri.equals(SemanticVocabulary.SWB_MODEL))
            {
                m_isSWBModel = true;
                break;
            }else if(uri.equals(SemanticVocabulary.SWB_CLASS))
            {
                m_isSWBClass = true;
                break;
            }else if(uri.equals(SemanticVocabulary.SWB_INTERFACE))
            {
                m_isSWBInterface = true;
                break;
            }else if(uri.equals(SemanticVocabulary.SWB_FORMELEMENT))
            {
                m_isSWBFormElement = true;
                break;
            }else if(uri.equals(SemanticVocabulary.SWB_SEMANTICRESOURCE))
            {
                m_isSWBSemanticResource = true;
                break;
            }
        }
    }

    public boolean isSWBClass()
    {
        if(m_isSWBClass==null)
        {
            checkType();
        }
        return m_isSWBClass.booleanValue();
    }

    public boolean isSWBInterface()
    {
        if(m_isSWBInterface==null)
        {
            checkType();
        }
        return m_isSWBInterface.booleanValue();
    }

    public boolean isSWBModel()
    {
        if(m_isSWBModel==null)
        {
            checkType();
        }
        return m_isSWBModel.booleanValue();
    }

    public boolean isSWBFormElement()
    {
        if(m_isSWBFormElement==null)
        {
            checkType();
        }
        return m_isSWBFormElement.booleanValue();
    }

    public boolean isSWBSemanticResource()
    {
        if(m_isSWBSemanticResource==null)
        {
            checkType();
        }
        return m_isSWBSemanticResource.booleanValue();
    }

    public SemanticProperty getDisplayNameProperty() {
        return displayNameProperty;
    }

    public boolean hasHerarquicalProperties()
    {
        return !herarquicalProps.isEmpty();
    }

    public Iterator<SemanticProperty> listHerarquicalProperties()
    {
        return herarquicalProps.iterator();
    }

    public boolean hasInverseHerarquicalProperties()
    {
        return !inverseHerarquicalProps.isEmpty();
    }

    public Iterator<SemanticProperty> listInverseHerarquicalProperties()
    {
        return inverseHerarquicalProps.iterator();
    }

    public void addSuperClass(SemanticClass cls)
    {
        SemanticObject obj=SWBPlatform.getSemanticMgr().getSchema().getSemanticObject(getURI());
        Resource res=obj.getRDFResource();
        res.addProperty(SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty(SemanticVocabulary.RDFS_SUBCLASSOF).getRDFProperty(), cls.getOntClass());
    }

    public boolean isSWB()
    {
        return isSWBClass() || isSWBModel() || isSWBFormElement() || isSWBInterface() || isSWBSemanticResource();
    }

    public SemanticClass getRootClass()
    {
        SemanticClass ret=this;
        if(isSWBClass())
        {
            SemanticClass swbcls=SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass(SemanticVocabulary.SWB_SWBCLASS);
            if(this==swbcls || this.isSubClass(swbcls, true))
            {
                ret=this;
            }else
            {
                Iterator<SemanticClass> it=listSuperClasses();
                while(it.hasNext())
                {
                    SemanticClass cls=it.next();
                    if(cls.isSubClass(swbcls,true))
                    {
                        ret=cls;
                        break;
                    }
                }
            }
        }
        return ret;
    }
}
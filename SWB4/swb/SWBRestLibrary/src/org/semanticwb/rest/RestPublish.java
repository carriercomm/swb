/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.semanticwb.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.semanticwb.Logger;
import org.semanticwb.SWBPlatform;
import org.semanticwb.SWBUtils;
import org.semanticwb.model.GenericIterator;
import org.semanticwb.model.GenericObject;
import org.semanticwb.model.base.GenericObjectBase;
import org.semanticwb.platform.SemanticClass;
import org.semanticwb.platform.SemanticLiteral;
import org.semanticwb.platform.SemanticObject;
import org.semanticwb.platform.SemanticProperty;
import org.semanticwb.platform.SemanticVocabulary;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;
import static javax.xml.XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI;
/**
 *
 * @author victor.lorenzana
 */
public class RestPublish
{

    private static final Logger log = SWBUtils.getLogger(RestPublish.class);
    private static final String REST_MODELURI = "rest:modeluri";
    private static final String REST_CLASSURI = "rest:classuri";
    private static final String REST_ID = "rest:id";
    private static final String REST_URI = "rest:uri";
    
    
    private static final String XSD_PREFIX = "xsd";
    private static final String XLINK_NS = "http://www.w3.org/1999/xlink";
    private static final String XSD_STRING = "xsd:string";
    private final Set<SemanticClass> classes;

    public static final String REST_RESOURCES_2010 = "http://www.semanticwb.org/rest/2010";

    public static final String WADL_NS_2006 = "http://research.sun.com/wadl/2006/10";
    private static final String WADL_XSD_LOCATION_2006 = "https://wadl.dev.java.net/wadl20061109.xsd";
    public static final String WADL_NS_2009 = "http://wadl.dev.java.net/2009/02";
    private static final String WADL_XSD_LOCATION_2009 = "http://www.w3.org/Submission/wadl/wadl.xsd";

    public RestPublish(Set<SemanticClass> classes)
    {
        this.classes = classes;
    }


    private Document getErrorXSD()
    {
        Document doc = SWBUtils.XML.getNewDocument();
        Element schema = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "schema");
        schema.setAttribute("targetNamespace", REST_RESOURCES_2010);
        Attr attr = doc.createAttribute("xmlns");
        attr.setValue(REST_RESOURCES_2010);
        schema.setAttributeNode(attr);

        doc.appendChild(schema);
        schema.setPrefix(XSD_PREFIX);
        Element element = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "element");
        schema.appendChild(element);
        element.setAttribute("name", "Error");
        element.setPrefix(XSD_PREFIX);
        Element complexType = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "complexType");
        complexType.setPrefix(XSD_PREFIX);
        element.appendChild(complexType);

        Element sequence = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "sequence");
        sequence.setPrefix(XSD_PREFIX);
        complexType.appendChild(sequence);


        Element message = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "element");
        message.setPrefix(XSD_PREFIX);
        message.setAttribute("name", "Message");
        message.setAttribute("type", XSD_STRING);
        sequence.appendChild(message);
        message.setAttribute("minOccurs", "1");
        message.setAttribute("maxOccurs", "unbounded");
        return doc;
    }

    private Document getCreatedXSD()
    {
        Document doc = SWBUtils.XML.getNewDocument();
        Element schema = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "schema");
        schema.setAttribute("targetNamespace", REST_RESOURCES_2010);

        Attr attr = doc.createAttribute("xmlns");
        attr.setValue(REST_RESOURCES_2010);
        schema.setAttributeNode(attr);

        doc.appendChild(schema);
        schema.setPrefix(XSD_PREFIX);
        Element element = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "element");
        schema.appendChild(element);
        element.setAttribute("name", "Created");
        element.setPrefix(XSD_PREFIX);



        Element attribute = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "attribute");
        element.appendChild(attribute);
        attribute.setAttribute("name", "uri");
        attribute.setAttribute("type", XSD_STRING);
        attribute.setPrefix(XSD_PREFIX);

        return doc;
    }
    private Document getUpdatedXSD()
    {
        Document doc = SWBUtils.XML.getNewDocument();
        Element schema = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "schema");
        schema.setAttribute("targetNamespace", REST_RESOURCES_2010);
        Attr attr = doc.createAttribute("xmlns");
        attr.setValue(REST_RESOURCES_2010);
        schema.setAttributeNode(attr);

        doc.appendChild(schema);
        schema.setPrefix(XSD_PREFIX);
        Element element = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "element");
        schema.appendChild(element);
        element.setAttribute("name", "Updated");
        element.setPrefix(XSD_PREFIX);
        Element attribute = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "attribute");
        element.appendChild(attribute);
        attribute.setAttribute("name", "uri");
        attribute.setAttribute("type", XSD_STRING);
        attribute.setPrefix(XSD_PREFIX);
        return doc;
    }

    private Document getDeletedXSD()
    {
        Document doc = SWBUtils.XML.getNewDocument();
        Element schema = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "schema");
        schema.setAttribute("targetNamespace", REST_RESOURCES_2010);
        Attr attr = doc.createAttribute("xmlns");
        attr.setValue(REST_RESOURCES_2010);
        schema.setAttributeNode(attr);

        doc.appendChild(schema);
        schema.setPrefix(XSD_PREFIX);
        Element element = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "element");
        schema.appendChild(element);
        element.setAttribute("name", "Deleted");
        element.setPrefix(XSD_PREFIX);
        Element attribute = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "attribute");
        element.appendChild(attribute);
        attribute.setAttribute("name", "uri");
        attribute.setAttribute("type", XSD_STRING);
        attribute.setPrefix(XSD_PREFIX);

        return doc;
    }

    

    private Document getXSD(final SemanticClass clazz, final String version)
    {
        Document doc = SWBUtils.XML.getNewDocument();
        Element schema = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "schema");
        schema.setAttribute("targetNamespace", clazz.getURI());
        Attr attr = doc.createAttribute("xmlns");        
        attr.setValue(clazz.getURI());
        schema.setAttributeNode(attr);

        schema.setAttribute("xmlns:" + clazz.getPrefix(), clazz.getURI());
        schema.setAttribute("xmlns:xlink", XLINK_NS);
        schema.setAttribute("xmlns:swbrest", REST_RESOURCES_2010);
        doc.appendChild(schema);
        schema.setPrefix(XSD_PREFIX);
        addXSD(schema, clazz);

        return doc;
    }

    private void addClassManagerResultToXSD(final Element schema, final SemanticClass clazz, HashSet<SemanticClass> ranges)
    {
        Document doc = schema.getOwnerDocument();
        try
        {
            Class main = Class.forName(clazz.getClassName());
            Class msgr = getClassManager(main);
            for (Method m : msgr.getDeclaredMethods())
            {
                if (Modifier.isPublic(m.getModifiers()) && Modifier.isStatic(m.getModifiers()) && (m.getName().startsWith("has") || m.getName().startsWith("list")))
                {
                    Class returnType = m.getReturnType();

                    Element methodElement = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "element");
                    methodElement.setPrefix(XSD_PREFIX);
                    methodElement.setAttribute("name", "swbrest:"+m.getName());
                    schema.appendChild(methodElement);
                    if (returnType instanceof Class && isGenericObject(returnType)) // is GenericObject
                    {
                        // gets SemanticClass;
                        Field sclass = ((Class) returnType).getField("sclass");
                        Object objClass = sclass.get(null);
                        if (objClass instanceof SemanticClass)
                        {
                            SemanticClass returnSemanticClazz = (SemanticClass) objClass;
                            addProperties(returnSemanticClazz, methodElement, ranges);
                        }
                    }
                    else if (returnType.equals(Iterator.class)) // GenericIterator
                    {
                        if (m.getGenericReturnType() instanceof ParameterizedType)
                        {
                            if (((ParameterizedType) m.getGenericReturnType()).getActualTypeArguments() != null && ((ParameterizedType) m.getGenericReturnType()).getActualTypeArguments().length > 0)
                            {
                                Type actual = ((ParameterizedType) m.getGenericReturnType()).getActualTypeArguments()[0];
                                if (actual instanceof Class)
                                {
                                    Class _returnType = (Class) actual;
                                    Field sclass = ((Class) _returnType).getField("sclass");
                                    Object objClass = sclass.get(null);
                                    if (objClass instanceof SemanticClass)
                                    {
                                        SemanticClass returnSemanticClazz = (SemanticClass) objClass;
                                        Element complex = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "complexType");
                                        complex.setPrefix(XSD_PREFIX);
                                        methodElement.appendChild(complex);
                                        Element sequence = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "sequence");
                                        sequence.setPrefix(XSD_PREFIX);
                                        complex.appendChild(sequence);
                                        Element child = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "element");
                                        child.setPrefix(XSD_PREFIX);
                                        child.setAttribute("name", "swbrest:"+returnSemanticClazz.getName());

                                        Element shortURI = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "attribute");
                                        shortURI.setPrefix(XSD_PREFIX);
                                        shortURI.setAttribute("name", "shortURI");
                                        child.appendChild(shortURI);

                                        Element uri = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "attribute");
                                        uri.setAttribute("name", "uri");
                                        child.appendChild(uri);

                                        Element href = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "attribute");
                                        href.setAttribute("name", "xlink:href");
                                        child.appendChild(href);


                                        sequence.appendChild(child);
                                    }
                                }
                            }

                        }

                    }
                    else
                    {
                        if (returnType instanceof Class)
                        {
                            methodElement.setAttribute("type", classToxsd((Class) returnType));
                        }
                    }
                }
            }
        }
        catch (Exception cnfe)
        {
            cnfe.printStackTrace();
            log.error(cnfe);
        }

    }

    private void addProperties(SemanticClass clazz, Element element, HashSet<SemanticClass> ranges)
    {
        Document doc = element.getOwnerDocument();
        Element complexType = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "complexType");
        complexType.setPrefix(XSD_PREFIX);
        element.appendChild(complexType);

        Element sequence = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "sequence");
        sequence.setPrefix(XSD_PREFIX);
        complexType.appendChild(sequence);

        Iterator<SemanticProperty> props = clazz.listProperties();
        while (props.hasNext())
        {
            SemanticProperty prop = props.next();
            if (!prop.hasInverse())
            {
                if (prop.isDataTypeProperty())
                {
                    Element property = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "element");
                    property.setPrefix(XSD_PREFIX);
                    property.setAttribute("name", prop.getName());
                    sequence.appendChild(property);
                    String type = XSD_STRING;
                    if (prop.isBoolean())
                    {
                        type = SemanticVocabulary.XMLS_BOOLEAN;
                    }
                    else if (prop.isBinary())
                    {
                        type = SemanticVocabulary.XMLS_BASE64BINARY;
                    }
                    else if (prop.isByte())
                    {
                        type = SemanticVocabulary.XMLS_BYTE;
                    }
                    else if (prop.isDate())
                    {
                        type = SemanticVocabulary.XMLS_DATE;
                    }
                    else if (prop.isDateTime())
                    {
                        type = SemanticVocabulary.XMLS_DATETIME;
                    }
                    else if (prop.isDouble())
                    {
                        type = SemanticVocabulary.XMLS_DOUBLE;
                    }
                    else if (prop.isFloat())
                    {
                        type = SemanticVocabulary.XMLS_FLOAT;
                    }
                    else if (prop.isInt())
                    {
                        type = SemanticVocabulary.XMLS_INT;
                    }
                    else if (prop.isLong())
                    {
                        type = SemanticVocabulary.XMLS_LONG;
                    }
                    else if (prop.isShort())
                    {
                        type = SemanticVocabulary.XMLS_SHORT;
                    }
                    type = type.replace(W3C_XML_SCHEMA_NS_URI + "#", "xsd:");
                    property.setAttribute("type", type);
                    if (prop.getName().startsWith("has"))
                    {
                        property.setAttribute("minOccurs", "0");
                        property.setAttribute("maxOccurs", "unbounded");
                    }

                }
                else
                {
                    SemanticClass range = prop.getRangeClass();
                    if (range != null)
                    {
                        ranges.add(range);
                        Element property = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "element");
                        sequence.appendChild(property);
                        property.setAttribute("name", prop.getName());
                        if (prop.getName().startsWith("has"))
                        {
                            property.setAttribute("minOccurs", "0");
                            property.setAttribute("maxOccurs", "unbounded");
                        }
                        property.setPrefix(XSD_PREFIX);
                        //property.setAttribute("type", range.getName());
                        Element attribute = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "attribute");
                        attribute.setPrefix(XSD_PREFIX);
                        property.appendChild(attribute);
                        attribute.setAttribute("name", "xlink:href");
                        //attribute.setAttribute("type","xsd:string");
                        //addXSD(schema,range);
                    }
                }
            }
        }
    }

    private void addXSD(final Element schema, final SemanticClass clazz)
    {
        Document doc = schema.getOwnerDocument();
        boolean exists = false;
        NodeList childs = schema.getChildNodes();
        for (int i = 0; i < childs.getLength(); i++)
        {
            Node node = childs.item(i);
            if (node instanceof Element)
            {
                Element element = (Element) node;
                String name = element.getAttribute("name");
                if (name.equals(clazz.getName()))
                {
                    exists = true;
                    break;
                }
            }
        }

        if (!exists && clazz.isSWBClass())
        {
            HashSet<SemanticClass> ranges = new HashSet<SemanticClass>();
            // addClassMenegerResults
            addClassManagerResultToXSD(schema, clazz, ranges);
            Element element = doc.createElementNS(W3C_XML_SCHEMA_NS_URI, "element");
            schema.appendChild(element);
            element.setAttribute("name", clazz.getName());
            element.setPrefix(XSD_PREFIX);
            addProperties(clazz, element, ranges);
        }
    }

    private void addPUTMethod(Document doc, Element resource, SemanticClass clazz, String WADL_NS)
    {
        Element method = doc.createElementNS(WADL_NS, "method");
        method.setAttribute("name", "PUT");
        method.setAttribute("id", "add" + clazz.getName());
        resource.appendChild(method);

        Element request = doc.createElementNS(WADL_NS, "request");
        method.appendChild(request);




        Iterator<SemanticProperty> props = clazz.listProperties();
        while (props.hasNext())
        {
            SemanticProperty prop = props.next();
            if (!prop.hasInverse())
            {
                if (prop.isDataTypeProperty())
                {
                    String type = XSD_STRING;
                    if (prop.isBoolean())
                    {
                        type = SemanticVocabulary.XMLS_BOOLEAN;
                    }
                    else if (prop.isBinary())
                    {
                        type = SemanticVocabulary.XMLS_BASE64BINARY;
                    }
                    else if (prop.isByte())
                    {
                        type = SemanticVocabulary.XMLS_BYTE;
                    }
                    else if (prop.isDate())
                    {
                        type = SemanticVocabulary.XMLS_DATE;
                    }
                    else if (prop.isDateTime())
                    {
                        type = SemanticVocabulary.XMLS_DATETIME;
                    }
                    else if (prop.isDouble())
                    {
                        type = SemanticVocabulary.XMLS_DOUBLE;
                    }
                    else if (prop.isFloat())
                    {
                        type = SemanticVocabulary.XMLS_FLOAT;
                    }
                    else if (prop.isInt())
                    {
                        type = SemanticVocabulary.XMLS_INT;
                    }
                    else if (prop.isLong())
                    {
                        type = SemanticVocabulary.XMLS_LONG;
                    }
                    else if (prop.isShort())
                    {
                        type = SemanticVocabulary.XMLS_SHORT;
                    }
                    type = type.replace(W3C_XML_SCHEMA_NS_URI + "#", "xsd:");
                    Element param = doc.createElementNS(WADL_NS, "param");
                    param.setAttribute("name", prop.getName());
                    param.setAttribute("style", "query");
                    param.setAttribute("type", type);
                    if (prop.getName().startsWith("has"))
                    {
                        param.setAttribute("repeating", "true");
                    }
                    request.appendChild(param);
                }
                else
                {
                    Element param = doc.createElementNS(WADL_NS, "param");
                    param.setAttribute("name", prop.getName());
                    param.setAttribute("style", "query");
                    param.setAttribute("type", "xsd:anyURI");
                    if (prop.getName().startsWith("has"))
                    {
                        param.setAttribute("repeating", "true");
                    }
                    request.appendChild(param);
                }
            }

        }
        Element param = doc.createElementNS(WADL_NS, "param");
        param.setAttribute("name", REST_CLASSURI);
        param.setAttribute("style", "query");
        param.setAttribute("type", XSD_STRING);
        param.setAttribute("required", "true");
        request.appendChild(param);

        param = doc.createElementNS(WADL_NS, "param");
        param.setAttribute("name", REST_MODELURI);
        param.setAttribute("style", "query");
        param.setAttribute("type", XSD_STRING);
        param.setAttribute("required", "true");
        request.appendChild(param);

        if (!clazz.isAutogenId())
        {
            param = doc.createElementNS(WADL_NS, "param");
            param.setAttribute("name", REST_ID);
            param.setAttribute("style", "query");
            param.setAttribute("type", XSD_STRING);
            param.setAttribute("required", "true");
            request.appendChild(param);
        }


        if (WADL_NS.equals(WADL_NS_2009))
        {
            Element error = doc.createElementNS(WADL_NS, "response");
            method.appendChild(error);
            Element representation = doc.createElementNS(WADL_NS, "representation");
            error.appendChild(representation);
            representation.setAttribute("mediaType", "application/xml");
            error.setAttribute("status", "400");
            representation.setAttribute("element", "swbrest:Error");
        }

        Element response = doc.createElementNS(WADL_NS, "response");
        method.appendChild(response);
        Element representation = doc.createElementNS(WADL_NS, "representation");
        response.appendChild(representation);
        representation.setAttribute("mediaType", "application/xml");
        if (WADL_NS.equals(WADL_NS_2009))
        {
            response.setAttribute("status", "200");
            representation.setAttribute("element", "swbrest:Created");
        }
    }

    private void addPOSTMethod(Document doc, Element resource, SemanticClass clazz, String WADL_NS)
    {
        Element method = doc.createElementNS(WADL_NS, "method");
        method.setAttribute("name", "POST");
        method.setAttribute("id", "update" + clazz.getName());
        resource.appendChild(method);

        Element request = doc.createElementNS(WADL_NS, "request");
        method.appendChild(request);



        Element param = doc.createElementNS(WADL_NS, "param");
        param.setAttribute("name", REST_URI);
        param.setAttribute("style", "query");
        param.setAttribute("type", "xsd:anyURI");

        request.appendChild(param);

        Iterator<SemanticProperty> props = clazz.listProperties();
        while (props.hasNext())
        {
            SemanticProperty prop = props.next();
            if (!prop.hasInverse())
            {
                if (prop.isDataTypeProperty())
                {
                    String type = XSD_STRING;
                    if (prop.isBoolean())
                    {
                        type = SemanticVocabulary.XMLS_BOOLEAN;
                    }
                    else if (prop.isBinary())
                    {
                        type = SemanticVocabulary.XMLS_BASE64BINARY;
                    }
                    else if (prop.isByte())
                    {
                        type = SemanticVocabulary.XMLS_BYTE;
                    }
                    else if (prop.isDate())
                    {
                        type = SemanticVocabulary.XMLS_DATE;
                    }
                    else if (prop.isDateTime())
                    {
                        type = SemanticVocabulary.XMLS_DATETIME;
                    }
                    else if (prop.isDouble())
                    {
                        type = SemanticVocabulary.XMLS_DOUBLE;
                    }
                    else if (prop.isFloat())
                    {
                        type = SemanticVocabulary.XMLS_FLOAT;
                    }
                    else if (prop.isInt())
                    {
                        type = SemanticVocabulary.XMLS_INT;
                    }
                    else if (prop.isLong())
                    {
                        type = SemanticVocabulary.XMLS_LONG;
                    }
                    else if (prop.isShort())
                    {
                        type = SemanticVocabulary.XMLS_SHORT;
                    }
                    type = type.replace(W3C_XML_SCHEMA_NS_URI + "#", "xsd:");
                    param = doc.createElementNS(WADL_NS, "param");
                    param.setAttribute("name", prop.getName());
                    param.setAttribute("style", "query");
                    param.setAttribute("type", type);
                    if (prop.getName().startsWith("has"))
                    {
                        param.setAttribute("repeating", "true");
                    }
                    request.appendChild(param);
                }
                else
                {
                    param = doc.createElementNS(WADL_NS, "param");
                    param.setAttribute("name", prop.getName());
                    param.setAttribute("style", "query");
                    param.setAttribute("type", "xsd:anyURI");
                    if (prop.getName().startsWith("has"))
                    {
                        param.setAttribute("repeating", "true");
                    }
                    request.appendChild(param);
                }
            }

        }


        if (WADL_NS.equals(WADL_NS_2009))
        {
            Element error = doc.createElementNS(WADL_NS, "response");
            method.appendChild(error);
            Element representation = doc.createElementNS(WADL_NS, "representation");
            error.appendChild(representation);
            representation.setAttribute("mediaType", "application/xml");
            error.setAttribute("status", "400");
            representation.setAttribute("element", "swbrest:Error");
        }

        Element response = doc.createElementNS(WADL_NS, "response");
        method.appendChild(response);
        Element representation = doc.createElementNS(WADL_NS, "representation");
        response.appendChild(representation);
        representation.setAttribute("mediaType", "application/xml");
        if (WADL_NS.equals(WADL_NS_2009))
        {
            response.setAttribute("status", "200");
            representation.setAttribute("element", "swbrest:Updated");
        }
    }

    private void createMethod(Document doc, Element resource, SemanticClass clazz, Method method, String WADL_NS,String id)
    {
        Element emethod = doc.createElementNS(WADL_NS, "method");
        emethod.setAttribute("name", "GET");
        emethod.setAttribute("id", id);
        resource.appendChild(emethod);

        Element request = doc.createElementNS(WADL_NS, "request");
        emethod.appendChild(request);

        Element param = doc.createElementNS(WADL_NS, "param");
        param.setAttribute("name", "method");
        param.setAttribute("style", "query");
        param.setAttribute("fixed", method.getName());
        param.setAttribute("type", XSD_STRING);
        param.setAttribute("required", "true");
        request.appendChild(param);

        param = doc.createElementNS(WADL_NS, "param");
        param.setAttribute("name", "classuri");
        param.setAttribute("style", "query");
        param.setAttribute("fixed", clazz.getURI());
        param.setAttribute("type", XSD_STRING);
        param.setAttribute("required", "true");
        request.appendChild(param);

        for (Class classparam : method.getParameterTypes())
        {
            if (isGenericObject(classparam))
            {
                param = doc.createElementNS(WADL_NS, "param");
                param.setAttribute("name", classparam.getName().toLowerCase());
                param.setAttribute("style", "query");
                param.setAttribute("type", "xsd:anyURI");
                param.setAttribute("required", "true");
                request.appendChild(param);
            }
            else
            {
                param = doc.createElementNS(WADL_NS, "param");
                param.setAttribute("name", classparam.getName().toLowerCase());
                param.setAttribute("style", "query");
                param.setAttribute("type", classToxsd(classparam));
                param.setAttribute("required", "true");
                request.appendChild(param);
            }
        }


        if (WADL_NS.equals(WADL_NS_2009))
        {
            Element error = doc.createElementNS(WADL_NS, "response");
            emethod.appendChild(error);
            Element representation = doc.createElementNS(WADL_NS, "representation");
            error.appendChild(representation);
            representation.setAttribute("mediaType", "application/xml");
            error.setAttribute("status", "400");
            representation.setAttribute("element", "swbrest:Error");
        }

        Element response = doc.createElementNS(WADL_NS, "response");
        emethod.appendChild(response);
        Element representation = doc.createElementNS(WADL_NS, "representation");
        response.appendChild(representation);
        representation.setAttribute("mediaType", "application/xml");
        if (WADL_NS.equals(WADL_NS_2009))
        {
            response.setAttribute("status", "200");
            representation.setAttribute("element", clazz.getPrefix() + ":" + clazz.getName());
        }
    }

    private static boolean isGenericObject(Class clazz)
    {
        try
        {
            clazz.asSubclass(GenericObject.class);
            return true;
        }
        catch (ClassCastException e)
        {
            //e.printStackTrace();
            return false;
        }
        catch (Exception e)
        {
            log.error(e);
        }
        return false;
    }

    public static String classToxsd(Class clazz)
    {
        String getXSDType = XSD_STRING;
        if (clazz.equals(Boolean.class))
        {
            getXSDType = "xsd:boolean";
        }
        else if (clazz.equals(Byte.class))
        {
            getXSDType = "xsd:byte";
        }
        else if (clazz.equals(Date.class))
        {
            getXSDType = "xsd:date";
        }
        else if (clazz.equals(Timestamp.class))
        {
            getXSDType = "xsd:datetime";
        }
        else if (clazz.equals(Double.class))
        {
            getXSDType = "xsd:double";
        }
        else if (clazz.equals(Float.class))
        {
            getXSDType = "xsd:float";
        }
        else if (clazz.equals(Float.class))
        {
            getXSDType = "xsd:float";
        }
        else if (clazz.equals(Float.class))
        {
            getXSDType = "xsd:float";
        }
        else if (clazz.equals(Integer.class))
        {
            getXSDType = "xsd:int";
        }
        else if (clazz.equals(Long.class))
        {
            getXSDType = "xsd:long";
        }
        else if (clazz.equals(Short.class))
        {
            getXSDType = "xsd:short";
        }
        return getXSDType;

    }

    public static Class xsdToClass(String xsdType)
    {
        xsdType = xsdType.replace("xsd:", SemanticVocabulary.XMLS_URI);
        if (xsdType.equals(SemanticVocabulary.XMLS_BOOLEAN))
        {
            return Boolean.class;
        }
        if (xsdType.equals(SemanticVocabulary.XMLS_DATE))
        {
            return Date.class;
        }
        if (xsdType.equals(SemanticVocabulary.XMLS_DATETIME))
        {
            return Timestamp.class;
        }
        if (xsdType.equals(SemanticVocabulary.XMLS_BYTE))
        {
            return Byte.class;
        }
        if (xsdType.equals(SemanticVocabulary.XMLS_DOUBLE))
        {
            return Double.class;
        }
        if (xsdType.equals(SemanticVocabulary.XMLS_FLOAT))
        {
            return Float.class;
        }
        if (xsdType.equals(SemanticVocabulary.XMLS_INT) || xsdType.equals(SemanticVocabulary.XMLS_INTEGER))
        {
            return Integer.class;
        }
        if (xsdType.equals(SemanticVocabulary.XMLS_LONG))
        {
            return Long.class;
        }
        if (xsdType.equals(SemanticVocabulary.XMLS_SHORT))
        {
            return Short.class;
        }

        return String.class;


    }

    private void addDELETEMethod(Document doc, Element resource, SemanticClass clazz, String WADL_NS)
    {
        Element method = doc.createElementNS(WADL_NS, "method");
        method.setAttribute("name", "DELETE");
        method.setAttribute("id", "delete" + clazz.getName());
        resource.appendChild(method);

        Element request = doc.createElementNS(WADL_NS, "request");
        method.appendChild(request);

        Element param = doc.createElementNS(WADL_NS, "param");
        param.setAttribute("name", "uri");
        param.setAttribute("style", "query");
        param.setAttribute("type", XSD_STRING);
        param.setAttribute("required", "true");
        request.appendChild(param);


        if (WADL_NS.equals(WADL_NS_2009))
        {
            Element error = doc.createElementNS(WADL_NS, "response");
            method.appendChild(error);
            Element representation = doc.createElementNS(WADL_NS, "representation");
            error.appendChild(representation);
            representation.setAttribute("mediaType", "application/xml");
            error.setAttribute("status", "400");
            representation.setAttribute("element", "swbrest:Error");
        }

        Element response = doc.createElementNS(WADL_NS, "response");
        method.appendChild(response);
        Element representation = doc.createElementNS(WADL_NS, "representation");
        response.appendChild(representation);
        representation.setAttribute("mediaType", "application/xml");
        if (WADL_NS.equals(WADL_NS_2009))
        {
            response.setAttribute("status", "200");
            representation.setAttribute("element", "swbrest:Deleted");
        }
    }

    private void addGetMethod(Document doc, Element resource, SemanticClass clazz, String WADL_NS)
    {
        Element method = doc.createElementNS(WADL_NS, "method");
        method.setAttribute("name", "GET");
        method.setAttribute("id", "get" + clazz.getName());
        resource.appendChild(method);

        Element request = doc.createElementNS(WADL_NS, "request");
        method.appendChild(request);

        Element param = doc.createElementNS(WADL_NS, "param");
        param.setAttribute("name", "uri");
        param.setAttribute("style", "query");
        param.setAttribute("type", XSD_STRING);
        param.setAttribute("required", "true");
        request.appendChild(param);



        if (WADL_NS.equals(WADL_NS_2009))
        {
            Element error = doc.createElementNS(WADL_NS, "response");
            method.appendChild(error);
            Element representation = doc.createElementNS(WADL_NS, "representation");
            error.appendChild(representation);
            representation.setAttribute("mediaType", "application/xml");
            error.setAttribute("status", "400");
            representation.setAttribute("element", "swbrest:Error");
        }

        Element response = doc.createElementNS(WADL_NS, "response");
        method.appendChild(response);
        Element representation = doc.createElementNS(WADL_NS, "representation");
        response.appendChild(representation);
        representation.setAttribute("mediaType", "application/xml");
        if (WADL_NS.equals(WADL_NS_2009))
        {
            response.setAttribute("status", "200");
            representation.setAttribute("element", clazz.getPrefix() + ":" + clazz.getName());
        }
    }

    private void executeMethod(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        Document doc = null;
        try
        {
            doc = getExecuteMethod(request);
        }
        catch (Exception e)
        {
            doc = getError(e.getMessage());
        }
        PrintWriter out = response.getWriter();
        String charset = Charset.defaultCharset().name();
        response.setContentType("application/xml; charset=" + charset);
        String xml = SWBUtils.XML.domToXml(doc, charset, true);
        out.print(xml);
        out.close();
    }

    private void showList(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String version = "2009";
        if ("2006".equals(request.getParameter("version")))
        {
            version = "2006";
        }
        Document doc = getWADL(request, version);
        PrintWriter out = response.getWriter();
        String charset = Charset.defaultCharset().name();
        response.setContentType("application/xml; charset=" + charset);
        String xml = SWBUtils.XML.domToXml(doc, charset, true);
        out.print(xml);
        out.close();
    }

    private void showCreted(HttpServletRequest request, HttpServletResponse response, String uri) throws IOException
    {
        Document doc = getCreated(uri);
        PrintWriter out = response.getWriter();
        String charset = Charset.defaultCharset().name();
        response.setContentType("application/xml; charset=" + charset);
        String xml = SWBUtils.XML.domToXml(doc, charset, true);
        out.print(xml);
        out.close();
    }

    private void showUpdated(HttpServletRequest request, HttpServletResponse response, String uri) throws IOException
    {
        Document doc = getUpdated(uri);
        PrintWriter out = response.getWriter();
        String charset = Charset.defaultCharset().name();
        response.setContentType("application/xml; charset=" + charset);
        String xml = SWBUtils.XML.domToXml(doc, charset, true);
        out.print(xml);
        out.close();
    }

    private void showObject(HttpServletRequest request, HttpServletResponse response, SemanticObject obj) throws IOException
    {
        Document doc = serialize(obj, request);
        PrintWriter out = response.getWriter();
        String charset = Charset.defaultCharset().name();
        response.setContentType("application/xml; charset=" + charset);
        String xml = SWBUtils.XML.domToXml(doc, charset, true);
        out.print(xml);
        out.close();
    }

    private void showXSD(HttpServletRequest request, HttpServletResponse response, SemanticClass clazz) throws IOException
    {
        String version = "2009";
        if ("2006".equals(request.getParameter("version")))
        {
            version = "2006";
        }
        Document doc = getXSD(clazz, version);
        PrintWriter out = response.getWriter();
        String charset = Charset.defaultCharset().name();
        response.setContentType("application/xml; charset=" + charset);
        String xml = SWBUtils.XML.domToXml(doc, charset, true);
        out.print(xml);
        out.close();
    }

    private void serialize(SemanticObject obj, Element name, HttpServletRequest request)
    {
        Document doc = name.getOwnerDocument();
        Iterator<SemanticProperty> props = obj.listProperties();
        while (props.hasNext())
        {
            SemanticProperty prop = props.next();
            if (!prop.hasInverse())
            {
                if (prop.isDataTypeProperty())
                {
                    if (prop.getName().startsWith("has"))
                    {
                        Iterator<SemanticLiteral> values = obj.listLiteralProperties(prop);
                        while (values.hasNext())
                        {
                            SemanticLiteral value = values.next();
                            Element eprop = doc.createElementNS(obj.getSemanticClass().getURI(), prop.getName());
                            name.appendChild(eprop);
                            Text tvalue = doc.createTextNode(value.getString());
                            eprop.appendChild(tvalue);
                        }
                    }
                    else
                    {
                        Element eprop = doc.createElementNS(obj.getSemanticClass().getURI(), prop.getName());
                        name.appendChild(eprop);
                        Text value = doc.createTextNode(obj.getProperty(prop));
                        eprop.appendChild(value);
                    }
                }
                else
                {
                    SemanticClass range = prop.getRangeClass();
                    if (range != null)
                    {
                        if (prop.getName().startsWith("has"))
                        {
                            Iterator<SemanticObject> values = obj.listObjectProperties(prop);
                            while (values.hasNext())
                            {
                                SemanticObject value = values.next();
                                if (value != null)
                                {
                                    Element eprop = doc.createElementNS(obj.getSemanticClass().getURI(), prop.getName());
                                    name.appendChild(eprop);
                                    eprop.setAttributeNS(XLINK_NS, "xlink:href", request.getRequestURI() + "?uri=" + value.getShortURI());
                                }
                            }
                        }
                        else
                        {
                            Element eprop = doc.createElementNS(obj.getSemanticClass().getURI(), prop.getName());
                            name.appendChild(eprop);
                            SemanticObject value = obj.getObjectProperty(prop);
                            if (value != null)
                            {
                                eprop.setAttributeNS(XLINK_NS, "xlink:href", request.getRequestURI() + "?uri=" + value.getShortURI());
                            }
                        }
                    }
                }
            }
        }
    }

    private Document serialize(SemanticObject obj, HttpServletRequest request)
    {
        Document doc = SWBUtils.XML.getNewDocument();
        Element name = doc.createElementNS(obj.getSemanticClass().getURI(), obj.getSemanticClass().getName());
        name.setAttribute("xmlns:xlink", XLINK_NS);
        name.setAttribute("xmlns", obj.getSemanticClass().getURI());
        doc.appendChild(name);
        serialize(obj, name, request);
        return doc;
    }

    private Document getWADL(HttpServletRequest servletRequest, String version)
    {
        String WADL_NS = WADL_NS_2009;
        String WADL_XSD_LOCATION = WADL_XSD_LOCATION_2009;
        if ("2006".equals(version))
        {
            WADL_NS = WADL_NS_2006;
            WADL_XSD_LOCATION = WADL_XSD_LOCATION_2006;
        }
        Document doc = SWBUtils.XML.getNewDocument();
        Element application = doc.createElementNS(WADL_NS, "application");


        application.setAttribute("xmlns:xsi", W3C_XML_SCHEMA_INSTANCE_NS_URI);
        application.setAttribute("xmlns:xsd", W3C_XML_SCHEMA_NS_URI);
        application.setAttribute("xmlns:swbrest", REST_RESOURCES_2010);

        Attr attr = doc.createAttribute("xmlns");
        attr.setValue(WADL_NS);
        application.setAttributeNode(attr);

        Attr schemaLocation = doc.createAttributeNS(W3C_XML_SCHEMA_INSTANCE_NS_URI, "schemaLocation");
        schemaLocation.setValue(WADL_NS + " " + WADL_XSD_LOCATION);
        schemaLocation.setPrefix("xsi");
        application.setAttributeNodeNS(schemaLocation);


        Set<String> prefixes = new HashSet<String>();

        for (SemanticClass clazz : classes)
        {
            if (!prefixes.contains(clazz.getPrefix()))
            {
                application.setAttribute("xmlns:" + clazz.getPrefix(), clazz.getURI());
                prefixes.add(clazz.getPrefix());
            }
        }



        Element grammars = doc.createElementNS(WADL_NS, "grammars");
        application.appendChild(grammars);

        Element include = doc.createElementNS(WADL_NS, "include");
        grammars.appendChild(include);
        include.setAttribute("href", servletRequest.getRequestURI() + "?error=xsd");

        include = doc.createElementNS(WADL_NS, "include");
        grammars.appendChild(include);
        include.setAttribute("href", servletRequest.getRequestURI() + "?created=xsd");

        include = doc.createElementNS(WADL_NS, "include");
        grammars.appendChild(include);
        include.setAttribute("href", servletRequest.getRequestURI() + "?updated=xsd");

        include = doc.createElementNS(WADL_NS, "include");
        grammars.appendChild(include);
        include.setAttribute("href", servletRequest.getRequestURI() + "?deleted=xsd");

        for (SemanticClass clazz : classes)
        {
            String xsd = URLEncoder.encode(clazz.getURI());
            include = doc.createElementNS(WADL_NS, "include");
            grammars.appendChild(include);
            include.setAttribute("href", servletRequest.getRequestURI() + "?xsd=" + xsd);
        }

        doc.appendChild(application);
        Element resources = doc.createElementNS(WADL_NS, "resources");

        String base = servletRequest.getRequestURI();
        int pos = base.lastIndexOf("/");
        if (pos != -1)
        {
            base = base.substring(0, pos);
        }

        resources.setAttribute("base", base);
        base = servletRequest.getRequestURI();
        pos = base.lastIndexOf("/");
        if (pos != -1)
        {
            base = base.substring(pos + 1);
        }
        String resourcepath = base;
        application.appendChild(resources);
        for (SemanticClass clazz : classes)
        {
            Element resource = doc.createElementNS(WADL_NS, "resource");
            resources.appendChild(resource);
            resource.setAttribute("path", resourcepath);
            resource.setAttribute("id", clazz.getName());
            addGetMethod(doc, resource, clazz, WADL_NS);
            addPUTMethod(doc, resource, clazz, WADL_NS);
            addPOSTMethod(doc, resource, clazz, WADL_NS);
            addDELETEMethod(doc, resource, clazz, WADL_NS);
            try
            {
                Class clazzjava = Class.forName(clazz.getClassName());
                Class superclazz = clazzjava.getSuperclass();
                if (superclazz.getName().endsWith("Base"))
                {
                    for (Class c : superclazz.getDeclaredClasses())
                    {
                        if (c.getName().endsWith("ClassMgr"))
                        {
                            Class mgr = c;
                            HashSet<String> ids=new HashSet<String>();
                            for (Method m : mgr.getDeclaredMethods())
                            {
                                if (Modifier.isPublic(m.getModifiers()) && Modifier.isStatic(m.getModifiers()) && (m.getName().startsWith("has") || m.getName().startsWith("list")))
                                {
                                    String id=m.getName();
                                    if(ids.contains(id))
                                    {
                                        id="_"+id;
                                    }
                                    createMethod(doc, resource, clazz, m, WADL_NS,id);
                                    ids.add(id);
                                }
                            }
                            break;
                        }
                    }
                }
            }
            catch (ClassNotFoundException clnfe)
            {
                clnfe.printStackTrace();
            }


        }
        return doc;
    }

    public Document getError(String error)
    {
        String[] errors = new String[1];
        errors[0] = error;
        return getError(errors);
    }

    public Document getError(String[] errors)
    {
        Document doc = SWBUtils.XML.getNewDocument();
        Element eError = doc.createElement("Error");

        Attr xmlns=doc.createAttribute("xmlns");
        xmlns.setValue(REST_RESOURCES_2010);
        eError.setAttributeNode(xmlns);

        doc.appendChild(eError);
        for (String error : errors)
        {
            Element message = doc.createElement("Message");
            Text text = doc.createTextNode(error);
            message.appendChild(text);
            eError.appendChild(message);
        }
        return doc;
    }

    private Class getClassManager(Class clazz) throws Exception
    {
        Class superclazz = clazz.getSuperclass();
        if (superclazz.getName().endsWith("Base"))
        {
            for (Class c : superclazz.getDeclaredClasses())
            {
                if (c.getName().endsWith("ClassMgr"))
                {
                    return c;
                }
            }
        }
        throw new Exception("The class is not a SemanticClass");
    }

    private boolean checkParameters(Method m, HttpServletRequest request)
    {
        for (Class parameter : m.getParameterTypes())
        {
            String parameterName = parameter.getName();
            String value = request.getParameter(parameterName);
            if (value != null)
            {
                try
                {
                    Object parameterValue = convert(value, parameter);
                    if (parameterValue == null)
                    {
                        return false;
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    log.error(e);
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        return true;
    }

    private Object convert(String value, Class clazz) throws Exception
    {
        if (isGenericObject(clazz))
        {
            GenericObject go = SemanticObject.createSemanticObject(value).createGenericInstance();
            return go;
        }
        else
        {
            String type = classToxsd(clazz);
            return get(value, type);
        }
    }

    private Object[] getParameters(Method m, HttpServletRequest request) throws Exception
    {
        ArrayList<Object> getParameters = new ArrayList<Object>();
        for (Class parameter : m.getParameterTypes())
        {
            String parameterName = parameter.getName();
            String value = request.getParameter(parameterName);
            if (value != null)
            {
                Object parameterValue = convert(value, parameter);
                getParameters.add(parameterValue);
            }
            else
            {
                throw new Exception();
            }
        }
        return getParameters.toArray(new Object[getParameters.size()]);
    }

    public Document getExecuteMethod(HttpServletRequest request) throws Exception
    {
        String methodName = request.getParameter("method");
        if(methodName==null)
        {
            throw new Exception("The parameter method was not found");
        }
        if(methodName.startsWith("_"))
        {
            methodName=methodName.substring(1);
        }
        String classUri = request.getParameter("classuri");
        SemanticClass clazz = SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass(classUri);
        if (clazz == null)
        {
            throw new Exception("The class " + classUri + " was not found");
        }
        Class javaClazz = Class.forName(clazz.getClassName());
        Class mgr = getClassManager(javaClazz);
        for (Method m : mgr.getMethods())
        {

            if (m.getName().equals(methodName))
            {
                if (checkParameters(m, request) && Modifier.isStatic(m.getModifiers()) && Modifier.isPublic(m.getModifiers()))
                {                    
                    try
                    {
                        Object[] args = getParameters(m, request);
                        Object resinvoke = m.invoke(null, args);                        
                        if (resinvoke != null)
                        {
                            if (resinvoke instanceof SemanticObject)
                            {
                                SemanticObject so = (SemanticObject) resinvoke;
                                return serialize(so, request);
                            }
                            if (resinvoke instanceof GenericIterator)
                            {
                                Document doc = SWBUtils.XML.getNewDocument();
                                Element res = doc.createElement(m.getName());

                                Attr xmlns=doc.createAttribute("xmlns");
                                xmlns.setValue(REST_RESOURCES_2010);
                                res.setAttributeNode(xmlns);

                                Attr swbrest=doc.createAttribute("xmlns:swbrest");
                                xmlns.setValue(REST_RESOURCES_2010);
                                res.setAttributeNode(swbrest);

                                doc.appendChild(res);
                                res.setAttribute("xmlns:xlink", XLINK_NS);
                                GenericIterator gi = (GenericIterator) resinvoke;
                                if (gi.hasNext())
                                {
                                    GenericObject go = gi.next();
                                    SemanticObject obj = go.getSemanticObject();
                                    Element name = doc.createElementNS(REST_RESOURCES_2010, obj.getSemanticClass().getName());
                                    name.setPrefix("swbrest");
                                    name.setAttributeNS(XLINK_NS, "xlink:href", request.getRequestURI() + "?uri=" + obj.getShortURI());
                                    name.setAttribute("uri", obj.getURI());
                                    name.setAttribute("shortURI", obj.getShortURI());
                                    res.appendChild(name);
                                }
                                while (gi.hasNext())
                                {
                                    GenericObject go = gi.next();
                                    SemanticObject obj = go.getSemanticObject();
                                    Element name = doc.createElementNS(REST_RESOURCES_2010, obj.getSemanticClass().getName());
                                    name.setPrefix("swbrest");
                                    name.setAttributeNS(XLINK_NS, "xlink:href", request.getRequestURI() + "?uri=" + obj.getShortURI());                                    
                                    name.setAttribute("uri", obj.getURI());
                                    name.setAttribute("shortURI", obj.getShortURI());
                                    res.appendChild(name);
                                }
                                return doc;
                            }
                        }
                        else
                        {
                            Document doc = SWBUtils.XML.getNewDocument();
                            return doc;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        log.error(e);
                        throw e;
                    }
                }
            }
        }
        throw new Exception("The method " + methodName + " was not found");

    }

    public Document getCreated(String uri)
    {
        Document doc = SWBUtils.XML.getNewDocument();
        Element created = doc.createElement("Created");
        doc.appendChild(created);
        Attr xmlns=doc.createAttribute("xmlns");
        xmlns.setValue(REST_RESOURCES_2010);
        created.appendChild(xmlns);
        created.setAttribute("uri", uri);
        return doc;
    }

    public Document getUpdated(String uri)
    {
        Document doc = SWBUtils.XML.getNewDocument();
        Element updated = doc.createElement("Updated");
        Attr xmlns=doc.createAttribute("xmlns");
        xmlns.setValue(REST_RESOURCES_2010);
        updated.appendChild(xmlns);

        doc.appendChild(updated);
        updated.setAttribute("uri", uri);
        return doc;
    }

    public Document getDeleted(String uri)
    {
        Document doc = SWBUtils.XML.getNewDocument();
        Element deleted = doc.createElement("Deleted");
        Attr xmlns=doc.createAttribute("xmlns");
        xmlns.setValue(REST_RESOURCES_2010);
        deleted.appendChild(xmlns);
        doc.appendChild(deleted);
        deleted.setAttribute("uri", uri);
        return doc;
    }

    private void showError(HttpServletRequest request, HttpServletResponse response, String msg) throws IOException
    {
        String[] error = new String[1];
        error[0] = msg;
        showError(request, response, error);
    }

    private void showError(HttpServletRequest request, HttpServletResponse response, String[] msg) throws IOException
    {
        Document doc = getError(msg);
        PrintWriter out = response.getWriter();
        String charset = Charset.defaultCharset().name();
        response.setContentType("application/xml; charset=" + charset);
        String xml = SWBUtils.XML.domToXml(doc, charset, true);
        out.print(xml);
        out.close();
    }

    private void showCreatedXSDResponse(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        Document doc = getCreatedXSD();
        PrintWriter out = response.getWriter();
        String charset = Charset.defaultCharset().name();
        response.setContentType("application/xml; charset=" + charset);
        String xml = SWBUtils.XML.domToXml(doc, charset, true);
        out.print(xml);
        out.close();
    }

    private void showDeletedXSDResponse(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        Document doc = getDeletedXSD();
        PrintWriter out = response.getWriter();
        String charset = Charset.defaultCharset().name();
        response.setContentType("application/xml; charset=" + charset);
        String xml = SWBUtils.XML.domToXml(doc, charset, true);
        out.print(xml);
        out.close();
    }

    private void showUpdatedXSDResponse(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        Document doc = getUpdatedXSD();
        PrintWriter out = response.getWriter();
        String charset = Charset.defaultCharset().name();
        response.setContentType("application/xml; charset=" + charset);
        String xml = SWBUtils.XML.domToXml(doc, charset, true);
        out.print(xml);
        out.close();
    }

    private void showErrorXSD(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        Document doc = getErrorXSD();
        PrintWriter out = response.getWriter();
        String charset = Charset.defaultCharset().name();
        response.setContentType("application/xml; charset=" + charset);
        String xml = SWBUtils.XML.domToXml(doc, charset, true);
        out.print(xml);
        out.close();
    }

    private Object get(String value, String dataType) throws Exception
    {
        dataType = dataType.replace("xsd:", SemanticVocabulary.XMLS_URI);
        if (dataType.equals(SemanticVocabulary.XMLS_BOOLEAN))
        {
            return Boolean.parseBoolean(value);
        }
        if (dataType.equals(SemanticVocabulary.XMLS_DATE))
        {
            return SWBUtils.TEXT.iso8601DateParse(value);
        }
        if (dataType.equals(SemanticVocabulary.XMLS_DATETIME))
        {
            return Timestamp.valueOf(value);
        }
        if (dataType.equals(SemanticVocabulary.XMLS_BYTE))
        {
            return Byte.parseByte(value);
        }
        if (dataType.equals(SemanticVocabulary.XMLS_DOUBLE))
        {
            return Double.parseDouble(value);
        }
        if (dataType.equals(SemanticVocabulary.XMLS_FLOAT))
        {
            return Float.parseFloat(value);
        }
        if (dataType.equals(SemanticVocabulary.XMLS_INT) || dataType.equals(SemanticVocabulary.XMLS_INTEGER))
        {
            return Integer.parseInt(value);
        }
        if (dataType.equals(SemanticVocabulary.XMLS_LONG))
        {
            return Long.parseLong(value);
        }
        if (dataType.equals(SemanticVocabulary.XMLS_SHORT))
        {
            return Short.parseShort(value);
        }
        if (dataType.equals(SemanticVocabulary.XMLS_URI))
        {
            String uri = value;
            if (uri.indexOf(":") != -1)
            {
                if (uri.indexOf("%3A") != -1)
                {
                    uri = uri.replace("%3A", ":");
                }
                if (uri.indexOf("%23") != -1)
                {
                    uri = uri.replace("%23", "#");
                }
                if (uri.indexOf("#") == -1)
                {
                    uri = SemanticObject.shortToFullURI(uri);
                }
            }
            SemanticObject ovalue = SemanticObject.createSemanticObject(uri);
            if (ovalue == null)
            {
                throw new Exception("The object with uri " + value + " was not found");
            }
            return ovalue;
        }
        return value;
    }

    private void validate(String value, String dataType) throws Exception
    {
        dataType = dataType.replace("xsd:", SemanticVocabulary.XMLS_URI);
        if (dataType.equals(SemanticVocabulary.XMLS_BOOLEAN))
        {
            if (!(value.equals("true") || value.equals("false")))
            {
                throw new Exception("The value is invalid");
            }
        }
        if (dataType.equals(SemanticVocabulary.XMLS_DATE))
        {
            SWBUtils.TEXT.iso8601DateParse(value);
        }
        if (dataType.equals(SemanticVocabulary.XMLS_DATETIME))
        {
            Timestamp.valueOf(value);
        }
        if (dataType.equals(SemanticVocabulary.XMLS_BYTE))
        {
            Byte.parseByte(value);
        }
        if (dataType.equals(SemanticVocabulary.XMLS_DOUBLE))
        {
            Double.parseDouble(value);
        }
        if (dataType.equals(SemanticVocabulary.XMLS_FLOAT))
        {
            Float.parseFloat(value);
        }
        if (dataType.equals(SemanticVocabulary.XMLS_INT) || dataType.equals(SemanticVocabulary.XMLS_INTEGER))
        {
            Integer.parseInt(value);
        }
        if (dataType.equals(SemanticVocabulary.XMLS_LONG))
        {
            Long.parseLong(value);
        }
        if (dataType.equals(SemanticVocabulary.XMLS_SHORT))
        {
            Short.parseShort(value);
        }
    }

    private void updateProperties(HttpServletRequest request, SemanticObject obj) throws Exception
    {
        Iterator<SemanticProperty> props = obj.listProperties();
        while (props.hasNext())
        {
            SemanticProperty prop = props.next();
            if (!prop.hasInverse())
            {
                String[] values = request.getParameterValues(prop.getName());
                if (prop.isDataTypeProperty())
                {
                    String type = XSD_STRING;
                    if (prop.isBoolean())
                    {
                        type = SemanticVocabulary.XMLS_BOOLEAN;
                    }
                    else if (prop.isBinary())
                    {
                        type = SemanticVocabulary.XMLS_BASE64BINARY;
                    }
                    else if (prop.isByte())
                    {
                        type = SemanticVocabulary.XMLS_BYTE;
                    }
                    else if (prop.isDate())
                    {
                        type = SemanticVocabulary.XMLS_DATE;
                    }
                    else if (prop.isDateTime())
                    {
                        type = SemanticVocabulary.XMLS_DATETIME;
                    }
                    else if (prop.isDouble())
                    {
                        type = SemanticVocabulary.XMLS_DOUBLE;
                    }
                    else if (prop.isFloat())
                    {
                        type = SemanticVocabulary.XMLS_FLOAT;
                    }
                    else if (prop.isInt())
                    {
                        type = SemanticVocabulary.XMLS_INT;
                    }
                    else if (prop.isLong())
                    {
                        type = SemanticVocabulary.XMLS_LONG;
                    }
                    else if (prop.isShort())
                    {
                        type = SemanticVocabulary.XMLS_SHORT;
                    }
                    type = type.replace(W3C_XML_SCHEMA_NS_URI + "#", "xsd:");
                    if (prop.getName().startsWith("has"))
                    {
                        if (values != null)
                        {
                            // Validate
                            for (String value : values)
                            {
                                validate(value, type);
                            }
                            // set the values                            
                            for (String value : values)
                            {
                                SemanticLiteral literal = SemanticLiteral.valueOf(prop, value);
                                obj.addLiteralProperty(prop, literal);
                            }

                        }

                    }
                    else
                    {
                        if (values != null)
                        {
                            if (values.length != 1)
                            {
                                throw new Exception("The property " + prop.getName() + " has single value");
                            }
                            if (values[0] != null)
                            {
                                Object value = get(values[0], type);
                                if (value instanceof Date)
                                {
                                    obj.setDateProperty(prop, (Date) value);
                                }
                                if (value instanceof Timestamp)
                                {
                                    obj.setDateTimeProperty(prop, (Timestamp) value);
                                }
                                if (value instanceof Boolean)
                                {
                                    obj.setBooleanProperty(prop, ((Boolean) value).booleanValue());
                                }
                                else
                                {
                                    obj.setProperty(prop, value.toString());
                                }
                            }


                        }

                    }
                }
                else
                {
                    if (prop.getName().startsWith("has"))
                    {
                        if (values != null)
                        {
                            // Validate
                            for (String value : values)
                            {
                                String uri = value;
                                if (uri.indexOf(":") != -1)
                                {
                                    if (uri.indexOf("%3A") != -1)
                                    {
                                        uri = uri.replace("%3A", ":");
                                    }
                                    if (uri.indexOf("%23") != -1)
                                    {
                                        uri = uri.replace("%23", "#");
                                    }
                                    if (uri.indexOf("#") == -1)
                                    {
                                        uri = SemanticObject.shortToFullURI(uri);
                                    }
                                }
                                SemanticObject testobj = SemanticObject.createSemanticObject(uri);
                                if (testobj == null)
                                {
                                    throw new Exception("The object with uri " + value + " was not found");
                                }
                                obj.addObjectProperty(prop, testobj);

                            }

                        }

                    }
                    else
                    {
                        if (values != null)
                        {
                            if (values.length != 1)
                            {
                                throw new Exception("The property " + prop.getName() + " has single value");
                            }
                            if (values[0] != null)
                            {
                                String uri = values[0];
                                if (uri.indexOf(":") != -1)
                                {
                                    if (uri.indexOf("%3A") != -1)
                                    {
                                        uri = uri.replace("%3A", ":");
                                    }
                                    if (uri.indexOf("%23") != -1)
                                    {
                                        uri = uri.replace("%23", "#");
                                    }
                                    if (uri.indexOf("#") == -1)
                                    {
                                        uri = SemanticObject.shortToFullURI(uri);
                                    }
                                }
                                SemanticObject testobj = SemanticObject.createSemanticObject(uri);
                                if (testobj == null)
                                {
                                    throw new Exception("The object with uri " + values[0] + " was not found");
                                }
                                obj.addObjectProperty(prop, testobj);
                            }

                        }

                    }
                }
            }
        }
    }

    private void showDeleted(HttpServletRequest request, HttpServletResponse response, String uri) throws IOException
    {
        Document doc = getDeleted(uri);
        PrintWriter out = response.getWriter();
        String charset = Charset.defaultCharset().name();
        response.setContentType("application/xml; charset=" + charset);
        String xml = SWBUtils.XML.domToXml(doc, charset, true);
        out.print(xml);
        out.close();
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        if (request.getMethod().toLowerCase().equals("delete"))
        {
            if (request.getParameter("uri") != null)
            {
                String uri = request.getParameter("uri");
                if (uri.indexOf(":") != -1)
                {
                    if (uri.indexOf("%3A") != -1)
                    {
                        uri = uri.replace("%3A", ":");
                    }
                    if (uri.indexOf("%23") != -1)
                    {
                        uri = uri.replace("%23", "#");
                    }
                    if (uri.indexOf("#") == -1)
                    {
                        uri = SemanticObject.shortToFullURI(uri);
                    }
                }

                SemanticObject obj = SemanticObject.createSemanticObject(uri);
                if (obj != null)
                {
                    uri = obj.getShortURI();
                    obj.remove();
                    showDeleted(request, response, uri);
                }
                else
                {
                    showError(request, response, "Invalid uri");
                    return;
                }


            }
            else
            {
                showError(request, response, "The parameter uri was not found");
                return;

            }
        }
        else if (request.getMethod().toLowerCase().equals("get"))
        {
            if (request.getParameter(XSD_PREFIX) != null)
            {
                String uri = request.getParameter(XSD_PREFIX);
                SemanticObject obj = SemanticObject.createSemanticObject(uri);
                if (obj != null)
                {
                    SemanticClass clazz = SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass(uri);
                    showXSD(request, response, clazz);
                }

            }
            else if (request.getParameter("error") != null)
            {
                showErrorXSD(request, response);
            }
            else if (request.getParameter("created") != null)
            {
                showCreatedXSDResponse(request, response);
            }
            else if (request.getParameter("updated") != null)
            {
                showUpdatedXSDResponse(request, response);
            }
            else if (request.getParameter("deleted") != null)
            {
                showDeletedXSDResponse(request, response);
            }
            else if (request.getParameter("method") != null && request.getParameter("classuri") != null)
            {
                executeMethod(request, response);
            }
            else if (request.getParameter("uri") != null)
            {
                String uri = request.getParameter("uri");
                if (uri.indexOf(":") != -1)
                {
                    if (uri.indexOf("%3A") != -1)
                    {
                        uri = uri.replace("%3A", ":");
                    }
                    if (uri.indexOf("%23") != -1)
                    {
                        uri = uri.replace("%23", "#");
                    }
                    if (uri.indexOf("#") == -1)
                    {
                        uri = SemanticObject.shortToFullURI(uri);
                    }
                }

                SemanticObject obj = SemanticObject.createSemanticObject(uri);
                if (obj != null)
                {
                    showObject(request, response, obj);
                }
                else
                {
                    response.setStatus(400);
                    showError(request, response, "The objct with uri " + request.getParameter("uri") + " was not found");
                }


            }
            else
            {
                showList(request, response);
            }
        }
        if (request.getMethod().toLowerCase().equals("post"))
        {
            String uri = request.getParameter(REST_URI);
            if (uri.indexOf(":") != -1)
            {
                if (uri.indexOf("%3A") != -1)
                {
                    uri = uri.replace("%3A", ":");
                }
                if (uri.indexOf("%23") != -1)
                {
                    uri = uri.replace("%23", "#");
                }
                if (uri.indexOf("#") == -1)
                {
                    uri = SemanticObject.shortToFullURI(uri);
                }
            }
            SemanticObject obj = SemanticObject.createSemanticObject(uri);
            try
            {
                updateProperties(request, obj);
                showUpdated(request, response, obj.getURI());
            }
            catch (Exception e)
            {
                obj.remove();
                response.setStatus(400);
                showError(request, response, e.getMessage());
                return;
            }
        }
        else if (request.getMethod().toLowerCase().equals("put"))
        {
            String classuri = request.getParameter(REST_CLASSURI);
            String modeluri = request.getParameter(REST_MODELURI);
            if (classuri == null)
            {
                response.setStatus(400);
                showError(request, response, "The parameter rest:classuri is required");
                return;
            }
            if (modeluri == null)
            {
                response.setStatus(400);
                showError(request, response, "The parameter modeluri is required");
                return;
            }

            if (classuri.indexOf(":") != -1)
            {
                if (classuri.indexOf("%3A") != -1)
                {
                    classuri = classuri.replace("%3A", ":");
                }
                if (classuri.indexOf("%23") != -1)
                {
                    classuri = classuri.replace("%23", "#");
                }
                if (classuri.indexOf("#") == -1)
                {
                    classuri = SemanticObject.shortToFullURI(classuri);
                }
            }
            SemanticClass clazz = SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass(classuri);
            if (clazz == null)
            {
                response.setStatus(400);
                showError(request, response, "The class " + request.getParameter("classuri") + " was not found");
                return;
            }
            if (!clazz.isAutogenId())
            {
                if (request.getParameter("id") == null)
                {
                    response.setStatus(400);
                    showError(request, response, "The parameter id is required");
                    return;
                }
            }



            if (modeluri.indexOf(":") != -1)
            {
                if (modeluri.indexOf("%3A") != -1)
                {
                    modeluri = classuri.replace("%3A", ":");
                }
                if (modeluri.indexOf("%23") != -1)
                {
                    modeluri = classuri.replace("%23", "#");
                }
                if (modeluri.indexOf("#") == -1)
                {
                    modeluri = SemanticObject.shortToFullURI(modeluri);
                }
            }
            SemanticObject objmodel = SemanticObject.createSemanticObject(modeluri);
            if (objmodel == null)
            {
                response.setStatus(400);
                showError(request, response, "The model " + request.getParameter(REST_MODELURI) + " was not found");
                return;
            }
            if (!objmodel.getSemanticClass().isSWBModel())
            {
                response.setStatus(400);
                showError(request, response, "The object " + request.getParameter(REST_MODELURI) + " is not a model");
                return;
            }
            GenericObject model = (GenericObjectBase) objmodel.createGenericInstance();
            String id = null;
            if (clazz.isAutogenId())
            {
                id = String.valueOf(model.getSemanticObject().getModel().getCounter(clazz));
            }
            else
            {
                id = request.getParameter(REST_ID);
                if (id == null)
                {
                    response.setStatus(400);
                    showError(request, response, "The object parameter rest:id was not found");
                    return;
                }
                SemanticObject objtest = model.getSemanticObject().getModel().createSemanticObjectById(id, clazz);
                if (objtest != null)
                {
                    response.setStatus(400);
                    showError(request, response, "The parameter rest:id is invalid, the id already exists");
                    return;
                }
            }
            GenericObject newobj = model.getSemanticObject().getModel().createGenericObject(model.getSemanticObject().getModel().getObjectUri(id, clazz), clazz);
            // update properties
            try
            {
                updateProperties(request, newobj.getSemanticObject());
                showCreted(request, response, newobj.getURI());
            }
            catch (Exception e)
            {
                newobj.getSemanticObject().remove();
                response.setStatus(400);
                showError(request, response, e.getMessage());
                return;
            }
        }

    }
}

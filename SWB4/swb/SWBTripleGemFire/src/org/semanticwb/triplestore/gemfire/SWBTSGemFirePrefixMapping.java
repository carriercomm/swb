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
package org.semanticwb.triplestore.gemfire;

import com.gemstone.gemfire.cache.Region;
import com.hp.hpl.jena.rdf.model.impl.Util;
import com.hp.hpl.jena.shared.PrefixMapping;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.semanticwb.Logger;
import org.semanticwb.SWBPlatform;
import org.semanticwb.SWBUtils;

/**
 *
 * @author jei
 */
public class SWBTSGemFirePrefixMapping implements PrefixMapping
{
    private static Logger log = SWBUtils.getLogger(SWBTSGemFirePrefixMapping.class);
    private SWBTSGemFireGraph graph;

    private Map<String,String> map=new ConcurrentHashMap();

    public SWBTSGemFirePrefixMapping(SWBTSGemFireGraph graph)
    {
        this.graph = graph;
        Map map2=getNsPrefixMap();
    }

    @Override
    public PrefixMapping setNsPrefix(String prefix, String uri)
    {
        if(!map.containsKey(prefix) || map.get(prefix)==null || !map.get(prefix).equals(uri))
        {
            map.put(prefix, uri);
            try
            {
                //Region db = SWBTSGemFire.getCache().getRegion(SWBPlatform.getEnv("swb/gemfire_region_name","swb"));
                
                Region<String,String> rprefix = SWBTSGemFire.getCache().getRegion("swb_prefix");                

                //rprefix.put(prefix, new SWBTSGemFirePrefix(graph.getId(), uri, prefix));
                rprefix.put(prefix, uri);
                
            } catch (Exception e2)
            {
                log.error(e2);
            }
        }
        return this;
    }

    @Override
    public PrefixMapping removeNsPrefix(String prefix)
    {
        map.remove(prefix);
        try
        {
            //Region db = SWBTSGemFire.getCache().getRegion(SWBPlatform.getEnv("swb/gemfire_region_name","swb"));
            Region<String,String> rprefix = SWBTSGemFire.getCache().getRegion("swb_prefix");                
            rprefix.remove(prefix);
            
        } catch (Exception e2)
        {
            log.error(e2);
        }
        return this;
    }

    @Override
    public PrefixMapping setNsPrefixes(PrefixMapping other)
    {
        return setNsPrefixes(other.getNsPrefixMap());
    }

    @Override
    public PrefixMapping setNsPrefixes(Map<String, String> map)
    {
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext())
        {
            String prefix = it.next();
            setNsPrefix(prefix, map.get(prefix));
        }
        return this;
    }

    @Override
    public PrefixMapping withDefaultMappings(PrefixMapping map)
    {
        for (Map.Entry<String, String> e : map.getNsPrefixMap().entrySet())
        {
            String prefix = e.getKey(), uri = e.getValue();
            if (getNsPrefixURI(prefix) == null && getNsURIPrefix(uri) == null)
            {
                setNsPrefix(prefix, uri);
            }
        }
        return this;
    }

    @Override
    public String getNsPrefixURI(String prefix)
    {
        String str=map.get(prefix);
        if(str==null)
        {
            try
            {
                //Region db = SWBTSGemFire.getCache().getRegion(SWBPlatform.getEnv("swb/gemfire_region_name","swb"));
                Region<String,String> rprefix = SWBTSGemFire.getCache().getRegion("swb_prefix");                
                str=rprefix.get(prefix);
            } catch (Exception e2)
            {
                log.error(e2);
            }
            if(str!=null)map.put(prefix, str);
        }
        return str;
    }

    @Override
    public String getNsURIPrefix(String uri)
    {
        Map.Entry<String, String> e = findMapping(uri, false);
        if (e != null)
        {
            return e.getKey();
        }
        return null;
    }

    @Override
    public Map<String, String> getNsPrefixMap()
    {
        Map<String,String> map2=new ConcurrentHashMap();
        try
        {
            //Region db = SWBTSGemFire.getCache().getRegion(SWBPlatform.getEnv("swb/gemfire_region_name","swb"));
            Region<String,String> rprefix = SWBTSGemFire.getCache().getRegion("swb_prefix");                

            map2=new ConcurrentHashMap(rprefix);
            
        } catch (Exception e2)
        {
            log.error(e2);
        }
        this.map=map2;
        return map2;
    }

    @Override
    public String expandPrefix(String prefixed)
    {
        int colon = prefixed.indexOf(':');
        if (colon < 0)
        {
            return prefixed;
        } else
        {
            String uri = getNsPrefixURI(prefixed.substring(0, colon));
            return uri == null ? prefixed : uri + prefixed.substring(colon + 1);
        }
    }

    @Override
    public String shortForm(String uri)
    {
        Map.Entry<String, String> e = findMapping(uri, true);
        return e == null ? uri : e.getKey() + ":" + uri.substring((e.getValue()).length());
    }

    @Override
    public String qnameFor(String uri)
    {
        int split = Util.splitNamespace(uri);
        String ns = uri.substring(0, split), local = uri.substring(split);
        if (local.equals(""))
        {
            return null;
        }
        String prefix = getNsURIPrefix(ns);
        return prefix == null ? null : prefix + ":" + local;
    }

    @Override
    public PrefixMapping lock()
    {
        //TODO
        //throw new UnsupportedOperationException("Not supported yet.");
        return this;
    }

    @Override
    public boolean samePrefixMappingAs(PrefixMapping other)
    {
        return other instanceof SWBTSGemFirePrefixMapping
                ? equals((SWBTSGemFirePrefixMapping) other)
                : equalsByMap(other);
    }

    protected final boolean equalsByMap(PrefixMapping other)
    {
        return getNsPrefixMap().equals(other.getNsPrefixMap());
    }

    /**
    Answer a prefixToURI entry in which the value is an initial substring of <code>uri</code>.
    If <code>partial</code> is false, then the value must equal <code>uri</code>.

    Does a linear search of the entire prefixToURI, so not terribly efficient for large maps.

    @param uri the value to search for
    @param partial true if the match can be any leading substring, false for exact match
    @return some entry (k, v) such that uri starts with v [equal for partial=false]
     */
    private Map.Entry<String, String> findMapping(String uri, boolean partial)
    {
        Map<String, String> map2 = getNsPrefixMap();
        Iterator<Map.Entry<String, String>> it = map2.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<String, String> e = it.next();
            String ss = e.getValue();
            if (uri.startsWith(ss) && (partial || ss.length() == uri.length()))
            {
                return e;
            }
        }
        return null;
    }
}

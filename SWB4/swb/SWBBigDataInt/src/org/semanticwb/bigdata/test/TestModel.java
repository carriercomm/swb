/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.semanticwb.bigdata.test;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.vocabulary.RDF;
import java.util.Date;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQueryResult;
import org.semanticwb.SWBPlatform;
import org.semanticwb.SWBUtils;
import org.semanticwb.bigdata.BigdataGraph;
import org.semanticwb.bigdata.BigdataModelMaker;
import org.semanticwb.model.SWBContext;
import org.semanticwb.model.WebPage;
import org.semanticwb.model.WebSite;
import org.semanticwb.platform.SemanticModel;
import org.semanticwb.platform.SemanticObject;

/**
 *
 * @author jei
 */
public class TestModel {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SWBUtils.createInstance("/programming/proys/SWB4/swb/build/web");
        String base = SWBUtils.getApplicationPath();
        SWBPlatform.createInstance();
        //SWBPlatform.getSemanticMgr().initializeDB();
        SWBPlatform.getSemanticMgr().addBaseOntology(base + "/WEB-INF/owl/swb.owl");
        SWBPlatform.getSemanticMgr().addBaseOntology(base + "/WEB-INF/owl/swb_rep.owl");
        SWBPlatform.getSemanticMgr().addBaseOntology(base + "/WEB-INF/owl/office.owl");
        SWBPlatform.getSemanticMgr().loadBaseVocabulary();
        //SWBPlatform.getSemanticMgr().loadDBModels();
        SWBPlatform.getSemanticMgr().getOntology().rebind();

        String path="/";
        String name="big";
        String ns="http://www." + name + ".swb#";

        long time=System.currentTimeMillis();

        BigdataModelMaker maker=new BigdataModelMaker(path);
        Model model=maker.getModel(name, false);

        boolean init=false;
        if(model==null)
        {
            model=maker.createModel(name, false);
            model.setNsPrefix(name, ns);
            init=true;
        }

        SemanticModel smodel=new SemanticModel(name, model);
        SWBPlatform.getSemanticMgr().addModel(smodel, true);

        WebSite site=null;
        if(init)site=(org.semanticwb.model.WebSite)smodel.createGenericObject(smodel.getObjectUri(name,WebSite.sclass),WebSite.sclass);
        else site=SWBContext.getWebSite(name);

        System.out.println("Init:"+(System.currentTimeMillis()-time));
        time=System.currentTimeMillis();

        if(init)
        {
            model.begin();
            for(int x=0;x<100000;x++)
            {
                WebPage page=site.createWebPage("page"+x);
                page.setTitle("Page "+x);
                page.setViews(x%100);
                page.setCreated(new Date());
                if(x>50000 && x<55000)page.setActive(true);
                else page.setActive(false);
                if(x%10000==0)System.out.println(x);
            }
            model.commit();

            System.out.println("Load:"+(System.currentTimeMillis()-time));
            time=System.currentTimeMillis();
        }

        WebPage page=site.getWebPage("page524000");
        System.out.println(page);

        System.out.println("get:"+(System.currentTimeMillis()-time));
        time=System.currentTimeMillis();

        page=site.getWebPage("page524001");
        System.out.println(page);

        System.out.println("get:"+(System.currentTimeMillis()-time));
        time=System.currentTimeMillis();

        //SparQL
        String query=
            //"select ?age " +
            "select ?created ?s ?views " +
            //"select ?s " +
            "where { " +
            //"  ?s <"+WebPage.swb_active.getURI()+"> true . " +
            "  ?s <"+RDF.type+"> <"+WebPage.sclass.getURI()+"> . " +
            //"  ?s <"+WebPage.swb_views.getURI()+"> ?views . " +
            //"  ?s <"+WebPage.swb_created.getURI()+"> ?created . " +
            //"  FILTER (?views > 10) . " +
            //"  FILTER (?views < 30) . " +
            //"  FILTER (?views = 30) . " +
            //" filter (?time > 129826447539) . " +
            "} " +
            //"order by desc(?created) " +
            "limit 20 offset 99980 " +
            "";

        TupleQueryResult result=((BigdataGraph)model.getGraph()).executeQuery(query, QueryLanguage.SPARQL, false);
        try
        {
            while (result.hasNext())
            {
                BindingSet bindingSet = result.next();
                System.out.println("bindingSet:" + bindingSet);
            }
            //result.close();
        } catch (QueryEvaluationException ex)
        {
            ex.printStackTrace();
        }
        System.out.println("query:"+(System.currentTimeMillis()-time));
        time=System.currentTimeMillis();

        model.close();
    }

}

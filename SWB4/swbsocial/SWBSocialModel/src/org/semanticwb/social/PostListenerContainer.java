package org.semanticwb.social;

import java.util.Date;
import java.util.Iterator;
import org.semanticwb.model.SWBModel;
import org.semanticwb.platform.SemanticObject;


   /**
   * En esta clase se guardan todos los post que lleguan por el listener, se estima que toda la info. que se guarde en este objeto debe de eliminarse aproximadamente c/mes, siendo este parametro configurable de acuerdo al tiempo que la organización quiera guardar  la información sobre los mensajes que lleguen por el listener. Cuando un post que llegue por el listener sea tomado como base para crear un nuevo post por la organización, se cree que debe copiarse la información de dicho post de esta clase hacia la clase PostListenerContainerBase. 
   */
public class PostListenerContainer extends org.semanticwb.social.base.PostListenerContainerBase 
{
    public PostListenerContainer(org.semanticwb.platform.SemanticObject base)
    {
        super(base);
    }

    public static PostListenerContainer getPostListenerContainerByDate(Date date, SWBModel model)
    {
        //
        //System.out.println("Borrado de todos los PostContainers...-No baorraraaaa");
        /*
        Iterator <PostContainer> itPostC=PostContainer.ClassMgr.listPostContainers(model);
        while(itPostC.hasNext())
        {
            PostContainer postC=itPostC.next();
            postC.remove();
        }
         * */

        //System.out.println("Termina Borrado de todos los PostContainers...");
        //

        int year=1900+(date.getYear());
        int month=1+(date.getMonth());
        //System.out.println("year pa buscar:"+year);
        //System.out.println("month pa buscar:"+month);
        Iterator<SemanticObject> it=model.getSemanticModel().listSubjects(social_plc_year, year+""); //No encuentra
        while(it.hasNext())
        {
            SemanticObject obj=it.next();
            PostListenerContainer plcContainer=(PostListenerContainer)obj.createGenericInstance();
            //System.out.println("postContainer encontrado:"+plcContainer+",year:"+plcContainer.getPlc_year()+", month:"+plcContainer.getPlc_month());
            if(plcContainer.getPlc_month().equals(String.valueOf(month)));
            {
                //System.out.println("Regresa PostContainer:"+plcContainer);
                return plcContainer;
            }
        }
        //System.out.println("Va ha crear nuevo postContainer..");
        PostListenerContainer plcContainer=PostListenerContainer.ClassMgr.createPostListenerContainer(model);
        plcContainer.setPlc_year(year+"");
        plcContainer.setPlc_month(month+"");
        return plcContainer;
    }
}

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="org.semanticwb.portal.lib.*,java.text.*,org.semanticwb.portal.api.*,org.semanticwb.portal.community.*,org.semanticwb.*,org.semanticwb.model.*,java.util.*"%>

<script language="Javascript" type="text/javascript">
    function validateremove(url, title,uri)
    {
        if(confirm('¿Esta seguro de borrar la entrada '+title+'?'))
        {
            var url=url+'&uri='+escape(uri);
            window.location.href=url;
        }
    }
</script>

<%
            SWBParamRequest paramRequest = (SWBParamRequest) request.getAttribute("paramRequest");
            if (paramRequest == null)
            {
                return;
            }
            User user = paramRequest.getUser();
            WebPage wpage = paramRequest.getWebPage();
            PostElement post = (PostElement) request.getAttribute("post");
            Member member = Member.getMember(user, wpage);
            if (!post.canView(member) || post == null)
            {
%>
<p>No tiene permisos para ver esta entrada, o la entrada ya no existe</p>
<%
        return;
    }
    

    String updated = SWBUtils.TEXT.getTimeAgo(post.getUpdated(), user.getLanguage());
    String postAuthor = post.getCreator().getFirstName();
    postAuthor = post.getCreator().getFullName();        
    post.incViews();  //Incrementar apariciones    
    DecimalFormat df = new DecimalFormat("#0.0#");
    String rank = df.format(post.getRank());
    SWBResourceURL removeUrl = paramRequest.getActionUrl();
    removeUrl.setParameter("act", "remove");
    boolean canadd=post.canModify(member);
    String editURL=paramRequest.getRenderUrl().setParameter("act","edit").setParameter("uri",post.getURI()).setParameter("mode","editpost").toString();
    String deleteUrl="javascript:validateremove('"+removeUrl+"','"+post.getTitle()+"','"+post.getURI()+"')";
%>

<div class="columnaIzquierda">
    <div class="adminTools">
        <%
            if (canadd)
            {
        %>
        <a class="adminTool" href="<%=editURL%>">Editar Entrada</a>
        <a class="adminTool" href="<%=deleteUrl%>">Eliminar Entrada</a>
        <%
            }
        %>
    </div>
    <h2 class="hidden"><%=post.getTitle()%></h2>
    
    <%=post.getContent()%>
    <br>
    <br>

    <%
            SWBResponse res = new SWBResponse(response);
            post.renderGenericElements(request, res, paramRequest);
            out.write(res.toString());
%>
</div>
<div class="columnaCentro">
    <h2 class="blogTitle"><%=post.getTitle()%></h2>
    <p> <%=post.getDescription()%> </p>
    <p> Autor: <%=postAuthor%> </p>
    <p> Acualizado: <%=updated%> </p>
    <p> Calificación: <%=rank%> </p>
    <p> Visitas <%=post.getViews()%> </p>    
</div>


<%@page contentType="text/html"%>
<%@page import="org.semanticwb.platform.*,org.semanticwb.portal.api.*,org.semanticwb.portal.community.*,org.semanticwb.*,org.semanticwb.model.*,java.util.*"%>
<%
    SWBParamRequest paramRequest=(SWBParamRequest)request.getAttribute("paramRequest");
    User user=paramRequest.getUser();
    WebPage wpage=paramRequest.getWebPage();
    Member member=Member.getMember(user,wpage);
%>
<%
        String uri=request.getParameter("uri");
        Video rec=(Video)SemanticObject.createSemanticObject(uri).getGenericInstance();
        if(rec!=null)
        {
%>
      <table border="0" width="100%" cellspacing="10">
        <tr><td valign="top">
        <%=rec.getCode()%>
        </td><td valign="top"><small>
        <%=rec.getTitle()%> <BR>
        <%=rec.getDescription()%> <BR>
        <%=rec.getCreated()%> <BR>
        <!--</small></td><td valign="top"><small>-->
        <%=rec.getViews()%> vistas<BR>
        </small></td></tr>
      </table>
<%
        }
%>        

<center>
    <a href="<portlet:renderURL/>">Regresar</a> 
    <%if(rec.canModify(member)){%><a href="<%=paramRequest.getRenderUrl().setParameter("act","edit").setParameter("uri",rec.getURI())%>">Editar Información</a><%}%>
    <%if(rec.canModify(member)){%><a href="<%=paramRequest.getActionUrl().setParameter("act","removeVideo").setParameter("uri",rec.getURI())%>">Eliminar Video</a><%}%>
</center>
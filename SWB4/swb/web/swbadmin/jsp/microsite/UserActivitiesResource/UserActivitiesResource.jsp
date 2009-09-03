<%@page import="org.semanticwb.portal.community.utilresources.CommunityActivity,java.text.*,java.net.*,org.semanticwb.platform.SemanticObject,org.semanticwb.portal.api.*,org.semanticwb.portal.community.*,org.semanticwb.*,org.semanticwb.model.*,java.util.*" %>

<%
            SWBParamRequest paramRequest = (SWBParamRequest) request.getAttribute("paramRequest");
            User user = paramRequest.getUser();
            int numrec = (Integer) request.getAttribute("numrec");
            Iterator<CommunityActivity> activities = (Iterator<CommunityActivity>) request.getAttribute("activities");
%>
<div id="contactos">
    <h2>Actividades</h2>
    <ul>
        <%

            CommunityActivity ca = null;
            MicroSiteElement mse = null;
            MicroSite ms = null;
            if (activities.hasNext())
            {
                int num = 0;
                while (activities.hasNext())
                {
                    num++;
                    if (num > numrec)
                    {
                        break;
                    }
                    ca = activities.next();
                    user = ca.getUser();
                    mse = ca.getElement();
                    ms = ca.getCommunity();
                    if (mse != null && user != null && ms != null)
                    {
        %>
        <li><a class="contactos_nombre" href="<%=mse.getURL()%>"><%=mse.getDisplayTitle(user.getLanguage())%></a>
            <a class="contactos_nombre" href="#"><%=SWBUtils.TEXT.getTimeAgo(mse.getUpdated(), user.getLanguage())%></a></li>
            <%

                        //out.println("("+mse.getSemanticObject().getSemanticClass().getDisplayName(user.getLanguage())+")</a>");

                    }
                }
            }
            else
            {
            %>
        <li>No hay actividades que reportar.</li>
        <%            }
        %>
    </ul>
</div>
<%

%>
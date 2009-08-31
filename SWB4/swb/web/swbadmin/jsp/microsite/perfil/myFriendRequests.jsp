<jsp:useBean id="paramRequest" scope="request" type="org.semanticwb.portal.api.SWBParamRequest"/>
<%@page import="org.semanticwb.model.User"%>
<%@page import="org.semanticwb.portal.community.*"%>
<%@page import="org.semanticwb.model.*"%>
<%@page import="org.semanticwb.SWBPlatform"%>
<%@page import="java.util.*"%>
<%@page import="org.semanticwb.portal.api.SWBResourceURL"%>
<%@page import="org.semanticwb.platform.SemanticObject"%>
<%
            User owner = paramRequest.getUser();
            User user = owner;
            if (request.getParameter("user") != null)
            {
                SemanticObject semObj = SemanticObject.createSemanticObject(request.getParameter("user"));
                user = (User) semObj.createGenericInstance();
            }
            String userParam = "";
            if (owner != user)
            {
                userParam = "?user=" + user.getEncodedURI();
            }

            WebPage wpage = paramRequest.getWebPage();
            SWBResourceURL urlAction = paramRequest.getActionUrl();

            String perfilPath = paramRequest.getWebPage().getWebSite().getWebPage("perfil").getUrl();
            String requestedPath = paramRequest.getWebPage().getWebSite().getWebPage("mis_solicitudes").getUrl();

            String photo = SWBPlatform.getContextPath() + "/swbadmin/images/defaultPhoto.jpg";
            boolean isStrategy = false;
            if (paramRequest.getCallMethod() == paramRequest.Call_STRATEGY)
            {
                isStrategy = true;
            }
            int contTot = 0;
            boolean hasRequest = false;
            Iterator<FriendshipProspect> itFriendshipProspect = FriendshipProspect.listFriendshipProspectByFriendShipRequester(owner, wpage.getWebSite());
            if (itFriendshipProspect.hasNext())
            {
                hasRequest = true;
            }
            if (hasRequest)
            {
%>
<div class="miembros">
    <p class="addOn">Mis solicitudes</p>
    <%
                itFriendshipProspect = FriendshipProspect.listFriendshipProspectByFriendShipRequester(owner, wpage.getWebSite());
                while (itFriendshipProspect.hasNext())
                {
                    FriendshipProspect friendshipProspect = itFriendshipProspect.next();
                    User userRequested = friendshipProspect.getFriendShipRequested();
                    if (userRequested.getPhoto() != null)
                    {
                        photo = SWBPlatform.getWebWorkPath() + userRequested.getPhoto();
                    }
                    urlAction.setParameter("user", userRequested.getURI());
                    if (!isStrategy)
                    {
    %>
    <div class="moreUser">
        <a href="<%=perfilPath%>?user=<%=userRequested.getEncodedURI()%>"><img src="<%=photo%>" alt="<%=userRequested.getFullName()%>" width="80" height="70">
            <br>
            <%=userRequested.getFullName()%>
        </a>
        <br>
        <%urlAction.setAction("removeRequest");%>
        <br>
        <div class="editarInfo"><p><a href="<%=urlAction%>">Eliminar solicitud</a></p></div>
    </div>
    <%
                    }
                    contTot++;
                }
                if (isStrategy && contTot > 0)
                {%>
    <div class="clear">
        <p class="titulo"><a href="<%=requestedPath%><%=userParam%>">Has solicidado a <%=contTot%> personas que se unan como tus amigos</a></p>
    </div>
    <%}
                else if (contTot == 0)
                {%>
    <p>No has solicitado personas que se unan a ti como amigos.</p>
    <%                }
    %>
</div>
<%
            }

%>


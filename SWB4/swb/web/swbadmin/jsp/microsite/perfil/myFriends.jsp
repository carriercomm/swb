<jsp:useBean id="paramRequest" scope="request" type="org.semanticwb.portal.api.SWBParamRequest"/>
<%@page import="org.semanticwb.model.*"%>
<%@page import="org.semanticwb.portal.community.*"%>
<%@page import="org.semanticwb.model.WebPage"%>
<%@page import="java.util.*"%>
<%@page import="org.semanticwb.SWBPortal"%>
<%@page import="org.semanticwb.platform.*"%>
<%@page import="org.semanticwb.platform.SemanticObject"%>
<%@page import="org.semanticwb.portal.api.SWBResourceURL"%>
<%!
    class GeoLocation
    {

        private double latitude;
        private double longitude;
        int step;
        String name;

        public GeoLocation(double latitude, double longitude, int step, String name)
        {
            this.latitude = latitude;
            this.longitude = longitude;
            this.step = step;
            this.name = name;
        }

        public double getLatitude()
        {
            return latitude;
        }

        public double getLongitude()
        {
            return longitude;
        }

        public int getStep()
        {
            return step;
        }

        public String getName()
        {
            return name;
        }
    }
%>
<%!    private static final int ELEMENETS_BY_PAGE = 5;
%>
<%
            String perfilPath = paramRequest.getWebPage().getWebSite().getWebPage("perfil").getUrl();
            String friendsPath = paramRequest.getWebPage().getWebSite().getWebPage("Amigos").getUrl();
            String cssPath = SWBPortal.getWebWorkPath() + "/models/" + paramRequest.getWebPage().getWebSiteId() + "/css/images/";
            String path = SWBPortal.getWebWorkPath() + "/models/" + paramRequest.getWebPage().getWebSite().getId() + "/css/images/";
            User owner = paramRequest.getUser();
            User user = owner;
            if (request.getParameter("user") != null)
            {
                SemanticObject semObj = SemanticObject.createSemanticObject(request.getParameter("user"));
                user = (User) semObj.createGenericInstance();
            }
            if (!user.isRegistered())
            {
                return;
            }
            List<GeoLocation> lista = new ArrayList<GeoLocation>();
            WebPage wpage = paramRequest.getWebPage();
            String photo = SWBPortal.getContextPath() + "/swbadmin/jsp/microsite/perfil/profilePlaceholder.jpg";

            ArrayList<User> elements = new ArrayList<User>();
            Iterator<Friendship> it = Friendship.ClassMgr.listFriendshipByFriend(user, wpage.getWebSite());
            while (it.hasNext())
            {
                Friendship friendShip = it.next();
                Iterator<User> itfriendUser = friendShip.listFriends();
                while (itfriendUser.hasNext())
                {
                    User friendUser = itfriendUser.next();
                    if (!friendUser.getURI().equals(user.getURI()))
                    {
                        elements.add(friendUser);
                    }
                }
            }
            int paginas = elements.size() / ELEMENETS_BY_PAGE;
            if (elements.size() % ELEMENETS_BY_PAGE != 0)
            {
                paginas++;
            }
            int inicio = 0;
            int fin = ELEMENETS_BY_PAGE;
            int ipage = 1;
            if (request.getParameter("ipage") != null)
            {
                try
                {
                    ipage = Integer.parseInt(request.getParameter("ipage"));
                    inicio = (ipage * ELEMENETS_BY_PAGE) - ELEMENETS_BY_PAGE;
                    fin = (ipage * ELEMENETS_BY_PAGE);
                }
                catch (NumberFormatException nfe)
                {
                    ipage = 1;
                }
            }
            if (ipage < 1 || ipage > paginas)
            {
                ipage = 1;
            }
            if (inicio < 0)
            {
                inicio = 0;
            }
            if (fin < 0)
            {
                fin = ELEMENETS_BY_PAGE;
            }
            if (fin > elements.size())
            {
                fin = elements.size();
            }
            if (inicio > fin)
            {
                inicio = 0;
                fin = ELEMENETS_BY_PAGE;
            }
            if (fin - inicio > ELEMENETS_BY_PAGE)
            {
                inicio = 0;
                fin = ELEMENETS_BY_PAGE;
            }
            inicio++;
            if (paramRequest.getCallMethod() == paramRequest.Call_STRATEGY)
            {
                String imgSize = "width=\"80\" height=\"70\"";

                boolean isStrategy = false;
                isStrategy = true;
                if (paramRequest.getCallMethod() == paramRequest.Call_STRATEGY)
                {
                    imgSize = "width=\"39\" height=\"39\"";
                }
%>

<%
                if (owner != user)
                {
%>
<h2>Amigos de <%=user.getFirstName()%></h2>
<%
          }
          else
          {
%>
<h2>Mis amigos</h2>
<%           }
%>
<ul class="amigos">

    <%
                String firstName = "", lastName = "";
                int contTot = 0;

                Iterator<Friendship> itMyFriends = Friendship.ClassMgr.listFriendshipByFriend(user, wpage.getWebSite());
                while (itMyFriends.hasNext())
                {
                    Friendship friendShip = itMyFriends.next();
                    Iterator<User> itfriendUser = friendShip.listFriends();
                    while (itfriendUser.hasNext())
                    {
                        User friendUser = itfriendUser.next();
                        if (!friendUser.getURI().equals(user.getURI()))
                        {
                            if (friendUser.getPhoto() != null)
                            {
                                photo = SWBPortal.getWebWorkPath() +friendUser.getPhoto();
                            }
    %>
    <li>
        <a href="<%=perfilPath%>?user=<%=friendUser.getEncodedURI()%>"><img alt="Foto de <%=friendUser.getFullName()%>" src="<%=photo%>" <%=imgSize%> title="<%=friendUser.getFullName()%>">
            <%if (!isStrategy)
                           {%>
            <br>
            <%=firstName%>
            <%=lastName%>
            <%}%>
        </a>
    </li>
    <%
                            contTot++;
                            if (isStrategy && contTot == 18)
                            {
                                break;
                            }
                        }
                    }
                }
    %>
</ul>
<%
                if (isStrategy && contTot >= 18)
                {%>
<div class="clear">
    <p class="verTodos"><a href="<%=friendsPath%>" >Ver todos</a></p>
</div>
<%}
           else if (contTot == 0)
           {%>
<div class="clear">
    <p class="titulo"><%=user.getFirstName()%> a�n no tiene amigos &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p>
</div>
<%}%>
<div class="clear"></div>


<%
            }
            else
            {
%>
<!-- paginacion -->
<%
       if (paginas > 1)
       {
%>
<div id="paginacion">


    <%
            String nextURL = "#";
            String previusURL = "#";
            if (ipage < paginas)
            {
                nextURL = paramRequest.getWebPage().getUrl() + "?ipage=" + (ipage + 1) + "&user=" + user.getEncodedURI();;
            }
            if (ipage > 1)
            {
                previusURL = paramRequest.getWebPage().getUrl() + "?ipage=" + (ipage - 1) + "&user=" + user.getEncodedURI();;
            }
            if (ipage > 1)
            {
    %>
    <a href="<%=previusURL%>"><img src="<%=cssPath%>pageArrowLeft.gif" alt="anterior"></a>
        <%
            }
            for (int i = 1; i <= paginas; i++)
            {
        %>
    <a href="<%=wpage.getUrl()%>?ipage=<%=i%>&user=<%=user.getEncodedURI()%>"><%
                if (i == ipage)
                {
        %>
        <strong>
            <%                    }
            %>
            <%=i%>
            <%
                    if (i == ipage)
                    {
            %>
        </strong>
        <%                    }
        %></a>
    <%
            }
    %>


    <%
            if (ipage != paginas)
            {
    %>
    <a href="<%=nextURL%>"><img src="<%=cssPath%>pageArrowRight.gif" alt="siguiente"></a>
        <%
            }
        %>
</div>
<%
       }
%>
<!-- fin paginacion -->
<div id="friendCards">    
    <%
           GeoLocation userLoc = null;
           if (user.getSemanticObject().getDoubleProperty(Geolocalizable.swb_latitude) != 0D)
           {
               userLoc = new GeoLocation(
                       user.getSemanticObject().getDoubleProperty(Geolocalizable.swb_latitude),
                       user.getSemanticObject().getDoubleProperty(Geolocalizable.swb_longitude),
                       user.getSemanticObject().getIntProperty(Geolocalizable.swb_geoStep),
                       user.getFullName());
           }
           else
           {
               userLoc = new GeoLocation(22.99885, -101.77734, 4, user.getFullName());
           }
           HashMap<String, SemanticProperty> mapa = new HashMap<String, SemanticProperty>();
           Iterator<SemanticProperty> list = org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/community#_ExtendedAttributes").listProperties();
           while (list.hasNext())
           {
               SemanticProperty prop = list.next();
               mapa.put(prop.getName(), prop);
           }
           String perfilurl = paramRequest.getWebPage().getWebSite().getWebPage("perfil").getUrl();
           int iElement = 0;
           for (User friendUser : elements)
           {


               iElement++;
               if (iElement > fin)
               {
                   break;
               }
               if (iElement >= inicio && iElement <= fin)
               {
                   String usr_sex = (String) friendUser.getExtendedAttribute(mapa.get("userSex"));
                   Object usr_age = (Object) friendUser.getExtendedAttribute(mapa.get("userAge"));
                   if (null == usr_age)
                   {
                       usr_age = "";
                   }
                   if ("M".equals(usr_sex))
                   {
                       usr_sex = "Hombre";
                   }
                   if ("F".equals(usr_sex))
                   {
                       usr_sex = "Mujer";
                   }
                   String usr_status = (String) friendUser.getExtendedAttribute(mapa.get("userStatus"));
                   if (null == usr_status)
                   {
                       usr_status = "";
                   }
                   if (friendUser.getPhoto() != null)
                   {
                       photo = friendUser.getPhoto();
                   }
                   if (friendUser.getSemanticObject().getDoubleProperty(Geolocalizable.swb_latitude) != 0D)
                   {
                       lista.add(new GeoLocation(
                               friendUser.getSemanticObject().getDoubleProperty(Geolocalizable.swb_latitude),
                               friendUser.getSemanticObject().getDoubleProperty(Geolocalizable.swb_longitude),
                               friendUser.getSemanticObject().getIntProperty(Geolocalizable.swb_geoStep),
                               friendUser.getFullName()));
                   }
                   String urluser = java.net.URLEncoder.encode(friendUser.getURI());

                   String email = friendUser.getEmail();

    %>
    <div class="friendCard">
        <img class="profilePic" width="121" height="121" src="<%=SWBPortal.getWebWorkPath() + photo%>" alt="<%=friendUser.getFullName()%>">
        <div class="friendCardInfo">
            <a class="ico" href="mailto:<%=email%>"><img src="<%=path%>icoMail.png" alt="enviar un mensaje"></a>
            <a class="ico" href="<%=perfilurl%>?user=<%=urluser%>"><img src="<%=path%>icoUser.png" alt="ir al perfil"></a>                
            <div class="friendCardName">
                <p><%=friendUser.getFullName()%></p>
            </div>
            <p>Sexo:<%=usr_sex%></p>
            <p>Edad:<%=usr_age%></p>          
        </div>
    </div>
    <%
               }

           }

    %>
</div> 
<div class="clear">&nbsp;</div><h2>Ubicaci&oacute;n de mis amigos</h2>
<div id="map_canvas" style="width: 420px; height: 300px; float: left; margin-left:5px; margin-right:5px;margin-top:5px"></div>
<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=<%=SWBPortal.getEnv("key/gmap", "")%>"
type="text/javascript"></script>
<script type="text/javascript">

    function createMarker(map, point, name) {
        var amigo = new GMarker(point);
        GEvent.addListener(amigo, "click", function() {
            var myHtml = "<b>"+name+"</b>";
            map.openInfoWindowHtml(point, myHtml);
        });
        return amigo;
    }

    function initialize() {
        if (GBrowserIsCompatible()) {
            var map = new GMap2(document.getElementById("map_canvas"));
            map.addControl(new GSmallMapControl());
            map.addControl(new GMapTypeControl());
            var bounds = new GLatLngBounds();
            /*var center = new GLatLng(%=userLoc.getLatitude()%>, %=userLoc.getLongitude()%>);
        map.setCenter(center, %=userLoc.getStep()-2%>);
        var marker = new GMarker(center);
        GEvent.addListener(marker, "click", function() {
                var myHtml = "<b>%=userLoc.getName()%></b>";
                map.openInfoWindowHtml(center, myHtml);
            });
        map.addOverlay(marker);*/
    <%
           Iterator<GeoLocation> listit = lista.iterator();
           while (listit.hasNext())
           {
               GeoLocation actual = listit.next();
    %>
                var pointer = new GLatLng(<%=actual.getLatitude()%>, <%=actual.getLongitude()%>);
                bounds.extend(pointer);
                map.addOverlay(createMarker(map, pointer, '<%=actual.getName()%>'));
    <%
           }
    %>
                map.setCenter(bounds.getCenter());
                map.setZoom(map.getBoundsZoomLevel(bounds));
            }
        }
        initialize();

</script>
<%
            }
%>


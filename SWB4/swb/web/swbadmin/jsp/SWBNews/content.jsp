<%@page import="org.semanticwb.model.Country"%><%@page import="org.semanticwb.platform.SemanticObject"%><%@page import="org.semanticwb.servlet.SWBHttpServletResponseWrapper"%><%@page import="org.semanticwb.portal.api.SWBResource"%><%@page import="org.semanticwb.portal.api.SWBResourceURL"%><%@page import="org.semanticwb.SWBPortal"%><%@page import="org.semanticwb.*"%><%@page import="java.text.DateFormat"%><%@page import="java.util.Locale"%><%@page import="org.semanticwb.portal.resources.sem.news.*"%><%@page import="org.semanticwb.model.Resource"%><%@page import="java.util.*"%><%@page import="org.semanticwb.model.WebPage"%><%@page import="org.semanticwb.model.User"%><jsp:useBean id="paramRequest" scope="request" type="org.semanticwb.portal.api.SWBParamRequest"/><%!    private static String mensaje = "Noticias de ";
    private static String mesage = "News of ";
    private static String ultmsg = "Noticias del mes";
    private static String lastmsg = "Previous news";
    static String[] meses =
    {
        "Enero", "Febrero", "Marzo", "Abril", "Mayo",
        "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };
    static String[] months =
    {
        "January", "February", "March", "April", "May",
        "June", "July", "August", "September", "October", "November", "December"
    };

    private String getMonth(int month, User user)
    {
        String getMonth = meses[month];
        return getMonth;
    }

    class YearComparator implements Comparator<String>
    {

        public int compare(String o1, String o2)
        {
            Integer i1 = Integer.parseInt(o1);
            Integer i2 = Integer.parseInt(o2);
            return i2.compareTo(i1);
        }
    }

    class SWBNewContentComparator implements Comparator<SWBNewContent>
    {

        public int compare(SWBNewContent o1, SWBNewContent o2)
        {
            Date d1 = o1.getPublishDate();
            Date d2 = o2.getPublishDate();
            if (d1 == null)
            {
                d1 = o1.getResource().getCreated();
            }
            if (d2 == null)
            {
                d2 = o2.getResource().getCreated();
            }
            if (d1 != null && d2 != null)
            {
                return d2.compareTo(d1);
            }
            else
            {
                return o1.getResourceBase().getIndex() >= o2.getResourceBase().getIndex() ? 1 : -1;
            }
        }
    }
%><%
            if (request.getParameter("mode") != null && "strategy".equals(request.getParameter("mode")))
            {
%><jsp:include page="strategy.jsp" flush="true" /><%
                return;
            }
            if(request.getParameter("uri")!=null)
                {
                %><jsp:include page="shownew.jsp" flush="true" /><%
                return;
                }

            // muestra lista de noticias en listado
            String usrlanguage = paramRequest.getUser().getLanguage();
            Locale locale = new Locale(usrlanguage);
            Calendar calendar = Calendar.getInstance(locale);

            int currentMonth = calendar.get(Calendar.MONTH);

            DateFormat sdf = DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale(usrlanguage));
            int limit = 15;
            List<SWBNewContent> contents = (List<SWBNewContent>) request.getAttribute("news");
            Collections.sort(contents, new SWBNewContentComparator());
            if (contents != null && contents.size() > 0)
            {

                // muestra las 15 primeras noticias
                int inew = 0;
                for (SWBNewContent content : contents)
                {
                    inew++;
                    //SWBResourceURL url = paramRequest.getRenderUrl();
                    //url.setMode(paramRequest.Mode_VIEW);
                    //url.setParameter("uri", content.getResourceBase().getSemanticObject().getURI());
                    
                    //String url = paramRequest.getWebPage().getUrl() + "?uri=" + content.getResourceBase().getSemanticObject().getEncodedURI();
                    String url = paramRequest.getWebPage().getUrl() + "?uri=" + content.getResourceBase().getSemanticObject().getId();
                    String title = SWBUtils.TEXT.encodeExtendedCharacters(content.getResourceBase().getTitle(usrlanguage));
                    if (title != null && title.trim().equals(""))
                    {
                        title = SWBUtils.TEXT.encodeExtendedCharacters(content.getResourceBase().getTitle());
                    }
                    String date = "";
                    if (content.getPublishDate() != null)
                    {
                        date = sdf.format(content.getPublishDate());
                    }
                    String country = "";
                    if (content.getCountry() != null && content.getCountry().getDisplayTitle(usrlanguage) != null && !content.getCountry().getDisplayTitle(usrlanguage).equals(""))
                    {
                        country = "(" + SWBUtils.TEXT.encodeExtendedCharacters(content.getCountry().getDisplayTitle(usrlanguage)) + ")";
                    }
                    String originalTitle = "";
                    if (content.getOriginalTitle() != null)
                    {
                        originalTitle = SWBUtils.TEXT.encodeExtendedCharacters(content.getOriginalTitle());
                    }
                    String urlcontent = url.toString().replace("&", "&amp;");
                    String ago = "";
                    String source = content.getSource();
                    if (date != null && !date.trim().equals(""))
                    {
                        ago = SWBUtils.TEXT.getTimeAgo(content.getPublishDate(), usrlanguage);
                    }
                    User user = paramRequest.getUser();
                    String description = "";
                    if (content.getResourceBase().getDisplayDescription(user.getLanguage()) != null)
                    {
                        description = SWBUtils.TEXT.encodeExtendedCharacters(content.getResourceBase().getDisplayDescription(user.getLanguage()));
                        if (description != null && description.trim().equals(""))
                        {
                            description = SWBUtils.TEXT.encodeExtendedCharacters(content.getResourceBase().getDisplayDescription(user.getLanguage()));
                        }
                    }
                    String pathPhoto="/work/models/"+paramRequest.getWebPage().getWebSite().getId()+"/css/noticias_infotec.gif";
                    /*String pathPhoto = SWBPortal.getContextPath() + "/swbadmin/jsp/SWBNews/sinfoto.png";
                    String image = "";
                    if (content.getImage() != null)
                    {
                    image = content.getImage();
                    pathPhoto = SWBPortal.getWebWorkPath() + content.getSemanticObject().getWorkPath() + "/thmb_image_" + image;
                    }
                    else
                    {
                    pathPhoto=SWBPortal.getContextPath()+"/work/models/"+paramRequest.getWebPage().getWebSiteId()+"/css/noticias_infotec.gif";
                    }
                    String titleImage = title.replace('"', '\'');*/
                    String id=paramRequest.getWebPage().getId();
%>
<div class="entradaVideos">
    <div class="thumbVideo">
        <%
                   if (pathPhoto != null && !"Prensa".equals(id))
                    {


        %>
       <img width="120" height="120" alt="<%=title%>" src="<%=pathPhoto%>" />
        <%
                    }
        %>

    </div>
    <div class="infoVideo">
        <h3><%=title%><%
                            if (country != null && !country.equals(""))
                            {
            %>&nbsp;<%=country%><%
                                }
            %>
        </h3>
        <%
                            if (originalTitle != null && !originalTitle.trim().equals(""))
                            {
        %>
        <p><%=originalTitle%></p>
        <%
                            }
        %>
        <p><%=description%></p>
        <p class="fechaVideo">
            <%
                                if (date != null && !date.trim().equals(""))
                                {
            %>
            <%=date%> - <%=ago%>
            <%
                                }
            %>

        </p>
        <%
                            if (source != null)
                            {
                                if (content.getSourceURL() == null)
                                {

        %>
        <p>Fuente: <%=source%></p>
        <%
                                        }
                                        else
                                        {
                                            String urlsource = content.getSourceURL();
                                            urlsource = urlsource.replace("&", "&amp;");
        %>
        <p>Fuente: <a href="<%=urlsource%>"><%=source%></a></p>
        <%
                                }
                            }
        %>
        <p class="vermas"><a href="<%=urlcontent%>">Ver M�s</a></p>
    </div>
    <div class="clear">&nbsp;</div>
</div>
<%
                    if (inew >= limit)
                    {
                        break;
                    }
                }
                int icolumna=1;

%>
<ul id="col<%=icolumna%>">
    <%

                    int count = 0;

                    String[] years = SWBNews.getYears(contents);
                    for (String year : years)
                    {
                        int iyear = Integer.parseInt(year);

                        for (int month = 11; month >= 0; month--)
                        {
                            if (SWBNews.hasNews(contents, month, iyear))
                            {
                                count++;
                            }
                        }
                    }
                    int elementInColumn = count / 3;
                    if (elementInColumn == 0)
                    {
                        elementInColumn++;
                    }
                    
                    int ielement = 0;
                    years = SWBNews.getYears(contents);
                    ArrayList<String> ayears = new ArrayList<String>();
                    ayears.addAll(Arrays.asList(years));
                    Collections.sort(ayears, new YearComparator());
                    for (String year : ayears)
                    {
                        int iyear = Integer.parseInt(year);
                        for (int month = 11; month >= 0; month--)
                        {
                            if (SWBNews.hasNews(contents, month, iyear))
                            {
                                ielement++;
                                if (ielement > elementInColumn && icolumna!=3)
                                {
                                    icolumna++;
                                    ielement = 1;
    %>
</ul>
<ul id="col<%=icolumna%>">
    <%
                                }
                                    
                                    String titleMonth = " " + getMonth(month, paramRequest.getUser()) + " " + iyear;
                                    SWBResourceURL url = paramRequest.getRenderUrl();
                                    url.setMode("month");
                                    url.setParameter("month", String.valueOf(month));
                                    url.setParameter("year", year);
                                    String currentyear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                                    String urlcontent = url.toString().replace("&", "&amp;");
                                    if (currentMonth == month && currentyear.equals(year))
                                    {
    %>
    <li class="listaLinksMes"><a href="<%=urlcontent%>"><%=ultmsg%></a></li>
    <%
                                    }
                                    else
                                    {
    %>
    <li class="listaLinksMes"><a href="<%=urlcontent%>"><%=mensaje%><%=titleMonth%></a></li>
    <%
                                }

                            }
                        }
                    }
    %>
</ul>   
<%
            }
%>

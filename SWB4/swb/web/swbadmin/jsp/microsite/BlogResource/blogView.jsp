<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="org.semanticwb.platform.*,java.text.*,org.semanticwb.portal.api.*,org.semanticwb.portal.community.*,org.semanticwb.*,org.semanticwb.model.*,java.util.*"%>
<%
            String pathIamge = SWBPlatform.getWebWorkPath();
            SWBParamRequest paramRequest = (SWBParamRequest) request.getAttribute("paramRequest");
            User user = paramRequest.getUser();
            WebPage wpage = paramRequest.getWebPage();
            Blog blog = (Blog) request.getAttribute("blog");
            if(blog!=null)
            {
            Member member = Member.getMember(user, wpage);

           
           
    
            String defaultFormat = "dd 'de' MMMM  'del' yyyy";
            SimpleDateFormat iso8601dateFormat = new SimpleDateFormat(defaultFormat);
            String created = iso8601dateFormat.format(blog.getCreated()); //SWBUtils.TEXT.(blog.getCreated(), user.getLanguage());//iso8601dateFormat.format(blog.getCreated());
            boolean showbr=false;
            Iterator<PostElement> posts2 = blog.listPostElements();
            while (posts2.hasNext())
            {
                PostElement post = posts2.next();
                if(post.canView(member))
                {
                    showbr=true;
                    break;
                }
            }
            %>

            <hr>

            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>                        
                        <%-- <h1><%=blog.getTitle()%></h1> --%>&nbsp;
                    </td>
                    <td>
                        <%
                if(member.getAccessLevel()==member.LEVEL_OWNER)
                {
                    SWBResourceURL url=paramRequest.getRenderUrl();
                    url.setParameter("act", "edit");
                    url.setParameter("uri", blog.getURI());
                    url.setParameter("mode", "editblog");
                    %>
                    &nbsp;&nbsp;&nbsp;<div class="editarInfo"><p><a href="<%=url%>">Editar</a></p></div>
                    <%
                }
            %>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <p class="titulo">Blog creado el: <%=created%></p>
                    </td>
                </tr>
            </table>
            <%
            if(showbr)
            {
                %>
                 <hr><br>
                <%
            }
             if(member.canAdd())
            {
                %>
                <div class="editarInfo"><p><a href="<%=paramRequest.getRenderUrl().setParameter("act","add")%>">Agregar Entrada</a></p></div><hr>
                <%
            }
               %>
                  <table width="100%" cellpadding="2" cellspacing="2" border="0">
            <%
            Iterator<PostElement> posts = SWBComparator.sortByCreated(blog.listPostElements(),false);
            while (posts.hasNext())
            {
                PostElement post = posts.next();
                if(post.canView(member))
                {
                    String postAuthor = post.getCreator().getFullName();
                    String updated = SWBUtils.TEXT.getTimeAgo(post.getUpdated(), user.getLanguage());
                    SWBResourceURL url=paramRequest.getRenderUrl();
                    url.setParameter("act", "detail");
                    url.setParameter("uri", post.getURI());
                    %>

                    <tr>
                        <td colspan="2" valign="top">
                        <%if(post.getCreator().getPhoto()!=null)
                        {
                            String src=pathIamge+post.getCreator().getPhoto();
                            %>
                            <p><img width="50" height="50" alt="<%=postAuthor%>" src="<%=src%>">&nbsp;&nbsp;&nbsp;Escrito por: <%=postAuthor%>, <%=updated%> , visitas: <%=post.getViews()%> , calificación: <%=post.getRank()%></p>                             
                          <%
                       }
                       else
                       {
                          %>
                            <p>Escrito por: <%=postAuthor%>, <%=updated%> , visitas: <%=post.getViews()%> , calificación: <%=post.getRank()%></p>
                          <%
                       }
                    %>
                    </td>
                    <td>
                         <%
                        if (post.canModify(member))
                        {
                            SWBResourceURL sWBResourceURL=paramRequest.getRenderUrl();
                            sWBResourceURL.setParameter("act", "edit");
                            sWBResourceURL.setParameter("uri",post.getURI());
                            sWBResourceURL.setParameter("mode","editpost");

                            SWBResourceURL removeUrl=paramRequest.getActionUrl();
                            removeUrl.setParameter("act", "remove");
                            %>
                            &nbsp;&nbsp;&nbsp;<div class="editarInfo"><p><a href="<%=sWBResourceURL%>">Editar</a></p></div>
                            &nbsp;&nbsp;&nbsp;<div class="editarInfo"><p><a href="javascript:validateremove('<%=removeUrl%>','<%=post.getTitle()%>','<%=post.getURI()%>')">Borrar</a><p></div>
                            <script language="Javascript" type="text/javascript">
                                function validateremove(url, title,uri)
                                {
                                    if(confirm('¿Esta seguro de borrar la entrada '+title+'?'))
                                        {
                                            var url=url+'&uri='+escape(uri);
                                            document.URL=url;
                                        }
                                }
                            </script>
                            <%
                        }
                    %>
                    </td>
                    </tr>
                        <tr>
                        <td>
                            <p class="titutlo"><%=post.getTitle()%></p>
                        </td>                                                
                    </tr>
                    <tr>
                        <td colspan="3">
                            <p class="vermas"><a href="<%=url%>" >Ver m&aacute;s</a></p>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3">
                            <hr>
                        </td>
                    </tr>

                    <%
                }
            }
%>
</table>

                    <%
                    if(showbr)
                    {
                        %>
                         <hr>
                        <%
                    }
                    else
                    {
                        %>
                        <hr><br><p>No hay entradas visibles para usted</p>
                        <%
                    }
            }
                    %>



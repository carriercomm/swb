<%@page contentType="text/html"%>
<%@page import="java.text.SimpleDateFormat, org.semanticwb.platform.*,org.semanticwb.portal.api.*,org.semanticwb.portal.community.*,org.semanticwb.*,org.semanticwb.model.*,java.util.*"%>
<%
    SWBParamRequest paramRequest = (SWBParamRequest) request.getAttribute("paramRequest");
    Resource base = paramRequest.getResourceBase();
    User user = paramRequest.getUser();
    WebPage wpage = paramRequest.getWebPage();
    Member member = Member.getMember(user, wpage);
    %>
    <%
    String uri = request.getParameter("uri");
    NewsElement rec = (NewsElement) SemanticObject.createSemanticObject(uri).createGenericInstance();
    if (rec == null) {
%>
        Error: Elemento no encontrado...
<%
        return;
    }
    if(!rec.canModify(member))
    {
        return;
    }
%>
<script type="text/javascript">
    dojo.require("dijit.form.TextBox");
    dojo.require("dijit.form.Textarea");
    dojo.require("dijit.form.DateTextBox");
    dojo.require("dijit.form.TimeTextBox");
    dojo.require("dijit.form.RadioButton");
    dojo.require("dijit.form.Button");
    dojo.require("dojo.parser");

    function validaForma()
    {
        var new_title = document.frmaeditnews.new_title.value;
        if(!new_title)
        {
            alert('�Debe ingresar el t�tulo de la noticia!');
            document.frmaeditnews.new_title.focus();
            return;
        }
        var new_author = document.frmaeditnews.new_author.value;
        if(!new_author)
        {
            alert('�Debe ingresar el autor de la noticia!');
            document.frmaeditnews.new_author.focus();
            return;
        }
        var new_abstract = document.frmaeditnews.new_abstract.value;
        if(!new_abstract)
        {
            alert('�Debe ingresar el resumen de la noticia!');
            document.frmaeditnews.new_abstract.focus();
            return;
        }
        var new_fulltext = document.frmaeditnews.new_fulltext.value;
        if(!new_fulltext)
        {
            alert('�Debe ingresar el texto de la noticia!');
            document.frmaeditnews.new_fulltext.focus();
            return;
        }
        var new_citation = document.frmaeditnews.new_citation.value;
        if(!new_citation)
        {
            alert('�Debe ingresar la fuente de la noticia!');
            document.frmaeditnews.new_citation.focus();
            return;
        }
        document.frmaeditnews.submit();
    }
</script>
<div class="columnaIzquierda">
<form name="frmaeditnews" id="frmaeditnews" class="swbform" enctype="multipart/form-data" method="post" action="<%=paramRequest.getActionUrl()%>">
    <div>
        <fieldset>
        <legend>Editar noticia</legend>
        <div>
            <p>
                <label for="new_image">Imagen de la noticia:&nbsp;</label>
                <a href="<%= SWBPortal.getWebWorkPath()+rec.getNewsImage()%>" target="_self">
                    <img id="img_<%=rec.getId()%>" src="<%= SWBPortal.getWebWorkPath()+rec.getNewsImage() %>" alt="<%= rec.getTitle() %>" border="0" />
                </a><br />
                <input type="file" id="foto" name="foto" size="45" />
            </p>
            <p>
                <label for="new_title">T�tulo de la noticia:&nbsp;</label><br />
                <input type="text" id="new_title" name="new_title" value="<%=(rec.getTitle()==null?"":rec.getTitle())%>" maxlength="70" size="45" />
            </p>
            <p>
                <label for="new_author">Autor de la noticia:&nbsp;</label><br />
                <input type="text" id="new_author" name="new_author" value="<%=(rec.getAuthor()==null?"":rec.getAuthor())%>" maxlength="50" size="45" />
            </p>
            <p>
                <label for="new_abstract">Resumen de la noticia:&nbsp;</label><br />
                <textarea id="new_abstract" name="new_abstract" cols="45" rows="2"><%=(rec.getDescription()==null?"":rec.getDescription())%></textarea>
            </p>
            <p>
                <label for="new_fulltext">Texto completo:&nbsp;</label><br />
                <textarea id="new_fulltext" name="new_fulltext" cols="45" rows="6"><%=(rec.getFullText()==null?"":rec.getFullText())%></textarea>
            </p>
            <p>
                <label for="new_citation">Fuente:&nbsp;</label><br />
                <input type="text" id="new_citation" name="new_citation" value="<%=(rec.getCitation()==null?"":rec.getCitation())%>" maxlength="50" size="45" />
            </p>
            <p>
                <label for="new_tags">Etiquetas:&nbsp;</label><br />
                <input type="text" id="new_tags" name="new_tags" value="<%=(rec.getTags()==null?"":rec.getTags())%>" maxlength="50" size="45" />
            </p>
        </div>
        </fieldset>
        <fieldset>
            <legend>�Qui�n puede ver este evento?</legend>
            <%String chk = "checked=\"checked\"";%>
            <div>
                <p>
                    <label for="level"><input type="radio" name="level" value="0" <%if(rec.getVisibility()==0)out.println(chk);%> />&nbsp;Cualquiera</label>
                </p>
                <p>
                    <label for="level"><input type="radio" name="level" value="1" <%if(rec.getVisibility()==1)out.println(chk);%> />&nbsp;S�lo los miembros</label>
                </p>
                <p>
                    <label for="level"><input type="radio" name="level" value="3" <%if(rec.getVisibility()==3)out.println(chk);%> />&nbsp;S�lo yo</label>
                </p>
            </div>
        </fieldset>
        <fieldset>
            <legend></legend>
            <div>
            <p>
                <div class="editarInfo"><p><a onclick="validaForma()" href="#">[Guardar]</a></p></div>
                <div class="editarInfo"><p><a href="<%=paramRequest.getRenderUrl()%>">[Cancelar]</a></p></div>
            </p>
            </div>
        </fieldset>
    </div>
    <input type="hidden" name="uri" value="<%=rec.getURI()%>"/>
    <input type="hidden" name="act" value="edit"/>
</form>
</div>
<div class="columnaCentro">

</div>
<script type="text/javascript">
    var img = document.getElementById('img_<%=rec.getId()%>');
    if( img.width>img.height && img.width>350) {
        img.width = 350;
        img.height = 270;
    }else {
        if(img.height>270) {
            img.width = 270;
            img.height = 350;
        }
    }
</script>

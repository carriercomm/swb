<%-- 
    Document   : reporter.jso
    Created on : 09-oct-2013, 10:41:08
    Author     : jorge.jimenez
--%>
<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@page import="org.semanticwb.social.*"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.semanticwb.SWBUtils"%>
<%@page import="org.semanticwb.model.*"%>
<%@page import="org.semanticwb.SWBPlatform"%>
<%@page import="org.semanticwb.SWBPortal"%> 
<%@page import="org.semanticwb.portal.api.*"%>
<%@page import="java.util.*"%>
<%@page import="org.semanticwb.social.util.*"%>
<jsp:useBean id="paramRequest" scope="request" type="org.semanticwb.portal.api.SWBParamRequest"/>
<%@page import="java.net.URLEncoder"%>
<%@page import="org.semanticwb.model.Descriptiveable"%>
<%@page import="org.semanticwb.platform.SemanticObject"%>
<%@page import="org.semanticwb.portal.api.SWBResourceURL"%>

<%
    /*
    Enumeration enParamNames=request.getParameterNames();
    while(enParamNames.hasMoreElements())
    {
        String paramName=(String)enParamNames.nextElement(); 
        System.out.println("paramName:"+paramName+",value:"+request.getParameter(paramName));
    }*/

    String suri=request.getParameter("suri");
    if(suri==null) return;
    SemanticObject semObj=SemanticObject.getSemanticObject(suri);
    if(semObj==null) return;
    User user=paramRequest.getUser();
    String title = "";
    if (semObj.getGenericInstance() instanceof Descriptiveable) {
        title = ((Descriptiveable) semObj.getGenericInstance()).getDisplayTitle(user.getLanguage());
    }
    SWBResourceURL url=paramRequest.getRenderUrl();
    url.setParameter("suri", suri);
    url.setParameter("doView", "1");
    url.setAction("showChart");
    String gender="";
    String schoolGrade="";
    String slifeStage="";
    String sentimentalRelationShip="";
    String scountryState="";
    if(paramRequest.getAction()!=null && paramRequest.getAction().equals("showChart"))
    {
        gender=request.getParameter("gender");
        schoolGrade=request.getParameter("schoolGrade");
        slifeStage=request.getParameter("lifeStage");
        sentimentalRelationShip=request.getParameter("sentimentalRelationShip");
        scountryState=request.getParameter("countryState"); 
    }        
%>
<h1><%=SWBSocialUtil.Util.getStringFromGenericLocale("sentimentProm", user.getLanguage())%>: <%=title%></h1>
 
<div class="swbform">
<form id="<%=semObj.getSemanticClass().getClassId()%>/reporterFilter" dojoType="dijit.form.Form" class="swbform" method="post" action="<%=url%>" method="post" onsubmit="submitForm('<%=semObj.getSemanticClass().getClassId()%>/reporterFilter');return false;"> 
<div class="combosFilter">
<p>Genero</p>
<select name="gender">
    <option value="all">Todos</option>
    <option value="<%=SocialNetworkUser.USER_GENDER_MALE%>" <%=gender.equals(""+SocialNetworkUser.USER_GENDER_MALE)?"selected":""%>>Hombre</option> 
    <option value="<%=SocialNetworkUser.USER_GENDER_FEMALE%>" <%=gender.equals(""+SocialNetworkUser.USER_GENDER_FEMALE)?"selected":""%>>Mujer</option>
    <option value="<%=SocialNetworkUser.USER_GENDER_UNDEFINED%>" <%=gender.equals(""+SocialNetworkUser.USER_GENDER_UNDEFINED)?"selected":""%>>Indefinido</option>
</select>
<p>Estudios</p>
<select name="schoolGrade">
    <option value="all">Todos</option>
    <option value="<%=SocialNetworkUser.USER_EDUCATION_HIGHSCHOOL%>" <%=schoolGrade.equals(""+SocialNetworkUser.USER_EDUCATION_HIGHSCHOOL)?"selected":""%>>Secundaria, Preparatoria</option>
    <option value="<%=SocialNetworkUser.USER_EDUCATION_COLLEGE%>" <%=schoolGrade.equals(""+SocialNetworkUser.USER_EDUCATION_COLLEGE)?"selected":""%>>Universidad</option>
    <option value="<%=SocialNetworkUser.USER_EDUCATION_GRADUATE%>" <%=schoolGrade.equals(""+SocialNetworkUser.USER_EDUCATION_GRADUATE)?"selected":""%>>PostGrado</option>
    <option value="<%=SocialNetworkUser.USER_EDUCATION_UNDEFINED%>" <%=schoolGrade.equals(""+SocialNetworkUser.USER_EDUCATION_UNDEFINED)?"selected":""%>>Indefinido</option>
</select>
<p>Etapa</p>
<select name="lifeStage">
    <option value="all">Todos</option>
    <%
        Iterator<LifeStage> itLifeStages=SWBComparator.sortByCreated(LifeStage.ClassMgr.listLifeStages(SWBContext.getAdminWebSite()));
        while(itLifeStages.hasNext())
        {
            LifeStage lifeStage=itLifeStages.next();  
            %>
                <option value="<%=lifeStage.getId()%>" <%=slifeStage.equals(lifeStage.getId())?"selected":""%>><%=lifeStage.getTitle(user.getLanguage())%></option>
            <%
        }
    %>
</select>
<p>Estatus Sentimental</p>
<select name="sentimentalRelationShip">
    <option value="all">Todos</option>
    <option value="<%=SocialNetworkUser.USER_RELATION_SINGLE%>" <%=sentimentalRelationShip.equals(""+SocialNetworkUser.USER_RELATION_SINGLE)?"selected":""%>>Soltero</option>
    <option value="<%=SocialNetworkUser.USER_RELATION_MARRIED%>" <%=sentimentalRelationShip.equals(""+SocialNetworkUser.USER_RELATION_MARRIED)?"selected":""%>>Casado</option>
    <option value="<%=SocialNetworkUser.USER_RELATION_DIVORCED%>" <%=sentimentalRelationShip.equals(""+SocialNetworkUser.USER_RELATION_DIVORCED)?"selected":""%>>Divorciado</option>
    <option value="<%=SocialNetworkUser.USER_RELATION_WIDOWED%>" <%=sentimentalRelationShip.equals(""+SocialNetworkUser.USER_RELATION_WIDOWED)?"selected":""%>>Viudo</option>
    <option value="<%=SocialNetworkUser.USER_RELATION_UNDEFINED%>" <%=sentimentalRelationShip.equals(""+SocialNetworkUser.USER_RELATION_UNDEFINED)?"selected":""%>>Indefinido</option>
</select>
<p>Estado</p>
<select name="countryState">
    <option value="all">Todos</option>
    <%
        Iterator <org.semanticwb.social.Country> itCountries=org.semanticwb.social.Country.ClassMgr.listCountries(SWBContext.getAdminWebSite());
        while(itCountries.hasNext())
        {
            org.semanticwb.social.Country country=itCountries.next(); 
            Iterator<CountryState> itCountryStates=CountryState.ClassMgr.listCountryStateByCountry(country, SWBContext.getAdminWebSite());
            while(itCountryStates.hasNext())
            {
                CountryState countryState=itCountryStates.next();  
                %>
                <option value="<%=countryState.getId()%>" <%=scountryState.equals(countryState.getId())?"selected":""%>><%=country.getTitle()%>/<%=countryState.getTitle()%></option>
                <%
            }
        }
    %>    
</select>
     <p>
         <button dojoType="dijit.form.Button" type="submit"><%=SWBSocialUtil.Util.getStringFromGenericLocale("send", user.getLanguage())%></button>
     </p>
</div>
</form>
</div>
<%
    if(paramRequest.getAction().equals("showChart"))
    {
        String args = "?objUri=" + semObj.getEncodedURI();
        String lang = paramRequest.getUser().getLanguage();
        args += "&lang=" + lang;
        args+="&gender="+ gender;
        args+="&schoolGrade="+ schoolGrade;
        args+="&slifeStage="+ slifeStage;
        args+="&sentimentalRelationShip="+ sentimentalRelationShip;
        args+="&scountryState="+ scountryState;
        %>
        <script src="http://d3js.org/d3.v3.min.js"></script>
        <script>

        var width = 960,
        height = 500,
        radius = Math.min(width, height) / 2;

        var arc = d3.svg.arc()
        .outerRadius(radius - 10)
        .innerRadius(0);

        var pie = d3.layout.pie()
        .sort(null)
        .value(function(d) { return d.value2; });

        var svg = d3.select("body").append("svg")
        .attr("width", width)
        .attr("height", height)
        .append("g")
        .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

        d3.json("<%=SWBPlatform.getContextPath()%>/work/models/<%=SWBContext.getAdminWebSite().getId()%>/jsp/stream/reporterChartData.jsp<%=args%>", function(error, data) {

            var g = svg.selectAll(".arc")
            .data(pie(data))
            .enter().append("g")
            .attr("class", function(d) { return d.data.chartclass; }); 

            g.append("path")
            .attr("d", arc)
            .style("fill", function(d) { return d.data.color; });

            g.append("text")
            .attr("transform", function(d) { return "translate(" + arc.centroid(d) + ")"; })
            .attr("dy", ".35em")
            .style("text-anchor", "middle")
            .text(function(d) { return d.data.label+"("+d.data.value1+"/"+d.data.value2+"%)" });

        });
           </script>
        <%
    }
%>
<%@page contentType="text/html"%><%@page import="org.semanticwb.opensocial.resources.*,org.semanticwb.opensocial.model.*,org.semanticwb.opensocial.resources.*,java.util.Date, java.util.Calendar, java.util.GregorianCalendar, java.text.SimpleDateFormat, org.semanticwb.portal.api.*,org.semanticwb.*,org.semanticwb.model.*,java.util.*"%><%

        String minChildWidth="150";
        String minChildWidthforCanvas="500";
        String nbZonesforCanvas="1";
        String nbZones="2";
        int iframewidth=300;
        int iframeheight=300;
        String dialogaddWidth="800";
        String dialogaddHeight="600";
        int iframewidthforCanvas=600;
        int iframeheightforCanvas=600;

        SWBParamRequest paramRequest = (SWBParamRequest) request.getAttribute("paramRequest");
        WebSite site=paramRequest.getWebPage().getWebSite();
        User user=paramRequest.getUser();
        SocialUser socialUser=SocialContainer.getSocialUser(user, session,site);
        SWBResourceURL add=paramRequest.getRenderUrl();
	add.setCallMethod(SWBResourceURL.Call_DIRECT);
        add.setMode(SocialContainer.Mode_LISTGADGETS);

        String lang=socialUser.getLanguage();
        String _country=socialUser.getCountry();
        SWBResourceURL script=paramRequest.getRenderUrl();
        script.setCallMethod(SWBResourceURL.Call_DIRECT);
        script.setMode(SocialContainer.Mode_JAVASCRIPT);
        script.setParameter("script", "core_rpc_pubsub_container.js");
        script.setParameter("debug", "0");
        String id=paramRequest.getUser().getId();


        SWBResourceURL metadata=paramRequest.getRenderUrl();
	metadata.setCallMethod(SWBResourceURL.Call_DIRECT);
        metadata.setMode(SocialContainer.Mode_METADATA);

        SWBResourceURL remove=paramRequest.getRenderUrl();
	remove.setCallMethod(SWBResourceURL.Call_DIRECT);
        remove.setMode(SocialContainer.Mode_SERVICECONTAINER);


        String defaultview="home";
        String context=SWBPortal.getContextPath();


        String moduleid=null;
        if(request.getParameter("mid")!=null && request.getParameter("view")!=null)
        {
            String _mid=request.getParameter("mid");
            String _view=request.getParameter("view");
            for(UserPrefs pref : socialUser.getUserPrefs())
            {
                Gadget g=pref.getGadget();
                if(g!=null && _mid.equalsIgnoreCase(pref.getModuleId()))
                {
                    for(View oview : g.getViews())
                    {
                        if(_view.equalsIgnoreCase(oview.getName()))
                        {
                            moduleid=_mid;
                            defaultview=_view;
                            minChildWidth=minChildWidthforCanvas;
                            nbZones=nbZonesforCanvas;
                        }
                    }
                }
            }
       }

        if(moduleid!=null)
        {
            iframewidth=iframewidthforCanvas;
            iframeheight=iframeheightforCanvas;
        }

%>
<html>
<head>
    <style type="text/css">
        iframe{width:<%=iframewidth-20%>px;height: <%=iframeheight-20%>px}
        .dndDropIndicator
    { border: 2px dashed #99BBE8; cursor:default; margin-bottom:5px; }
    </style>
<script type="text/javascript" >
        var djConfig = {
            parseOnLoad: true,
            isDebug: false,
            locale: 'en-us',
            extraLocale: ['ja-jp']
        };

    </script>


<link rel="stylesheet" type="text/css" media="all" href="<%=context%>/swbadmin/js/dojo/dijit/themes/soria/soria.css" >
<link rel="stylesheet" href="<%=context%>/swbadmin/dojo1_5/dojox/widget/Portlet/Portlet.css">
<link rel="stylesheet" href="<%=context%>/swbadmin/dojo1_5/dojox/layout/resources/GridContainer.css">
<link rel="stylesheet" href="<%=context%>/swbadmin/jsp/opensocial/gadgets.css">
<link rel="stylesheet" type="text/css" media="all" href="/swb/swbadmin/js/dojo/dijit/themes/soria/soria.css" >

<script type="text/javascript" >
 dojo.require("dijit.dijit");
 dojo.require("dojo.parser");
 dojo.require("dijit.Dialog");
 dojo.require("dojox.layout.GridContainer");
 dojo.require("dojox.widget.Portlet"); 
 dojo.addOnLoad(function(){
      dojo.parser.parse(); // or set djConfig.parseOnLoad = true
});



</script>
<script type="text/javascript" src="<%=script%>"></script>
<script type="text/javascript">
var baseUrl = document.location.href;
//var parentUrl=baseUrl;
var viewerId = '<%=id%>';
var ownerId = '<%=id%>';
var iframeBaseUrl = baseUrl.replace("localhost", "127.0.0.1");
<%
    StringBuilder _gadgets=new StringBuilder("[");


    //socialUser.clearUserPrefs(site);
    if(moduleid==null)
    {
        for(UserPrefs pref : socialUser.getUserPrefs())
        {
            Gadget g=pref.getGadget();
            if(g!=null)
            {
                g.reload();
                _gadgets.append("{url:\"");
                _gadgets.append(g.getUrl());
                _gadgets.append("\",moduleId:\"");
                _gadgets.append(pref.getModuleId());
                _gadgets.append("\"},");
            }
        }
        if(_gadgets.charAt(_gadgets.length()-1)==',')
        {
            _gadgets.deleteCharAt(_gadgets.length()-1);
        }


    }
    else
    {
        for(UserPrefs pref : socialUser.getUserPrefs())
        {
            Gadget g=pref.getGadget();
            if(g!=null && moduleid.equalsIgnoreCase(pref.getModuleId()))
            {
                g.reload();
                _gadgets.append("{url:\"");
                _gadgets.append(g.getUrl());
                _gadgets.append("\",moduleId:\"");
                _gadgets.append(pref.getModuleId());
                _gadgets.append("\"},");
            }
        }
        if(_gadgets.charAt(_gadgets.length()-1)==',')
        {
            _gadgets.deleteCharAt(_gadgets.length()-1);
        }
    }


    _gadgets.append("]");
%>
var _gadgets = <%=_gadgets%>;

function generateSecureToken(gadgetUrl,appId) {

    var fields = [ownerId, viewerId, appId, "shindig", gadgetUrl, "0", "default"];
    for (var i = 0; i < fields.length; i++) {
      // escape each field individually, for metachars in URL
      fields[i] = escape(fields[i]);
    }
    return fields.join(":");
  };


function sendRequestToServer(url, method, opt_postParams, opt_callback, opt_excludeSecurityToken) {
    // TODO: Should re-use the jsoncontainer code somehow
    opt_postParams = opt_postParams || {};

    var makeRequestParams = {
      "CONTENT_TYPE" : "JSON",
      "METHOD" : method,
      "POST_DATA" : opt_postParams};

    if (!opt_excludeSecurityToken) {
      url = socialDataPath + url + "?st=" + gadget.secureToken;
    }

    gadgets.io.makeNonProxiedRequest(url,
      function(data) {

        data = data.data;
        if (opt_callback) {
            opt_callback(data);
        }
      },
      makeRequestParams,
      "application/javascript"
    );
  };


function removeGadget(gadget) {
    var aleatorio = Math.round(Math.random()*99);
    var request = {'url':gadget.specUrl,'moduleid':gadget.moduleId,'service':'remove'};
    var remove='<%=remove%>?st='+aleatorio;
    sendRequestToServer(remove, "POST",gadgets.json.stringify(request), null,true);
  };

function requestGadgetMetaData(opt_callback) {


    var request = {
      context: {
        country: '<%=_country%>',
        language: '<%=lang%>',
        view: '<%=defaultview%>',
        container: "default"
      },
      gadgets: _gadgets
    };
    sendRequestToServer("<%=metadata%>", "POST",gadgets.json.stringify(request), opt_callback, true);
  };



function init() {
  shindig.container.layoutManager.setGadgetChromeIds(['gadget-chrome']);
  shindig.container.layoutManager =new shindig.DojoPorletManager('layout-root');
  shindig.container.setView('<%=defaultview%>');
  shindig.container.setLanguage('<%=lang%>');
  shindig.container.setCountry('<%=_country%>');
  requestGadgetMetaData(generateGadgets);

};
function generateGadgets(metadata)
{
    for (var i = 0; i < metadata.gadgets.length; i++)
    {
            var url=metadata.gadgets[i].url;
            var title=metadata.gadgets[i].title;
            var moduleId=metadata.gadgets[i].moduleId;
            var secureToken0=generateSecureToken(url);
            var gadget=shindig.container.createGadget({'id':moduleId,'title':title,'moduleId':moduleId,'secureToken':secureToken0,'specUrl': url,'userPrefs': metadata.gadgets[i].userPrefs});
            gadget.setWidth('<%=iframewidth%>');
            gadget.setHeight('<%=iframeheight%>');
            gadget.onClose=function()
            {
                var resp=confirm('�Desea eliminar el gadget '  + this.title +' permanentemente?');
                if(resp)
                {
                    var id_gadget=this.id;
                    id_gadget=id_gadget.substring(21);
                    var gadget_ = shindig.container.getGadget(id_gadget);
                    if(gadget_)
                    	removeGadget(gadget_);
                }
            };
            gadget.setServerBase(iframeBaseUrl);
            shindig.container.addGadget(gadget);
    }
    renderGadgets();
};
function renderGadgets() {
  shindig.container.renderGadgets();
};

</script>


</head>

<script type="text/javascript">
  function showDialogEmail()
  {      
        var edialog=dijit.byId('dialog');
        if(edialog)
        {
            edialog.show();
        }
  }


</script>
<body class="soria" onLoad="init();renderGadgets();">
    <%
    if(moduleid==null)
    {

        %>
        <div dojoType="dijit.Dialog" title="Agregar un gadget" id="dialog" style="width:<%=dialogaddWidth%>px;height:<%=dialogaddHeight%>px">
            <iframe id="iframeadd" src="<%=add%>" frameborder="0" style="width: <%=dialogaddWidth%>px;height:<%=dialogaddHeight%>px" scrolling="auto" width="100%" height="100%"></iframe>
        </div>
        <p><a href="#" onclick="showDialogEmail();">Agregar</a></p>
        <%
    }
%>



<div id="layout-root" class="gadgets-layout-root">
    <div dojoType="dojox.layout.GridContainer"
			id="grid"
			isAutoOrganized="false"
			hasResizableColumns="false"
			nbZones="<%=nbZones%>"
                        autoRefresh="false"
			dragHandleClass="dijitTitlePaneTitle"
			style="width:100%"
			doLayout="false"
	></div>
</div>
<%
    if(moduleid!=null)
    {
        String urclose=paramRequest.getWebPage().getUrl();
        %>
        <div><p><a href="<%=urclose%>">Cerrar</a></p></div>
        <%
    }
%>
</body>
</html>


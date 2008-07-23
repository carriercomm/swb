package org.semanticwb.model;

import org.semanticwb.SWBInstance;
import org.semanticwb.platform.SemanticVocabulary;
import org.semanticwb.platform.SemanticClass;
import org.semanticwb.platform.SemanticProperty;
import static org.semanticwb.platform.SemanticVocabulary.URI;

public class Vocabulary
{
    //Classes
    public static final SemanticClass User;
    public static final SemanticClass Calendar;
    public static final SemanticClass Resourceable;
    public static final SemanticClass Community;
    public static final SemanticClass TemplateRef;
    public static final SemanticClass Templateable;
    public static final SemanticClass Deleteable;
    public static final SemanticClass Content;
    public static final SemanticClass Reference;
    public static final SemanticClass Roleable;
    public static final SemanticClass HomePage;
    public static final SemanticClass RoleRefable;
    public static final SemanticClass Ruleable;
    public static final SemanticClass PFlow;
    public static final SemanticClass SWBModel;
    public static final SemanticClass RuleRefable;
    public static final SemanticClass Valueable;
    public static final SemanticClass Calendarable;
    public static final SemanticClass ipFilter;
    public static final SemanticClass Resource;
    public static final SemanticClass Permission;
    public static final SemanticClass TemplateRefable;
    public static final SemanticClass WebSiteable;
    public static final SemanticClass RuleRef;
    public static final SemanticClass Referensable;
    public static final SemanticClass Groupable;
    public static final SemanticClass Device;
    public static final SemanticClass ResourceType;
    public static final SemanticClass Localeable;
    public static final SemanticClass Application;
    public static final SemanticClass Camp;
    public static final SemanticClass Dns;
    public static final SemanticClass UserRepository;
    public static final SemanticClass Template;
    public static final SemanticClass Priorityable;
    public static final SemanticClass Role;
    public static final SemanticClass VersionInfo;
    public static final SemanticClass ResourceRef;
    public static final SemanticClass Descriptiveable;
    public static final SemanticClass Versionable;
    public static final SemanticClass RoleRef;
    public static final SemanticClass SWBClass;
    public static final SemanticClass System;
    public static final SemanticClass Rule;
    public static final SemanticClass Statusable;
    public static final SemanticClass WebPage;
    public static final SemanticClass WebPageable;
    public static final SemanticClass WebSite;
    public static final SemanticClass ObjectGroup;
    public static final SemanticClass Language;
    public static final SemanticClass SWBInterface;
    public static final SemanticClass Strategy;
    public static final SemanticClass ResourceRefable;

    //Properties
    public static final SemanticProperty hasUserReposotory;
    public static final SemanticProperty hasRole;
    public static final SemanticProperty hasGroup;
    public static final SemanticProperty status;
    public static final SemanticProperty hasReference;
    public static final SemanticProperty created;
    public static final SemanticProperty userModified;
    public static final SemanticProperty useWebSite;
    public static final SemanticProperty title;
    public static final SemanticProperty description;
    public static final SemanticProperty userCreated;
    public static final SemanticProperty updated;
    public static final SemanticProperty hasResource;
    public static final SemanticProperty hasTemplate;
    public static final SemanticProperty priority;
    public static final SemanticProperty deleted;
    public static final SemanticProperty hasRoleRef;
    public static final SemanticProperty useLanguage;
    public static final SemanticProperty actualVersion;
    public static final SemanticProperty lastVersion;
    public static final SemanticProperty hasRuleRef;
    public static final SemanticProperty hasCalendar;
    public static final SemanticProperty hasTemplateRef;
    public static final SemanticProperty hasResourceRef;
    public static final SemanticProperty isChildOf;
    public static final SemanticProperty hasRule;
    public static final SemanticProperty value;
    public static final SemanticProperty hasWebPage;
    public static final SemanticProperty nextVersion;
    public static final SemanticProperty versionComment;
    public static final SemanticProperty versionCreated;
    public static final SemanticProperty hasHome;


    static
    {
         SemanticVocabulary vocabulary=SWBInstance.getSemanticMgr().getVocabulary();
        // Classes
        User=vocabulary.getSemanticClass(URI+"User");
        Calendar=vocabulary.getSemanticClass(URI+"Calendar");
        Resourceable=vocabulary.getSemanticClass(URI+"Resourceable");
        Community=vocabulary.getSemanticClass(URI+"Community");
        TemplateRef=vocabulary.getSemanticClass(URI+"TemplateRef");
        Templateable=vocabulary.getSemanticClass(URI+"Templateable");
        Deleteable=vocabulary.getSemanticClass(URI+"Deleteable");
        Content=vocabulary.getSemanticClass(URI+"Content");
        Reference=vocabulary.getSemanticClass(URI+"Reference");
        Roleable=vocabulary.getSemanticClass(URI+"Roleable");
        HomePage=vocabulary.getSemanticClass(URI+"HomePage");
        RoleRefable=vocabulary.getSemanticClass(URI+"RoleRefable");
        Ruleable=vocabulary.getSemanticClass(URI+"Ruleable");
        PFlow=vocabulary.getSemanticClass(URI+"PFlow");
        SWBModel=vocabulary.getSemanticClass(URI+"SWBModel");
        RuleRefable=vocabulary.getSemanticClass(URI+"RuleRefable");
        Valueable=vocabulary.getSemanticClass(URI+"Valueable");
        Calendarable=vocabulary.getSemanticClass(URI+"Calendarable");
        ipFilter=vocabulary.getSemanticClass(URI+"ipFilter");
        Resource=vocabulary.getSemanticClass(URI+"Resource");
        Permission=vocabulary.getSemanticClass(URI+"Permission");
        TemplateRefable=vocabulary.getSemanticClass(URI+"TemplateRefable");
        WebSiteable=vocabulary.getSemanticClass(URI+"WebSiteable");
        RuleRef=vocabulary.getSemanticClass(URI+"RuleRef");
        Referensable=vocabulary.getSemanticClass(URI+"Referensable");
        Groupable=vocabulary.getSemanticClass(URI+"Groupable");
        Device=vocabulary.getSemanticClass(URI+"Device");
        ResourceType=vocabulary.getSemanticClass(URI+"ResourceType");
        Localeable=vocabulary.getSemanticClass(URI+"Localeable");
        Application=vocabulary.getSemanticClass(URI+"Application");
        Camp=vocabulary.getSemanticClass(URI+"Camp");
        Dns=vocabulary.getSemanticClass(URI+"Dns");
        UserRepository=vocabulary.getSemanticClass(URI+"UserRepository");
        Template=vocabulary.getSemanticClass(URI+"Template");
        Priorityable=vocabulary.getSemanticClass(URI+"Priorityable");
        Role=vocabulary.getSemanticClass(URI+"Role");
        VersionInfo=vocabulary.getSemanticClass(URI+"VersionInfo");
        ResourceRef=vocabulary.getSemanticClass(URI+"ResourceRef");
        Descriptiveable=vocabulary.getSemanticClass(URI+"Descriptiveable");
        Versionable=vocabulary.getSemanticClass(URI+"Versionable");
        RoleRef=vocabulary.getSemanticClass(URI+"RoleRef");
        SWBClass=vocabulary.getSemanticClass(URI+"SWBClass");
        System=vocabulary.getSemanticClass(URI+"System");
        Rule=vocabulary.getSemanticClass(URI+"Rule");
        Statusable=vocabulary.getSemanticClass(URI+"Statusable");
        WebPage=vocabulary.getSemanticClass(URI+"WebPage");
        WebPageable=vocabulary.getSemanticClass(URI+"WebPageable");
        WebSite=vocabulary.getSemanticClass(URI+"WebSite");
        ObjectGroup=vocabulary.getSemanticClass(URI+"ObjectGroup");
        Language=vocabulary.getSemanticClass(URI+"Language");
        SWBInterface=vocabulary.getSemanticClass(URI+"SWBInterface");
        Strategy=vocabulary.getSemanticClass(URI+"Strategy");
        ResourceRefable=vocabulary.getSemanticClass(URI+"ResourceRefable");

        //Properties
        hasUserReposotory=vocabulary.getSemanticProperty(URI+"hasUserReposotory");
        hasRole=vocabulary.getSemanticProperty(URI+"hasRole");
        hasGroup=vocabulary.getSemanticProperty(URI+"hasGroup");
        status=vocabulary.getSemanticProperty(URI+"status");
        hasReference=vocabulary.getSemanticProperty(URI+"hasReference");
        created=vocabulary.getSemanticProperty(URI+"created");
        userModified=vocabulary.getSemanticProperty(URI+"userModified");
        useWebSite=vocabulary.getSemanticProperty(URI+"useWebSite");
        title=vocabulary.getSemanticProperty(URI+"title");
        description=vocabulary.getSemanticProperty(URI+"description");
        userCreated=vocabulary.getSemanticProperty(URI+"userCreated");
        updated=vocabulary.getSemanticProperty(URI+"updated");
        hasResource=vocabulary.getSemanticProperty(URI+"hasResource");
        hasTemplate=vocabulary.getSemanticProperty(URI+"hasTemplate");
        priority=vocabulary.getSemanticProperty(URI+"priority");
        deleted=vocabulary.getSemanticProperty(URI+"deleted");
        hasRoleRef=vocabulary.getSemanticProperty(URI+"hasRoleRef");
        useLanguage=vocabulary.getSemanticProperty(URI+"useLanguage");
        actualVersion=vocabulary.getSemanticProperty(URI+"actualVersion");
        lastVersion=vocabulary.getSemanticProperty(URI+"lastVersion");
        hasRuleRef=vocabulary.getSemanticProperty(URI+"hasRuleRef");
        hasCalendar=vocabulary.getSemanticProperty(URI+"hasCalendar");
        hasTemplateRef=vocabulary.getSemanticProperty(URI+"hasTemplateRef");
        hasResourceRef=vocabulary.getSemanticProperty(URI+"hasResourceRef");
        isChildOf=vocabulary.getSemanticProperty(URI+"isChildOf");
        hasRule=vocabulary.getSemanticProperty(URI+"hasRule");
        value=vocabulary.getSemanticProperty(URI+"value");
        hasWebPage=vocabulary.getSemanticProperty(URI+"hasWebPage");
        nextVersion=vocabulary.getSemanticProperty(URI+"nextVersion");
        versionComment=vocabulary.getSemanticProperty(URI+"versionComment");
        versionCreated=vocabulary.getSemanticProperty(URI+"versionCreated");
        hasHome=vocabulary.getSemanticProperty(URI+"hasHome");
    }
}

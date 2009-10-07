package org.semanticwb.portal.community.base;


public class ClasifiedVehicleBase extends org.semanticwb.portal.community.ClasifiedBuySell implements org.semanticwb.model.Descriptiveable,org.semanticwb.model.Traceable,org.semanticwb.portal.community.Contactable
{
    public static final org.semanticwb.platform.SemanticProperty swbcomm_vehicleColor=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/community#vehicleColor");
    public static final org.semanticwb.platform.SemanticProperty swbcomm_vehicleMileage=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/community#vehicleMileage");
    public static final org.semanticwb.platform.SemanticProperty swbcomm_vehicleYear=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/community#vehicleYear");
    public static final org.semanticwb.platform.SemanticProperty swbcomm_vehicleModel=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/community#vehicleModel");
    public static final org.semanticwb.platform.SemanticProperty swbcomm_vehicleBrand=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/community#vehicleBrand");
    public static final org.semanticwb.platform.SemanticProperty swbcomm_vehicleType=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.semanticwebbuilder.org/swb4/community#vehicleType");
    public static final org.semanticwb.platform.SemanticClass swbcomm_ClasifiedVehicle=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/community#ClasifiedVehicle");
    public static final org.semanticwb.platform.SemanticClass sclass=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/community#ClasifiedVehicle");

    public ClasifiedVehicleBase(org.semanticwb.platform.SemanticObject base)
    {
        super(base);
    }

    public static java.util.Iterator<org.semanticwb.portal.community.ClasifiedVehicle> listClasifiedVehicles(org.semanticwb.model.SWBModel model)
    {
        java.util.Iterator it=model.getSemanticObject().getModel().listInstancesOfClass(sclass);
        return new org.semanticwb.model.GenericIterator<org.semanticwb.portal.community.ClasifiedVehicle>(it, true);
    }

    public static java.util.Iterator<org.semanticwb.portal.community.ClasifiedVehicle> listClasifiedVehicles()
    {
        java.util.Iterator it=sclass.listInstances();
        return new org.semanticwb.model.GenericIterator<org.semanticwb.portal.community.ClasifiedVehicle>(it, true);
    }

    public static org.semanticwb.portal.community.ClasifiedVehicle createClasifiedVehicle(org.semanticwb.model.SWBModel model)
    {
        long id=model.getSemanticObject().getModel().getCounter(sclass);
        return org.semanticwb.portal.community.ClasifiedVehicle.createClasifiedVehicle(String.valueOf(id), model);
    }

    public static org.semanticwb.portal.community.ClasifiedVehicle getClasifiedVehicle(String id, org.semanticwb.model.SWBModel model)
    {
        return (org.semanticwb.portal.community.ClasifiedVehicle)model.getSemanticObject().getModel().getGenericObject(model.getSemanticObject().getModel().getObjectUri(id,sclass),sclass);
    }

    public static org.semanticwb.portal.community.ClasifiedVehicle createClasifiedVehicle(String id, org.semanticwb.model.SWBModel model)
    {
        return (org.semanticwb.portal.community.ClasifiedVehicle)model.getSemanticObject().getModel().createGenericObject(model.getSemanticObject().getModel().getObjectUri(id, sclass), sclass);
    }

    public static void removeClasifiedVehicle(String id, org.semanticwb.model.SWBModel model)
    {
        model.getSemanticObject().getModel().removeSemanticObject(model.getSemanticObject().getModel().getObjectUri(id,sclass));
    }

    public static boolean hasClasifiedVehicle(String id, org.semanticwb.model.SWBModel model)
    {
        return (getClasifiedVehicle(id, model)!=null);
    }

    public String getColor()
    {
        return getSemanticObject().getProperty(swbcomm_vehicleColor);
    }

    public void setColor(String value)
    {
        getSemanticObject().setProperty(swbcomm_vehicleColor, value);
    }

    public int getMileage()
    {
        return getSemanticObject().getIntProperty(swbcomm_vehicleMileage);
    }

    public void setMileage(int value)
    {
        getSemanticObject().setIntProperty(swbcomm_vehicleMileage, value);
    }

    public int getVehicleYear()
    {
        return getSemanticObject().getIntProperty(swbcomm_vehicleYear);
    }

    public void setVehicleYear(int value)
    {
        getSemanticObject().setIntProperty(swbcomm_vehicleYear, value);
    }

    public String getModel()
    {
        return getSemanticObject().getProperty(swbcomm_vehicleModel);
    }

    public void setModel(String value)
    {
        getSemanticObject().setProperty(swbcomm_vehicleModel, value);
    }

    public String getBrand()
    {
        return getSemanticObject().getProperty(swbcomm_vehicleBrand);
    }

    public void setBrand(String value)
    {
        getSemanticObject().setProperty(swbcomm_vehicleBrand, value);
    }

    public int getVehicleType()
    {
        return getSemanticObject().getIntProperty(swbcomm_vehicleType);
    }

    public void setVehicleType(int value)
    {
        getSemanticObject().setIntProperty(swbcomm_vehicleType, value);
    }
}

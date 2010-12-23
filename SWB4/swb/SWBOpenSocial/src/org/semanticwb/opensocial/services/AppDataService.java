/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.semanticwb.opensocial.services;

import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.semanticwb.model.GenericIterator;
import org.semanticwb.model.WebSite;
import org.semanticwb.opensocial.model.Gadget;
import org.semanticwb.opensocial.model.data.Group;
import org.semanticwb.opensocial.model.data.Person;

/**
 *
 * @author victor.lorenzana
 */
public class AppDataService implements Service
{

    private String getData(String key, Person person,String appid)
    {
        GenericIterator<org.semanticwb.opensocial.model.data.AppData> data = person.listAppDatas();
        while (data.hasNext())
        {
            org.semanticwb.opensocial.model.data.AppData appData = data.next();
            if (key.equals(appData.getKey()) && appid.equals(appData.getAppid()))
            {
                return appData.getValue();
            }
        }
        return null;
    }

    public JSONObject get(String userid, JSONObject params, WebSite site,Gadget gadget)
    {
        JSONObject response = new JSONObject();
        try
        {
            ArrayList<Person> persons = new ArrayList<Person>();
            String groupId = params.getString("groupId").trim();
            Person personUserID = Person.ClassMgr.getPerson(userid, site);
            String appid=gadget.getId();
            
            if(params.optString("appId")!=null && !params.optString("appId").equals(""))
            {
                appid=params.optString("appId");
            }
            if (groupId.equals("@self"))
            {
                JSONObject responseKeys = new JSONObject();
                JSONArray keys = params.getJSONArray("keys");
                for (int i = 0; i < keys.length(); i++)
                {
                    String key = keys.getString(i);
                    String data = getData(key, personUserID,appid);
                    if (data != null)
                    {
                        responseKeys.accumulate(key, data);
                    }

                }
                response.put(personUserID.getId(), responseKeys);
                return response;
            }
            else if (groupId.equals("@all"))
            {
                Iterator<Group> groups = personUserID.listGroups();
                while (groups.hasNext())
                {
                    Group group = groups.next();
                    Iterator<Person> _persons = group.listPersons();
                    while (_persons.hasNext())
                    {
                        Person person = _persons.next();
                        persons.add(person);
                    }
                }
            }
            else
            {
                Iterator<Group> groups = personUserID.listGroups();
                while (groups.hasNext())
                {
                    Group group = groups.next();
                    if (group.getId().equals(groupId))
                    {
                        Iterator<Person> _persons = group.listPersons();
                        while (_persons.hasNext())
                        {
                            Person person = _persons.next();
                            persons.add(person);
                        }
                    }
                }
            }
            for (Person p : persons)
            {
                JSONObject responseKeys = new JSONObject();
                JSONArray keys = params.getJSONArray("keys");
                for (int i = 0; i < keys.length(); i++)
                {
                    String key = keys.getString(i);
                    String data = getData(key, p,appid);
                    if (data != null)
                    {
                        responseKeys.accumulate(key, data);
                    }

                }
                response.accumulate(p.getId(), responseKeys);
            }

        }
        catch (JSONException je)
        {
            je.printStackTrace();
        }
        return response;
    }

    public void update(String userid, JSONObject params, WebSite site,Gadget gadget)
    {

        try
        {
            ArrayList<Person> persons = new ArrayList<Person>();
            String groupId = params.getString("groupId").trim();
            String appid=gadget.getId();
            if(params.optString("appId")!=null && !params.optString("appId").equals(""))
            {
                appid=params.optString("appId");
            }
            Person personUserID = Person.ClassMgr.getPerson(userid, site);
            if (groupId.equals("@self"))
            {
                persons.add(personUserID);
            }
            else if (groupId.equals("@all"))
            {
                Iterator<Group> groups = personUserID.listGroups();
                while (groups.hasNext())
                {
                    Group group = groups.next();
                    Iterator<Person> _persons = group.listPersons();
                    while (_persons.hasNext())
                    {
                        Person person = _persons.next();
                        persons.add(person);
                    }
                }
            }
            else
            {
                Iterator<Group> groups = personUserID.listGroups();
                while (groups.hasNext())
                {
                    Group group = groups.next();
                    if (group.getId().equals(groupId))
                    {
                        Iterator<Person> _persons = group.listPersons();
                        while (_persons.hasNext())
                        {
                            Person person = _persons.next();
                            persons.add(person);
                        }
                    }
                }
            }

            for (Person p : persons)
            {
                // update data
                JSONObject data=params.getJSONObject("data");
                Iterator keys=data.keys();
                while(keys.hasNext())
                {
                    String key=keys.next().toString();
                    String value=data.get(key).toString();
                    if(getData(key, p,appid)==null)
                    {
                        org.semanticwb.opensocial.model.data.AppData appdata=org.semanticwb.opensocial.model.data.AppData.ClassMgr.createAppData(key, site);
                        appdata.setKey(key);
                        appdata.setValue(value);
                        appdata.setAppid(gadget.getId());
                        p.addAppData(appdata);
                    }
                    else
                    {
                        GenericIterator<org.semanticwb.opensocial.model.data.AppData> appdatas=p.listAppDatas();
                        while(appdatas.hasNext())
                        {
                            org.semanticwb.opensocial.model.data.AppData appdata=appdatas.next();
                            if(appdata.getKey().equals(key) && appid.equals(appdata.getAppid()))
                            {
                                appdata.setValue(value);                                
                                break;
                            }
                        }
                    }
                }
            }

        }
        catch (JSONException je)
        {
            je.printStackTrace();
        }

    }
}

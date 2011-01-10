/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.semanticwb.opensocial.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.semanticwb.model.WebSite;
import org.semanticwb.opensocial.model.Gadget;
import org.semanticwb.opensocial.model.data.Group;
import org.semanticwb.opensocial.model.data.Name;
import org.semanticwb.opensocial.model.data.Person;
import org.semanticwb.opensocial.util.HtmlScape;
import org.semanticwb.opensocial.util.NoneScape;
import org.semanticwb.opensocial.util.Scape;

/**
 *
 * @author victor.lorenzana
 */
public class PeopleService implements Service
{

    static
    {
        Iterator<Person> _persons = Person.ClassMgr.listPersons();
        while (_persons.hasNext())
        {
            Person person = _persons.next();
            person.remove();
        }

        Iterator<Group> groups = Group.ClassMgr.listGroups();
        while (groups.hasNext())
        {
            Group g = groups.next();
            g.remove();
        }
        WebSite site = WebSite.ClassMgr.getWebSite("reg_digital_demo");

        Person john_doe = Person.ClassMgr.createPerson("john.doe", site);
        Name name = Name.ClassMgr.createName(site);
        name.setFormatted("Demo Friend");
        john_doe.setName(name);
        john_doe.setAge(20);
        john_doe.setGender("male");
        john_doe.setProfileUrl("http://www.infotec");

        Group friendsOfGeorgeDoe = Group.ClassMgr.createGroup(john_doe.getId() + "@friends", site);
        john_doe.addGroup(friendsOfGeorgeDoe);
        friendsOfGeorgeDoe.setTitle("friends");
        friendsOfGeorgeDoe.setDescription("friends");



        Person george_doe = Person.ClassMgr.createPerson("George.doe", site);
        name = Name.ClassMgr.createName(site);
        name.setFormatted("George Doe");
        george_doe.setName(name);
        george_doe.setAge(20);
        george_doe.setGender("famale");
        george_doe.setProfileUrl("http://www.infotec");






        Person jane_doe = Person.ClassMgr.createPerson("jane.doe", site);
        name = Name.ClassMgr.createName(site);
        name.setFormatted("Jane Doe");
        jane_doe.setName(name);
        jane_doe.setAge(37);
        jane_doe.setGender("male");
        jane_doe.setProfileUrl("http://www.infotec");


        Person Maija = Person.ClassMgr.createPerson("maija", site);
        name = Name.ClassMgr.createName(site);
        name.setFormatted("Maija Meikäläinen");
        Maija.setName(name);
        Maija.setAge(37);
        Maija.setGender("male");
        Maija.setProfileUrl("http://www.infotec");

        //friend.setThumbnailUrl("a");
        friendsOfGeorgeDoe.addPerson(jane_doe);
        friendsOfGeorgeDoe.addPerson(Maija);
        friendsOfGeorgeDoe.addPerson(george_doe);



    }

    public void update(String userid, JSONObject params, WebSite site, Gadget gadget)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    class PersonComparator implements Comparator<Person>
    {

        private final String field;

        public PersonComparator(String field)
        {
            this.field = field;
        }

        public int compare(Person o1, Person o2)
        {
            if ("name".equals(field))
            {
                return o1.getName().getFormatted().compareTo(o2.getName().getFormatted());
            }
            return 0;
        }
    }

    public JSONObject get(String userid, JSONObject params, WebSite site, Gadget gadget)
    {


        JSONObject response = new JSONObject();
        JSONArray entries = new JSONArray();
        try
        {
            ArrayList<Person> persons = new ArrayList<Person>();
            response.put("entry", entries);
            Scape scape = new HtmlScape();
            String escapeType = "htmlEscape";
            if (params.optString("escapeType") != null && !params.optString("escapeType").equals(""))
            {
                escapeType = params.optString("escapeType");
            }
            if ("none".equals(escapeType))
            {
                scape = new NoneScape();
            }
            String groupId = params.getString("groupId").trim();
            Person personUserID = Person.ClassMgr.getPerson(userid, site);
            if (groupId.equals("@self")) //Defaults to "@self", which MUST return only the Person object(s) specified by the userId parameter
            {
                if (personUserID != null)
                {
                    JSONObject jsonperson = new JSONObject();
                    JSONArray fields = params.getJSONArray("fields");
                    for (int i = 0; i < fields.length(); i++)
                    {
                        String field = fields.getString(i);
                        jsonperson.put(field, personUserID.getValueFromField(field, scape));
                    }
                    return jsonperson;

                }
            }
            else if (groupId.equals("@friends"))
            {
                if (personUserID != null)
                {
                    Iterator<Group> groups = personUserID.listGroups();
                    while (groups.hasNext())
                    {
                        Group group = groups.next();
                        if ((personUserID.getId() + "@friends").equals(group.getId()))
                        {
                            Iterator<Person> _persons = group.listPersons();
                            while (_persons.hasNext())
                            {
                                Person person = _persons.next();
                                persons.add(person);
                            }
                            break;
                        }
                    }
                }
            }
            else if (groupId.equals("@all"))
            {
                if (personUserID != null)
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
            }
            else
            {

                if(personUserID!=null)
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
            }
            String sortBy = params.getString("sortBy");
            Person[] result = persons.toArray(new Person[persons.size()]);
            if (sortBy != null)
            {
                Arrays.sort(result, new PersonComparator(sortBy));
            }
            for (Person p : result)
            {
                JSONObject jsonperson = new JSONObject();
                JSONArray fields = params.getJSONArray("fields");
                for (int i = 0; i < fields.length(); i++)
                {
                    String field = fields.getString(i);
                    jsonperson.put(field, p.getValueFromField(field, scape));
                }
                entries.put(jsonperson);
            }


        }
        catch (JSONException jsone)
        {
            jsone.printStackTrace();
        }
        return response;
    }
    public void delete(String userid,JSONObject params,WebSite site,Gadget gadget)
    {
        
    }
}

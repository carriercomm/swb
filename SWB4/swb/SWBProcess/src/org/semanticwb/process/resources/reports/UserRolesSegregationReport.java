/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.semanticwb.process.resources.reports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.semanticwb.Logger;
import org.semanticwb.SWBUtils;
import org.semanticwb.model.Role;
import org.semanticwb.model.RoleRef;
import org.semanticwb.process.model.Process;
import org.semanticwb.model.User;
import org.semanticwb.model.UserRepository;
import org.semanticwb.model.WebSite;
import org.semanticwb.process.model.UserTask;

/**
 *
 * @author hasdai
 */
public class UserRolesSegregationReport {
    public static Logger log = SWBUtils.getLogger(UserRolesSegregationReport.class);
    private static Comparator<UserRolesSegregationBean> userNameComparator = new Comparator<UserRolesSegregationBean>() {
        @Override
        public int compare(UserRolesSegregationBean o1, UserRolesSegregationBean o2) {
            return o1.getUserName().compareTo(o2.getUserName());
        }
    }; 
    
    public UserRolesSegregationReport() {
    }
    
    public static ArrayList<UserRolesSegregationBean> generateBeans(WebSite site) {
        UserRepository userRep = site.getUserRepository();
        ArrayList<UserRolesSegregationBean> beans = new ArrayList<UserRolesSegregationBean>();
        
        HashMap<Role, ArrayList<UserTask>> taskMap = new HashMap<Role, ArrayList<UserTask>>();
        Iterator<UserTask> tasks = UserTask.ClassMgr.listUserTasks(site);
        while (tasks.hasNext()) {
            UserTask userTask = tasks.next();
            Process p = userTask.getProcess();
            if (p != null && p.isValid()) {
                Iterator<RoleRef> roleRefs = userTask.listRoleRefs();
                while (roleRefs.hasNext()) {
                    RoleRef roleRef = roleRefs.next();
                    Role role = roleRef.getRole();
                    if (role != null) {
                        if (taskMap.containsKey(role)) {
                            ArrayList<UserTask> tTasks = taskMap.get(role);
                            tTasks.add(userTask);
                            taskMap.put(role, tTasks);
                        } else {
                            ArrayList<UserTask> tTasks = new ArrayList<UserTask>();
                            tTasks.add(userTask);
                            taskMap.put(role, tTasks);
                        }
                    }
                }
            }
        }
        
        Iterator<User> users = userRep.listUsers();
        while (users.hasNext()) {
            User user = users.next();
            if (user.isActive() && user.listRoles().hasNext()) {
                Iterator<Role> roles = user.listRoles();
                while (roles.hasNext()) {
                    Role role = roles.next();
                    if (taskMap.get(role) != null) {
                        Iterator<UserTask> ittasks = taskMap.get(role).iterator();
                        while (ittasks.hasNext()) {
                            UserTask userTask = ittasks.next();
                            beans.add(new UserRolesSegregationBean(user.getFullName(), userTask.getProcess().getTitle(), userTask.getTitle(), role.getTitle()));
                        }
                    }
                }
            }
        }
        Collections.sort(beans, userNameComparator);
        return beans;
    }
    
    public static void generateReport(String templatePath, String outPath, ArrayList<UserRolesSegregationBean> beansList) {
//        Collection beans = new HashSet();
        Map mbeans = new HashMap();
//        Iterator<UserRolesSegregationBean> itbeans = beansList.iterator();
//        while (itbeans.hasNext()) {
//            UserRolesSegregationBean itbean = itbeans.next();
//            beans.add(new UserRolesSegregationBean(itbean.getUserName(), itbean.getProcessName(), itbean.getTaskName(), itbean.getRoleName()));
//        }
        
        mbeans.put("bean", beansList);
        
        XLSTransformer transformer = new XLSTransformer();
        try {
            try {
                transformer.transformXLS(templatePath, mbeans, outPath);
            } catch (InvalidFormatException ex) {
                log.error(ex);
            }
        } catch (ParsePropertyException ex) {
            log.error(ex);
        } catch (IOException ex) {
            log.error(ex);
        }
    }
}

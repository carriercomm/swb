/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.semanticwb.servlet.internal;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Jei
 */
public interface InternalServlet 
{
    public void init(ServletContext config);
    
    public void doProcess(HttpServletRequest request, HttpServletResponse response) throws IOException;
}

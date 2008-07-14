/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.semanticwb.xmlrpc;

import java.net.URI;

/**
 *
 * @author victor.lorenzana
 */
public class XmlRpcClientConfig {
    private URI ServerURI;
    private URI proxyServer;
    private int proxyPort;
    private String userName;
    private String password;
    public XmlRpcClientConfig(URI ServerURI)
    {
        this.ServerURI=ServerURI;
    }
    public XmlRpcClientConfig(URI ServerURI,URI proxyServer,int proxyPort)
    {
        this(ServerURI);
        this.proxyServer=proxyServer;
        this.proxyPort=proxyPort;
    }
    public XmlRpcClientConfig(URI ServerURI,URI proxyServer,int proxyPort,String user,String password)
    {
        this(ServerURI,proxyServer,proxyPort);
        this.userName=user;
        this.password=password;
    }
    public XmlRpcClientConfig(URI ServerURI,String user,String password)
    {        
        this(ServerURI);
        this.userName=user;
        this.password=password;
    }
    
    
    public boolean hasUserPassWord()
    {
        if(this.userName==null || password==null)
        {
            return false;
        }
        return true;
    }
    public String getUserName()
    {
        return userName;
    }
    public String getPassword()
    {
        return password;
    }
    public boolean usesProxyServer()
    {
        if(this.proxyServer()==null || proxyPort==0)
        {
            return false;
        }
        return true;
    }
    public URI proxyServer()
    {
        return proxyServer;    
    }
    public int getProxyPort()
    {
        return proxyPort;    
    }
    public URI getServerURI()
    {
        return ServerURI;
    }
    public void setServerURI(URI ServerURI)
    {
        this.ServerURI=ServerURI;
    }
}

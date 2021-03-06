/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.semanticwb.rest.consume;

import org.semanticwb.rest.util.HTTPMethod;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import org.semanticwb.Logger;
import org.semanticwb.SWBUtils;
import org.w3c.dom.Document;

/**
 *
 * @author victor.lorenzana
 */
public final class XWWWFormUrlEncoded extends RepresentationBase implements RepresentationRequest
{
    private static final Logger log = SWBUtils.getLogger(ApplicationXML.class);
    public static final String APPLICATION_XWWW_FORM_URL_ENCODED = "application/x-www-form-urlencoded";

    public XWWWFormUrlEncoded()
    {
    }
    
    private Parameter getDefinition(String name)
    {
        for (Parameter parameter : this.getAllParameters())
        {
            if (parameter.getName().equals(name))
            {
                return parameter;
            }
        }
        for (Parameter parameter : this.method.getAllParameters())
        {
            if (parameter.getName().equals(name))
            {
                return parameter;
            }
        }
        return null;
    }
    private String constructParametersToURL(List<ParameterValue> values) throws RestException
    {
        StringBuilder sb = new StringBuilder();
        for (ParameterValue pvalue : values)
        {
            Parameter definition = getDefinition(pvalue.getName());
            if (definition != null && "query".equals(definition.getStyle()))
            {
                try
                {
                    sb.append(pvalue.getName());
                    sb.append("=");
                    sb.append(URLEncoder.encode(pvalue.getValue().toString(), "utf-8"));
                    sb.append("&");
                }
                catch (Exception e)
                {
                    log.debug(e);
                    throw new RestException(e);
                }
            }

        }

        for (Parameter parameter : this.parameters)
        {
            if (parameter.isFixed())
            {
                try
                {
                    sb.append(parameter.getName());
                    sb.append("=");
                    sb.append(URLEncoder.encode(parameter.getFixedValue(), "utf-8"));
                    sb.append("&");
                }
                catch (Exception e)
                {
                    log.debug(e);
                    throw new RestException(e);
                }
            }
        }

        for (Parameter parameter : this.method.getAllParameters())
        {
            if (parameter.isFixed())
            {
                try
                {
                    sb.append(parameter.getName());
                    sb.append("=");
                    sb.append(URLEncoder.encode(parameter.getFixedValue(), "utf-8"));
                    sb.append("&");
                }
                catch (Exception e)
                {
                    log.debug(e);
                    throw new RestException(e);
                }
            }
        }


        /*try
        {
        for (Parameter parameter : this.getRequiredParameters())
        {
        for (ParameterValue pvalue : values)
        {
        if (pvalue.getName().equals(parameter.getName()))
        {
        sb.append(parameter.getName());
        sb.append("=");
        sb.append(URLEncoder.encode(pvalue.getValue().toString(), "utf-8"));
        sb.append("&");
        }
        }
        }
        for (Parameter parameter : this.getOptionalParameters())
        {
        for (ParameterValue pvalue : values)
        {
        if (pvalue.getName().equals(parameter.getName()))
        {
        sb.append(parameter.getName());
        sb.append("=");
        sb.append(URLEncoder.encode(pvalue.getValue().toString(), "utf-8"));
        sb.append("&");
        }
        }
        }
        for (Parameter parameter : this.parameters)
        {
        if (parameter.isFixed())
        {
        sb.append(parameter.getName());
        sb.append("=");
        sb.append(URLEncoder.encode(parameter.getFixedValue(), "utf-8"));
        sb.append("&");
        }
        }


        for (Parameter parameter : this.method.getRequiredParameters())
        {
        for (ParameterValue pvalue : values)
        {
        if (pvalue.getName().equals(parameter.getName()))
        {
        sb.append(parameter.getName());
        sb.append("=");
        sb.append(URLEncoder.encode(pvalue.getValue().toString(), "utf-8"));
        sb.append("&");
        }
        }
        }
        for (Parameter parameter : this.method.getOptionalParameters())
        {
        for (ParameterValue pvalue : values)
        {
        if (pvalue.getName().equals(parameter.getName()))
        {
        sb.append(parameter.getName());
        sb.append("=");
        sb.append(URLEncoder.encode(pvalue.getValue().toString(), "utf-8"));
        sb.append("&");
        }
        }
        }
        for (Parameter parameter : this.method.getAllParameters())
        {
        if (parameter.isFixed())
        {
        sb.append(parameter.getName());
        sb.append("=");
        sb.append(URLEncoder.encode(parameter.getFixedValue(), "utf-8"));
        sb.append("&");
        }
        }

        }
        catch (Exception e)
        {
        log.debug(e);
        throw new RestException(e);
        }*/
        return sb.toString();
    }
private void addHeaders(List<ParameterValue> values,HttpURLConnection con) throws RestException
    {
        for (ParameterValue pvalue : values)
        {
            Parameter definition = getDefinition(pvalue.getName());
            if (definition != null && "header".equals(definition.getStyle()))
            {
                try
                {
                    String key=pvalue.getName();
                    String value=pvalue.getValue().toString();
                    con.setRequestProperty(key, value);
                }
                catch (Exception e)
                {
                    log.debug(e);
                    throw new RestException(e);
                }
            }
        }
        for (Parameter parameter : this.parameters)
        {
            if (parameter.isFixed())
            {
                try
                {
                    String key=parameter.getName();
                    String value=parameter.getFixedValue();
                    con.setRequestProperty(key, value);
                }
                catch (Exception e)
                {
                    log.debug(e);
                    throw new RestException(e);
                }
            }
        }

        for (Parameter parameter : this.method.getAllParameters())
        {
            if (parameter.isFixed())
            {
                try
                {
                   String key=parameter.getName();
                   String value=parameter.getFixedValue();
                   con.setRequestProperty(key, value);
                }
                catch (Exception e)
                {
                    log.debug(e);
                    throw new RestException(e);
                }
            }
        }
    }
    public RepresentationResponse request(List<ParameterValue> values) throws RestException
    {
        checkParameters(values);
        URL url = this.getMethod().getResource().getPath();
        String _parameters = constructParametersToURL(values);
        try
        {
            if (this.getMethod().getHTTPMethod() == HTTPMethod.GET || this.getMethod().getHTTPMethod() == HTTPMethod.DELETE)
            {
                url = new URL(url.toString() + "?" + _parameters);
            }
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(this.method.getHTTPMethod().toString());
            addHeaders(values, con);
            if (this.getMethod().getHTTPMethod() == HTTPMethod.PUT || this.getMethod().getHTTPMethod() == HTTPMethod.POST)
            {
                con.setRequestProperty(CONTENT_TYPE, APPLICATION_XWWW_FORM_URL_ENCODED);
                con.setDoOutput(true);
                con.setDoInput(true);
                OutputStream out = con.getOutputStream();
                byte[] bparameters = _parameters.getBytes();
                out.write(bparameters);
                out.close();
            }
            return processResponse(con);

        }
        catch (Exception ioe)
        {
            throw new ExecutionRestException(this.getMethod().getHTTPMethod(), url, ioe);
        }
    }

    public void setMethod(Method method)
    {
        this.method = method;
    }

    public String getMediaType()
    {
        return APPLICATION_XWWW_FORM_URL_ENCODED;
    }

    public void addParameter(Parameter parameter)
    {
        this.parameters.add(parameter);
    }

    public RepresentationResponse request(List<ParameterValue> values, Document request) throws ExecutionRestException, RestException
    {
        return this.request(values);
    }
}

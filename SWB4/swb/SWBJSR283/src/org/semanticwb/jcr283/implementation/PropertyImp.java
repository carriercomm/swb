/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.semanticwb.jcr283.implementation;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import javax.jcr.Binary;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.PropertyDefinition;
import javax.jcr.version.VersionException;
import org.semanticwb.platform.SemanticProperty;

/**
 *
 * @author victor.lorenzana
 */
public class PropertyImp extends ItemImp implements Property
{

    private static final ValueFactoryImp valueFactoryImp = new ValueFactoryImp();
    private final PropertyDefinitionImp propertyDefinitionImp;
    ArrayList<Value> values = new ArrayList<Value>();

    public PropertyImp(SemanticProperty prop, NodeImp parent) throws RepositoryException
    {
        super(prop, parent);
        NodeTypeImp nodeType = NodeTypeManagerImp.loadNodeType(prop.getDomainClass());
        propertyDefinitionImp = new PropertyDefinitionImp(prop.getSemanticObject(), nodeType);
    }

    public void setValue(Value value) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException
    {
        setValue(new Value[]
                {
                    value
                });
    }

    private Value transformValue(Value value, int reqValue) throws ValueFormatException, RepositoryException
    {
        if (value.getType() != reqValue)
        {
            if (value.getType() == PropertyType.STRING || value.getType() == PropertyType.LONG || value.getType() == PropertyType.BOOLEAN || value.getType() == PropertyType.DECIMAL || value.getType() == PropertyType.DATE || value.getType() == PropertyType.DOUBLE || value.getType() == PropertyType.NAME || value.getType() == PropertyType.PATH || value.getType() == PropertyType.URI)
            {
                return valueFactoryImp.createValue(value.getString(), reqValue);
            }
            else
            {
                // TODO:
                throw new ValueFormatException("The value can not be transformed to a valid value");
            }
        }
        else
        {
            return value;
        }
    }

    public void setValue(Value[] values) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException
    {
        if (values.length > 1 && !propertyDefinitionImp.isMultiple())
        {
            throw new ConstraintViolationException("The property is not multiple");
        }
        int reqType = propertyDefinitionImp.getRequiredType();
        //Validate Values
        HashSet<Value> newValues = new HashSet<Value>();
        for (Value value : values)
        {
            newValues.add(transformValue(value, reqType));
        }
        this.values.addAll(newValues);
    }

    public void setValue(String value) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException
    {
        setValue(valueFactoryImp.createValue(value));
    }

    public void setValue(String[] values) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException
    {
        HashSet<Value> ovalues = new HashSet<Value>();
        for (String value : values)
        {
            ovalues.add(valueFactoryImp.createValue(value));
        }
        setValue(ovalues.toArray(new Value[ovalues.size()]));
    }

    public void setValue(InputStream value) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException
    {
        setValue(valueFactoryImp.createValue(value));
    }

    public void setValue(Binary value) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException
    {
        setValue(valueFactoryImp.createValue(value));
    }

    public void setValue(long value) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException
    {
        setValue(valueFactoryImp.createValue(value));
    }

    public void setValue(double value) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException
    {
        setValue(valueFactoryImp.createValue(value));
    }

    public void setValue(BigDecimal value) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException
    {
        setValue(valueFactoryImp.createValue(value));
    }

    public void setValue(Calendar value) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException
    {
        setValue(valueFactoryImp.createValue(value));
    }

    public void setValue(boolean value) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException
    {
        setValue(valueFactoryImp.createValue(value));
    }

    public void setValue(Node value) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException
    {
        setValue(valueFactoryImp.createValue(value));
    }

    public Value getValue() throws ValueFormatException, RepositoryException
    {
        if (this.isMultiple())
        {
            throw new ValueFormatException("The property is multivalued");
        }
        return getCopy(values.get(0));
    }

    public Value[] getValues() throws ValueFormatException, RepositoryException
    {
        HashSet<Value> ovalues = new HashSet<Value>();
        for (Value value : values)
        {
            ovalues.add(getCopy(value));
        }
        return ovalues.toArray(new Value[ovalues.size()]);
    }

    public String getString() throws ValueFormatException, RepositoryException
    {
        return this.getValue().getString();
    }

    public InputStream getStream() throws ValueFormatException, RepositoryException
    {
        return this.getValue().getStream();
    }

    public Binary getBinary() throws ValueFormatException, RepositoryException
    {
        return this.getValue().getBinary();
    }

    public long getLong() throws ValueFormatException, RepositoryException
    {
        return this.getValue().getLong();
    }

    public double getDouble() throws ValueFormatException, RepositoryException
    {
        return this.getValue().getDouble();
    }

    public BigDecimal getDecimal() throws ValueFormatException, RepositoryException
    {
        return this.getValue().getDecimal();
    }

    public Calendar getDate() throws ValueFormatException, RepositoryException
    {
        return this.getValue().getDate();
    }

    public boolean getBoolean() throws ValueFormatException, RepositoryException
    {
        return this.getValue().getBoolean();
    }

    public Node getNode() throws ItemNotFoundException, ValueFormatException, RepositoryException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Property getProperty() throws ItemNotFoundException, ValueFormatException, RepositoryException
    {
        if (this.isMultiple())
        {
            throw new ValueFormatException("The property is multivalued");
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getLength() throws ValueFormatException, RepositoryException
    {        
        if(this.isMultiple())
        {
            throw new ValueFormatException("The property is multivalued");
        }
        return getLength(values.get(0));
    }
    private long getLength(Value value)
    {
        return -1;
    }
    public long[] getLengths() throws ValueFormatException, RepositoryException
    {
        long[] getLengths=new long[values.size()];
        int index=0;
        for(Value value : values)
        {
            getLengths[index]=getLength(value);
            index++;
        }
        return getLengths;
    }

    public PropertyDefinition getDefinition() throws RepositoryException
    {
        return propertyDefinitionImp;
    }

    public int getType() throws RepositoryException
    {
        int type = PropertyType.UNDEFINED;
        if (values.size() > 0)
        {
            type = values.get(0).getType();
        }
        return type;
    }

    public boolean isMultiple() throws RepositoryException
    {
        return values.size() == 0 ? false : true;
    }

    public boolean isNode()
    {
        return false;
    }

    private Value getCopy(Value value)
    {
        // TODO:
        return value;
    }
}

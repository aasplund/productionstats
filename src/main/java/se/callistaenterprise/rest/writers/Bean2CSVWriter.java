package se.callistaenterprise.rest.writers;

import static org.apache.commons.beanutils.BeanUtils.getProperty;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static se.callistaenterprise.Utils.NEW_LINE;
import static se.callistaenterprise.Utils.QUOT;
import static se.callistaenterprise.Utils.getLast;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class Bean2CSVWriter {
    
    private List<String> properties;
    private static final char SEPARATOR = ',';

    public Bean2CSVWriter(List<String> properties) {
        this.properties = properties;
    }
    
    public void writeHeader(PrintStream out) {
        for (int i = 0; i < properties.size()-1; i++) {
            out.append(properties.get(i)).append(SEPARATOR);
        }
        out.append(getLast(properties)).append(NEW_LINE);
    }
    
    public void writeData(PrintStream out, Object bean) {
        try {
            String value = "";
            for (int i = 0; i < properties.size() - 1; i++) {
                String propName = properties.get(i);
                if(hasPropertey(bean, propName + "AsString")) {
                    value = getProperty(bean, propName + "AsString");
                } else {
                    value = getProperty(bean, propName);
                }
                out.append(QUOT)
                .append(defaultIfBlank(value,EMPTY))
                .append(QUOT).append(SEPARATOR);
            }
            if(hasPropertey(bean, getLast(properties) + "AsString")) {
                value = getProperty(bean, getLast(properties) + "AsString");
            } else {
                value = getProperty(bean, getLast(properties));
            }
            
            out.append(QUOT)
            .append(value)
            .append(QUOT).append(NEW_LINE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setProperties(List<String> properties) {
        this.properties = properties;
    }
    
    private static boolean hasPropertey(Object bean, String name) throws IllegalAccessException, InvocationTargetException {
        try {
            getProperty(bean, name);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}

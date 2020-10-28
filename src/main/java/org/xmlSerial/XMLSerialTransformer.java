package org.xmlSerial;

import java.io.IOException;
import org.jojo.flow.exc.Warning;
import org.jojo.flow.model.api.IXMLSerialTransform;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class XMLSerialTransformer implements IXMLSerialTransform {
    private XStream xStream;
    
    public XMLSerialTransformer() {
        
    }
    
    //this main method is for testing and exporting as runnable jar, so that req. libs get packed 
    //into the jar as well
    public static void main(String[] args) { 
        final XMLSerialTransformer transform = new XMLSerialTransformer();
        String x;
        try {
            x = IXMLSerialTransform.serialize("TEST-STRING");
            final String a = transform.toXMLString(x);
            System.err.println(Warning.getWarningLog());
            System.out.println(a);
            System.out.println("\n");
            final String b = transform.toSerialString(a);
            System.err.println(Warning.getWarningLog());
            System.out.println(b);
            System.out.println("\n");
            System.out.println(x.equals(b));
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private void initXStream() {
        this.xStream = new XStream(new StaxDriver());
        XStream.setupDefaultSecurity(this.xStream);
        this.xStream.allowTypesByRegExp(new String[] { ".*" });
    }
    
    @Override
    public String toXMLString(final String serialString) {
        initXStream();
        try {
            final Object o = IXMLSerialTransform.deserialize(serialString);
            return encodeXML(this.xStream.toXML(o));
        } catch (ClassNotFoundException | IOException | XStreamException e) {
            new Warning(null, e.toString(), true).reportWarning();
        }
        return null;
    }
    
    public static String encodeXML(CharSequence s) { 
        return org.apache.commons.text.StringEscapeUtils.escapeXml10(s.toString());
    }
    
    public static String decodeXML(final String xml) {
        return org.apache.commons.text.StringEscapeUtils.unescapeXml(xml);
    }

    @Override
    public String toSerialString(final String xmlString) {
        initXStream();
        try {
            final Object o = this.xStream.fromXML(decodeXML(xmlString));
            return IXMLSerialTransform.serialize(o);
        } catch (ClassNotFoundException | IOException | XStreamException e) {
            new Warning(null, e.toString(), true).reportWarning();
        }
        return null;
    }
}

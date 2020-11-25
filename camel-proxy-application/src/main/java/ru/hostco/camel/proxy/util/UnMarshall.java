package ru.hostco.camel.proxy.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayInputStream;

public class UnMarshall {

    public static Object unmarshall(String xml) throws Exception {

        SOAPMessage message = MessageFactory.newInstance().createMessage(null,
                new ByteArrayInputStream(xml.getBytes()));
        JAXBContext jaxbContext = JAXBContext.newInstance("ru.rt_eu.med.er.v2_0");
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Object obj = unmarshaller.unmarshal(message.getSOAPBody().extractContentAsDocument());

        return obj;
    }

}

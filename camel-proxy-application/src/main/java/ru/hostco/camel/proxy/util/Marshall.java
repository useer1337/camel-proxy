package ru.hostco.camel.proxy.util;

import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.*;
import java.io.ByteArrayOutputStream;

public class Marshall {

    public static String marshall(Object response) throws Exception{
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

        JAXBContext jaxbContext = JAXBContext.newInstance("ru.rt_eu.med.er.v2_0");
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(response, document);

        SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
        soapMessage.getSOAPBody().addDocument(document);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        soapMessage.writeTo(outputStream);
        String output = new String(outputStream.toByteArray());

        return output;
    }

}

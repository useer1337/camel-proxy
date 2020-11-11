package com.example.camelproxy.aggregation;

import com.example.camelproxy.GenerateToken;
import com.ibm.wsdl.Constants;
import com.ibm.wsdl.extensions.http.HTTPConstants;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import ru.rt_eu.med.er.v2_0.GetPatientInfoResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class GetPatientAggregation implements AggregationStrategy {
    private GetPatientInfoResponse myResponse;
    private GetPatientInfoResponse response;
    private String token;
    private GenerateToken generateToken = GenerateToken.getInstance();

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange){
        HttpServletResponse response1 = newExchange.getOut().getBody(HttpServletResponse.class);
        System.out.println(response1.getStatus());
        if (oldExchange == null) {
            token = generateToken.generateNewToken();
            myResponse = new GetPatientInfoResponse();
        }

        try {
            response = unmarshalResponse(newExchange.getIn().getBody(String.class));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

        if (!generateToken.getTokenMap().containsKey(token)) {
            List<String> tokenList = new ArrayList<>();

            String patientId = response.getPatientId();
            if (patientId != null)
                tokenList.add(response.getPatientId());

            generateToken.getTokenMap().put(token, tokenList);

        } else {
            String patientId = response.getPatientId();
            if (patientId != null)
                generateToken.getTokenMap().get(token).add(patientId);
        }

        myResponse.setSessionID(response.getSessionID());
        myResponse.setPatientId(token);
        newExchange.getIn().setBody(myResponse);
        return newExchange;
    }

    private GetPatientInfoResponse unmarshalResponse(String response) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(GetPatientInfoResponse.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        StringReader reader = new StringReader(response);
        GetPatientInfoResponse getPatientInfoResponse = (GetPatientInfoResponse) unmarshaller.unmarshal(reader);

        return getPatientInfoResponse;
    }
}

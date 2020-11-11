package com.example.camelproxy;

import com.example.camelproxy.aggregation.GetPatientAggregation;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.springframework.stereotype.Component;

@Component
public class Route extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        JaxbDataFormat jaxb = new JaxbDataFormat("ru.rt_eu.med.er.v2_0");

        from("spring-ws:uri:http://localhost:8081/api?endpointMapping=#endpointMapping")
                .setExchangePattern(ExchangePattern.InOut)
                .unmarshal(jaxb)
                .recipientList(method("requestRouter", "routRequest"));

        from("direct:GetPatientInfo")
                .setExchangePattern(ExchangePattern.InOut)
                .recipientList(constant("spring-ws:http://t-ovis-soapui.hostco.ru:8099/mockBasicHttpBinding_ErWebService," +
                        "spring-ws:http://p-ovis-psserv-2.hostco.ru:8080/FerIntegration/services/FerIntegration"))
                .aggregationStrategy(new GetPatientAggregation());

    }
}

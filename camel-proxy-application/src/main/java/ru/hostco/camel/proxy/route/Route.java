package ru.hostco.camel.proxy.route;

import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.springframework.stereotype.Component;
import ru.hostco.camel.proxy.aggregation.GetPatientAggregation;

/**
 * Класс маршрутизатор
 *
 */
@Component
public class Route extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        JaxbDataFormat jaxb = new JaxbDataFormat("ru.rt_eu.med.er.v2_0");

        // Ендпоинт куда приходит запрос от клинетов
        // ЗДЕСЬ ЕСТЬ ЛОГГИРОВАНИЕ!!!
        from("spring-ws:uri:http://localhost:8081/api?endpointMapping=#endpointMapping")
                .log(LoggingLevel.INFO, "Get request for endpoint")
                .setExchangePattern(ExchangePattern.InOut)
                .unmarshal(jaxb)
                .recipientList(method("requestRouter", "routeRequest"))
                .log(LoggingLevel.INFO, "Route request to end service");

        // Конечная точка в которой реализованна логика для GetPatient метода
        from("direct:GetPatientInfo")
                .setExchangePattern(ExchangePattern.InOut)
                .recipientList(constant("spring-ws:http://t-ovis-soapui.hostco.ru:8099/mockBasicHttpBinding_ErWebService," +
                        "spring-ws:http://p-ovis-psserv-2.hostco.ru:8080/FerIntegration/services/FerIntegration"))
                .aggregationStrategy(new GetPatientAggregation());
    }
}

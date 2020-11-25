package ru.hostco.camel.proxy.route;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.hostco.camel.proxy.aggregation.GetPatientAggregation;
import ru.hostco.camel.proxy.token.GenerateToken;

/**
 * Класс маршрутизатор
 */
@Component
public class Route extends RouteBuilder {

    private final ApplicationContext context;

    @Autowired
    public Route(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void configure() throws Exception {

        // Ендпоинт куда приходит запрос от клинетов
        from("servlet:/?matchOnUriPrefix=true")
                .log(LoggingLevel.INFO, "Get request for endpoint")
                .recipientList(method("requestRouter", "routeTo"))
                .log(LoggingLevel.INFO, "Route request to end service");

        // Конечная точка в которой реализованна логика для GetPatient метода
        from("direct:GetPatientInfo")
                .setExchangePattern(ExchangePattern.InOut)
                .recipientList(simple(
                        "http://localhost:8088/hi?bridgeEndpoint=true&throwExceptionOnFailure=false," +
                                "http://${in.header.url_komtek}?bridgeEndpoint=true&throwExceptionOnFailure=false"))
                .aggregationStrategy(context.getBean("getPatientAggregation", GetPatientAggregation.class));
    }
}

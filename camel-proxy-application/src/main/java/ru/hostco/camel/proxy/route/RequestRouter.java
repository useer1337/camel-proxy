package ru.hostco.camel.proxy.route;

import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.hostco.camel.proxy.config.ApplicationConfiguration;
import ru.hostco.camel.proxy.config.ClientConfiguration;
import ru.rt_eu.med.er.v2_0.GetPatientInfoRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static ru.hostco.camel.proxy.util.UnMarshall.unmarshall;

/**
 * Класс сопоставления
 */
@Component("requestRouter")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class RequestRouter {

    private final ApplicationConfiguration applicationConfiguration;

    /**
     * Метод переправляющий запрос по типу тела запроса
     *
     * @param xml запрос от клиента
     * @return куда будут отправляться запросы
     */
    public String routeTo(String xml, Exchange exchange) throws Exception {
        HttpServletRequest httpServletRequest = exchange.getIn().getBody(HttpServletRequest.class);
        String ip = httpServletRequest.getRemoteAddr();

        String route = "";

        Object request = unmarshall(xml);

        if (request instanceof GetPatientInfoRequest) {
            ClientConfiguration clientConfiguration = applicationConfiguration.getClient()
                    .stream().filter(clientConfiguration1 -> clientConfiguration1.getIpAddress().equals(ip)).findFirst().get();

            Map<String, Object> map = new HashMap<>();
            map.put("url_vitakor", clientConfiguration.getUrlVitakor());
            map.put("url_komtek", clientConfiguration.getUrlKomtek());
            exchange.getIn().setHeaders(map);
            route = "direct:GetPatientInfo";
        }

        return route;
    }

}

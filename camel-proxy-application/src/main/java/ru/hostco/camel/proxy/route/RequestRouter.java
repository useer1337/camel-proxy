package ru.hostco.camel.proxy.route;

import org.springframework.stereotype.Component;
import ru.rt_eu.med.er.v2_0.*;

/**
 * Класс сопоставления
 */
@Component("requestRouter")
public class RequestRouter {

    /**
     * Метод переправляющий запрос по типу тела запроса
     *
     * @param request запрос от клиента
     * @return куда будут отправляться запросы
     */
    public String routeRequest(Object request) {

        String route = null;

        if (request instanceof GetPatientInfoRequest)
            route = "direct:GetPatientInfo";

        if (request instanceof CancelAppointmentRequest)
            route = "spring-ws:http://t-ovis-soapui.hostco.ru:8099/mockBasicHttpBinding_ErWebService";

        if (request instanceof CreateAppointmentRequest)
            route = "spring-ws:http://t-ovis-soapui.hostco.ru:8099/mockBasicHttpBinding_ErWebService";

        if (request instanceof GetMOInfoExtendedRequest)
            route = "spring-ws:http://t-ovis-soapui.hostco.ru:8099/mockBasicHttpBinding_ErWebService";

        if (request instanceof GetMOResourceInfoRequest)
            route = "spring-ws:http://t-ovis-soapui.hostco.ru:8099/mockBasicHttpBinding_ErWebService";

        if (request instanceof GetReferralInfoRequest)
            route = "spring-ws:http://t-ovis-soapui.hostco.ru:8099/mockBasicHttpBinding_ErWebService";

        if (request instanceof GetScheduleInfoRequest)
            route = "spring-ws:http://t-ovis-soapui.hostco.ru:8099/mockBasicHttpBinding_ErWebService";

        if (request instanceof GetServicePostSpecsInfoRequest)
            route = "spring-ws:http://t-ovis-soapui.hostco.ru:8099/mockBasicHttpBinding_ErWebService";

        if (request instanceof ReferralAppointmentInformationRequest)
            route = "spring-ws:http://t-ovis-soapui.hostco.ru:8099/mockBasicHttpBinding_ErWebService";

        return route;
    }
}

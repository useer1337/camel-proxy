package com.example.camelproxy;

import org.springframework.stereotype.Component;
import ru.rt_eu.med.er.v2_0.*;

@Component("requestRouter")
public class RequestRouter {
    public String[] routRequest(Object request) {
        if (request instanceof GetPatientInfoRequest)
            return new String[]{"direct:GetPatientInfo"};

        if (request instanceof CancelAppointmentRequest)
            return new String[]{"spring-ws:http://t-ovis-soapui.hostco.ru:8099/mockBasicHttpBinding_ErWebService"};

        if (request instanceof CreateAppointmentRequest)
            return new String[]{"spring-ws:http://t-ovis-soapui.hostco.ru:8099/mockBasicHttpBinding_ErWebService"};

        if (request instanceof GetMOInfoExtendedRequest)
            return new String[]{"spring-ws:http://t-ovis-soapui.hostco.ru:8099/mockBasicHttpBinding_ErWebService"};

        if (request instanceof GetMOResourceInfoRequest)
            return new String[]{"spring-ws:http://t-ovis-soapui.hostco.ru:8099/mockBasicHttpBinding_ErWebService"};

        if (request instanceof GetReferralInfoRequest)
            return new String[]{"spring-ws:http://t-ovis-soapui.hostco.ru:8099/mockBasicHttpBinding_ErWebService"};

        if (request instanceof GetScheduleInfoRequest)
            return new String[]{"spring-ws:http://t-ovis-soapui.hostco.ru:8099/mockBasicHttpBinding_ErWebService"};

        if (request instanceof GetServicePostSpecsInfoRequest)
            return new String[]{"spring-ws:http://t-ovis-soapui.hostco.ru:8099/mockBasicHttpBinding_ErWebService"};

        if (request instanceof ReferralAppointmentInformationRequest)
            return new String[]{"spring-ws:http://t-ovis-soapui.hostco.ru:8099/mockBasicHttpBinding_ErWebService"};

        else return null;
    }
}

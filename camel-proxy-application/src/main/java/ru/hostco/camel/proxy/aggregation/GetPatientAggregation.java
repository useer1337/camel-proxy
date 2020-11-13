package ru.hostco.camel.proxy.aggregation;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import ru.hostco.camel.proxy.token.GenerateToken;
import ru.rt_eu.med.er.v2_0.GetPatientInfoResponse;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс котороый аггрегирует респонсы полученные от метода GetPatientInfo
 *
 */
public class GetPatientAggregation implements AggregationStrategy {
    private GetPatientInfoResponse generatedResponse;
    private String token;
    private GenerateToken generateToken = GenerateToken.getInstance();

    /**
     * Метод агреграции
     *
     * @param oldExchange контейнер сообщений содержащий старый ответ
     * @param newExchange контейнер сообщений содержащий новый ответ
     * @return возвращает контейнер сообщений
     */
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange){

        GetPatientInfoResponse receivedResponse = null;

        // Размаршаливаем респнсе который пришел
        try {
            receivedResponse = unmarshalResponse(newExchange.getIn().getBody(String.class));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (oldExchange == null) {
            token = generateToken.generateNewToken();
            generatedResponse = new GetPatientInfoResponse();

             addPatientIdToMap(receivedResponse);

            generatedResponse.setSessionID(receivedResponse.getSessionID());
            generatedResponse.setPatientId(token);
            newExchange.getIn().setBody(generatedResponse);
            return newExchange;
        }

        addPatientIdToMap(receivedResponse);

        return oldExchange;

    }


    /**
     * Метод добавления patientId в map
     *
     * @param response ответ в котором содержиться patientId
     */
    private void addPatientIdToMap(GetPatientInfoResponse response) {

        // Добавляем patientId в map
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

    }

    /**
     * Метод размаршаливания ответа из string в GetPatientInfoResponse
     *
     * @param response строка содержащая ответ
     * @return обьект класса GetPatientInfoResponse
     */
    private GetPatientInfoResponse unmarshalResponse(String response) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(GetPatientInfoResponse.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        StringReader reader = new StringReader(response);
        GetPatientInfoResponse getPatientInfoResponse = (GetPatientInfoResponse) unmarshaller.unmarshal(reader);

        return getPatientInfoResponse;
    }
}
package ru.hostco.camel.proxy.aggregation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.hostco.camel.proxy.entities.PatientIdData;
import ru.hostco.camel.proxy.entities.Token;
import ru.hostco.camel.proxy.repository.PatientIdDataRepository;
import ru.hostco.camel.proxy.repository.TokenRepository;
import ru.rt_eu.med.er.v2_0.GetPatientInfoResponse;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.hostco.camel.proxy.util.Marshall.marshall;
import static ru.hostco.camel.proxy.util.UnMarshall.unmarshall;

/**
 * Класс котороый аггрегирует респонсы полученные от метода GetPatientInfo
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class GetPatientAggregation implements AggregationStrategy {
    private final PatientIdDataRepository patientIdDataRepository;
    private final TokenRepository tokenRepository;

    /**
     * Метод агреграции
     *
     * @param oldExchange контейнер сообщений содержащий старый ответ
     * @param newExchange контейнер сообщений содержащий новый ответ
     * @return возвращает контейнер сообщений
     */
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        try {

            log.info("Aggregation responses GetPatientInfoResponse");
            GetPatientInfoResponse receivedResponse = null;

            receivedResponse = (GetPatientInfoResponse) unmarshall(newExchange.getIn().getBody(String.class));

            if (receivedResponse.getPatientId() == null)
                throw new Exception();


            String camelUri = (String) newExchange.getProperty("CamelRecipientListEndpoint");
            String uri = camelUri.split("\\/\\/")[1].split("\\?")[0];

            // Создаем собственный ответ
            addPatientIdToDB(receivedResponse, uri);
            GetPatientInfoResponse generatedResponse = generateResponse(receivedResponse);

            newExchange.getOut().setBody(marshall(generatedResponse));

            return newExchange;

        } catch (Exception e) {
            log.error(e.getMessage());
            return newExchange;
        }
    }

    private GetPatientInfoResponse generateResponse(GetPatientInfoResponse receivedResponse) {
        GetPatientInfoResponse generatedResponse = new GetPatientInfoResponse();
        generatedResponse.setSessionID(receivedResponse.getSessionID());

        Token token = tokenRepository.findBySession(receivedResponse.getSessionID()).get();

        generatedResponse.setPatientId(token.getValue());
        return generatedResponse;
    }


    private void addPatientIdToDB(GetPatientInfoResponse response, String uri) {

        Token token = null;

        if (!tokenRepository.findBySession(response.getSessionID()).isPresent())
        {
            token = new Token();
            token.setValue(generateNewToken());
            token.setSession(response.getSessionID());
            tokenRepository.save(token);
        }else{
            token = tokenRepository.findBySession(response.getSessionID()).get();
        }

        PatientIdData patientIdData = new PatientIdData();
        patientIdData.setValue(response.getPatientId());
        patientIdData.setAddress(uri);
        patientIdData.setToken(token);

        patientIdDataRepository.save(patientIdData);
    }


    /**
     * Метод генерирует токены
     *
     * @return сгенерированный токен
     */
    public String generateNewToken() {
        SecureRandom secureRandom = new SecureRandom();
        Base64.Encoder base64Encoder = Base64.getUrlEncoder();

        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
package ru.hostco.camel.proxy.aggregation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.hostco.camel.proxy.entities.MisAddress;
import ru.hostco.camel.proxy.entities.TemporaryToken;
import ru.hostco.camel.proxy.repository.MisAddressRepository;
import ru.hostco.camel.proxy.repository.TemporaryTokenRepository;
import ru.hostco.camel.proxy.token.GenerateToken;
import ru.rt_eu.med.er.v2_0.GetPatientInfoResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static ru.hostco.camel.proxy.util.UnMarshall.unmarshall;

/**
 * Класс котороый аггрегирует респонсы полученные от метода GetPatientInfo
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class GetPatientAggregation implements AggregationStrategy {
    private final MisAddressRepository misAddressRepository;
    private final TemporaryTokenRepository temporaryTokenRepository;

    // private String token;
    // private String patientId;

    /**
     * Метод агреграции
     *
     * @param oldExchange контейнер сообщений содержащий старый ответ
     * @param newExchange контейнер сообщений содержащий новый ответ
     * @return возвращает контейнер сообщений
     */
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

        log.info("Aggregation responses GetPatientInfoResponse");

        GetPatientInfoResponse receivedResponse = null;

        String charsetEncoding = newExchange.getIn().getHeader(Exchange.HTTP_CHARACTER_ENCODING, String.class);
        String message = newExchange.getIn().getBody(String.class);



        return newExchange;



//        // Размаршаливаем респнсе который пришел
//        try {
//            receivedResponse = (GetPatientInfoResponse) unmarshall(newExchange.getIn().getBody(String.class));
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//
//
//        if (oldExchange == null) {
//
//            patientId = receivedResponse.getPatientId();
//
//            if (patientId == null)
//                return newExchange;
//
//            addPatientIdToMap(patientId);
//
//            GetPatientInfoResponse generatedResponse = new GetPatientInfoResponse();
//            generatedResponse.setSessionID(receivedResponse.getSessionID());
//            generatedResponse.setPatientId(token);
//            newExchange.getIn().setBody(generatedResponse);
//            return newExchange;
//        }
//
//        if( patientId == null)
//            return oldExchange;
//
//        addPatientIdToMap(patientId);
//
//        return oldExchange;
    }

//    private void addPatientIdToMap(String patientId) {
//
//        // Проверка на токен
//        if (token == null)
//            token = generateNewToken();
//
//        // Добавляем patientId в map
//        if (!GenerateToken.getInstance().getTokenMap().containsKey(token)) {
//            Map<String, String> map = new HashMap<>();
//            map.put(patientId, "path");
//            GenerateToken.getInstance().getTokenMap().put(token, map);
//        } else {
//            GenerateToken.getInstance().getTokenMap().get(token).put(patientId, "path");
//        }
//    }

    /**
     * Метод добавления patientId в map
     *
     * @param response ответ в котором содержиться patientId
     */
    private void addPatientIdToDB(GetPatientInfoResponse response, String uri, String token) {

        MisAddress misAddress = misAddressRepository.findByAddressName(uri);

        String patientId = response.getPatientId();
        if (patientId != null) {
            TemporaryToken temporaryToken = new TemporaryToken();
            temporaryToken.setToken(token);
            temporaryToken.setPatientId(patientId);
            temporaryToken.getMisAddresses().add(misAddress);
            temporaryTokenRepository.save(temporaryToken);
        }
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
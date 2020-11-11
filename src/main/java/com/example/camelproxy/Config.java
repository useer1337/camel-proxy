package com.example.camelproxy;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.spring.ws.bean.CamelEndpointMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.endpoint.adapter.MessageEndpointAdapter;
import org.springframework.ws.server.endpoint.interceptor.PayloadLoggingInterceptor;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class Config extends WsConfigurerAdapter {

    @Bean
    public EndpointAdapter messageEndpointAdapter() {
        return new MessageEndpointAdapter();
    }

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/api/*");
    }

//    @Bean(name = "er-schema")
//    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema schema) {
//        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
//        wsdl11Definition.setPortTypeName("Service");
//        wsdl11Definition.setLocationUri("/api");
//        wsdl11Definition.setTargetNamespace("http://www.rt-eu.ru/med/er/v2_0");
//        wsdl11Definition.setSchema(schema);
//        return wsdl11Definition;
//    }

    @Bean
    public XsdSchema schema() {
        return new SimpleXsdSchema(new ClassPathResource("er-schema.xsd"));
    }

    @Bean
    public PayloadLoggingInterceptor loggingInterceptor(){
        return new PayloadLoggingInterceptor();
    }

    @Bean
    public PayloadValidatingInterceptor validatingInterceptor(){
        PayloadValidatingInterceptor payloadValidatingInterceptor = new PayloadValidatingInterceptor();
        payloadValidatingInterceptor.setSchema(new ClassPathResource("er-schema.xsd"));
        payloadValidatingInterceptor.setValidateRequest(true);
        payloadValidatingInterceptor.setValidateResponse(true);
        return payloadValidatingInterceptor;
    }

    @Bean
    public CamelEndpointMapping endpointMapping(){
        CamelEndpointMapping camelEndpointMapping = new CamelEndpointMapping();
        EndpointInterceptor[] list = {loggingInterceptor(), validatingInterceptor()};
        camelEndpointMapping.setInterceptors(list);
        return camelEndpointMapping;
    }
}

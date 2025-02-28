package com.audition.configuration;

import com.audition.common.logging.AuditionLogger;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebServiceConfiguration implements WebMvcConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(WebServiceConfiguration.class);

    private static final String YEAR_MONTH_DAY_PATTERN = "yyyy-MM-dd";

    private final ResponseHeaderInjector responseHeaderInjector;

    private final AuditionLogger logger;

    @Autowired
    public WebServiceConfiguration(final ResponseHeaderInjector responseHeaderInjector, final AuditionLogger logger) {
        this.responseHeaderInjector = responseHeaderInjector;
        this.logger = logger;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(responseHeaderInjector);
    }

    // TODO configure Jackson Object mapper that
    //  1. allows for date format as yyyy-MM-dd
    //  2. Does not fail on unknown properties
    //  3. maps to camelCase
    //  4. Does not include null values or empty values
    //  5. does not write datas as timestamps.
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd", Locale.forLanguageTag("en-AU")));
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    public RestTemplate restTemplate(ObjectMapper objectMapper) {
        final RestTemplate restTemplate = new RestTemplate(
            new BufferingClientHttpRequestFactory(createClientFactory()));
        // TODO use object mapper
        // TODO create a logging interceptor that logs request/response for rest template calls.

        // Use object mapper
        restTemplate.getMessageConverters().add(0, new MappingJackson2HttpMessageConverter(objectMapper));

        // Create a logging interceptor
        ClientHttpRequestInterceptor loggingInterceptor = (request, body, execution) -> {
            // Log request details
            logger.info(LOG, "Request URI: " + request.getURI());
            logger.info(LOG, "Request Method: " + request.getMethod());
            logger.info(LOG, "Request Headers: " + request.getHeaders());
            logger.info(LOG, "Request Body: " + new String(body, StandardCharsets.UTF_8));

            // Execute the request
            var response = execution.execute(request, body);

            // Convert InputStream to String and ensure it is closed
            String responseBody;
            try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
                responseBody = reader.lines().collect(Collectors.joining("\n"));
            }

            // Log response details
            logger.info(LOG, "Response Status Code: " + response.getStatusCode());
            logger.info(LOG, "Response Headers: " + response.getHeaders());
            logger.info(LOG, "Response Body: " + responseBody);

            return response;
        };

        restTemplate.getInterceptors().add(loggingInterceptor);

        return restTemplate;
    }

    private SimpleClientHttpRequestFactory createClientFactory() {
        final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        return requestFactory;
    }
}

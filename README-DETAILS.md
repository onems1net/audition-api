# Audition API

## Overview

Audition API is a Spring Boot application designed to provide a robust and scalable API service. It includes features
such as distributed tracing, metrics collection, and custom logging.

## Technologies Used

- Java
- Spring Boot
- Gradle
- OpenTelemetry
- SLF4J

## Project Structure

- `src/main/java/com/audition/configuration/ResponseHeaderInjector.java`: Interceptor to inject trace and span IDs into
  HTTP responses.
- `src/main/java/com/audition/common/logging/AuditionLogger.java`: Custom logger for standardized logging.
- `src/main/java/com/audition/common/exception/SystemException.java`: Custom exception class for handling system errors.
- `src/main/java/com/audition/web/AuditionController.java`: REST controller for handling API requests.
- `src/main/java/com/audition/service/AuditionService.java`: Service layer for business logic.
- `src/main/java/com/audition/integration/AuditionIntegrationClient.java`: Client for external service integration.
- `src/main/resources/application.yml`: Configuration file for Spring Boot and other services.

## OpenAPI Specification

The OpenAPI specification for the Audition API can be found [here](docs/audition-api-openapi-spec.yml).

## Configuration

### application.yml

The `application.yml` file contains various configurations for the application:

- **Spring Boot Application Settings**:
    - `spring.application.name`: Name of the application.
    - `spring.config.import`: Configuration server import.
    - `spring.main.allow-bean-definition-overriding`: Allows bean definition overriding.
    - `spring.autoconfigure.exclude`: Excludes DataSource auto-configuration.
    - `spring.mvc.throw-exception-if-no-handler-found`: Throws an exception if no handler is found.

- **Sleuth and OpenTelemetry**:
    - `spring.sleuth.enabled`: Enables Sleuth.
    - `spring.sleuth.sampler.probability`: Sets the sampling probability.
    - `spring.sleuth.otel.enabled`: Enables OpenTelemetry.
    - `spring.sleuth.otel.config.trace-id-ratio-based`: Sets the trace ID ratio.

- **Server Settings**:
    - `server.max-http-request-header-size`: Sets the maximum HTTP request header size.

- **Management and Actuator**:
    - `management.tracing.enabled`: Enables tracing.
    - `management.tracing.sampling.probability`: Sets the sampling probability.
    - `management.endpoints.web.exposure.include`: Includes specific Actuator endpoints.
    - `management.endpoint.health.show-details`: Shows health details.
    - `management.security.enabled`: Enables security for Actuator endpoints.
    - `management.security.roles`: Sets the roles for accessing Actuator endpoints.

- **OpenTelemetry Exporter**:
    - `otel.exporter.otlp.endpoint`: Sets the OTLP endpoint.
    - `otel.traces.exporter`: Sets the traces exporter.
    - `otel.traces.sampler`: Sets the traces sampler.

- **Logging**:
    - `logging.pattern.level`: Sets the logging pattern.

- **Audition Integration**:
    - `audition.integration.base-url`: Sets the base URL for integration.

## Running the Application

1. **Build the Project**:
    ```sh
    ./gradlew build
    ```

2. **Run the Application**:
    ```sh
    ./gradlew bootRun
    ```

3. **Access Actuator Endpoints**:

- Health: `http://localhost:8080/actuator/health`
- Metrics: `http://localhost:8080/actuator/metrics`

## Logging

The application uses a custom logger (`AuditionLogger`) to standardize logging across the application. It supports
various log levels such as `info`, `debug`, `warn`, and `error`.

## Exception Handling

The application uses a custom exception class (`SystemException`) to handle system errors. This class provides various
constructors to create exceptions with different levels of detail.

## Tracing

The application uses OpenTelemetry for distributed tracing. The `ResponseHeaderInjector` interceptor adds trace and span
IDs to HTTP responses, which helps in tracking requests across different services.

## Metrics

Metrics can be accessed via the Actuator endpoints.
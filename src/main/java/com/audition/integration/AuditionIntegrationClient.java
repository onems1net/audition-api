package com.audition.integration;

import com.audition.common.exception.SystemException;
import com.audition.configuration.ExternalApiConfiguration;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class AuditionIntegrationClient {

    private static final Logger LOG = LoggerFactory.getLogger(AuditionIntegrationClient.class);
    private final RestTemplate restTemplate;
    private final ExternalApiConfiguration externalApiConfiguration;

    public AuditionIntegrationClient(RestTemplate restTemplate, ExternalApiConfiguration externalApiConfiguration) {
        this.restTemplate = restTemplate;
        this.externalApiConfiguration = externalApiConfiguration;
    }

    public List<AuditionPost> getPosts() {
        String requestUrl = externalApiConfiguration.getBaseUrl().concat(externalApiConfiguration.getGetPostsPath());

        try {
            ResponseEntity<AuditionPost[]> response = restTemplate.getForEntity(requestUrl, AuditionPost[].class);
            return Arrays.asList(Objects.requireNonNull(response.getBody()));
        } catch (final HttpClientErrorException e) {
            LOG.error("Error occurred while executing getPosts - requestUrl[{}] statusCode[{}] responseBody[{}]",
                requestUrl,
                e.getStatusCode().value(),
                e.getResponseBodyAsString()
            );
            throw new SystemException("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public AuditionPost getPostById(final String id) {
        String requestUrl = String.format(
            externalApiConfiguration.getBaseUrl().concat(externalApiConfiguration.getGetSinglePostPath()),
            id
        );
        try {
            return restTemplate.getForEntity(requestUrl, AuditionPost.class).getBody();
        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException(
                    String.format("Cannot find a Post with id - %s", id),
                    "Resource Not Found",
                    HttpStatus.NOT_FOUND.value()
                );
            } else {
                LOG.error("Error occurred while executing getPostById - requestUrl[{}] statusCode[{}] responseBody[{}]",
                    requestUrl,
                    e.getStatusCode().value(),
                    e.getResponseBodyAsString()
                );
                throw new SystemException("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        }
    }

    public AuditionPost getPostByIdIncludingComments(final String id) {
        String requestUrl = String.format(
            externalApiConfiguration.getBaseUrl().concat(externalApiConfiguration.getGetPostCommentsPath()),
            id
        );
        AuditionPost auditionPost = this.getPostById(id);
        try {
            List<AuditionPostComment> comments = Arrays.asList(
                Objects.requireNonNull(restTemplate.getForEntity(requestUrl, AuditionPostComment[].class).getBody()));
            auditionPost.setComments(comments);
            return auditionPost;
        } catch (final HttpClientErrorException e) {
            LOG.error(
                "Error occurred while executing getPostByIdIncludingComments - requestUrl[{}] statusCode[{}] responseBody[{}]",
                requestUrl,
                e.getStatusCode().value(),
                e.getResponseBodyAsString()
            );
            throw new SystemException("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public List<AuditionPostComment> getCommentsByPost(final String id) {
        String requestUrl = String.format(
            externalApiConfiguration.getBaseUrl().concat(externalApiConfiguration.getGetCommentsByPost()),
            id
        );
        try {
            return Arrays.asList(
                Objects.requireNonNull(restTemplate.getForEntity(requestUrl, AuditionPostComment[].class).getBody()));
        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException(
                    String.format("Cannot find a Post with id - %s", id),
                    "Resource Not Found",
                    HttpStatus.NOT_FOUND.value()
                );
            } else {
                LOG.error(
                    "Error occurred while executing getCommentsByPost - requestUrl[{}] statusCode[{}] responseBody[{}]",
                    requestUrl,
                    e.getStatusCode().value(),
                    e.getResponseBodyAsString()
                );
                throw new SystemException("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        }
    }
}

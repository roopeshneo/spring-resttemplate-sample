package com.learn.spring.rest.restapi.exception;

import com.learn.spring.rest.restapi.config.RestTemplateConfig;
import com.learn.spring.rest.restapi.model.Todo;
import com.learn.spring.rest.restapi.service.TodoJsonPlaceHolderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RestTemplateConfig.class, TodoJsonPlaceHolderService.class})
public class CustomResponseErrorHandlerTest {
    private static final String URL = "/server/products/1";
    private ResponseActions responseActions;

    @Autowired
    TodoJsonPlaceHolderService todoJsonPlaceHolderService;

    @BeforeEach
    public void setUp() {
        responseActions = MockRestServiceServer.createServer(todoJsonPlaceHolderService.restTemplateWithErrorHandler)
                .expect(requestTo(URL))
                .andExpect(method(HttpMethod.GET));
    }

    @Test
    public void response4xx() {
        responseActions.andRespond(withBadRequest());

        assertThatThrownBy(() -> todoJsonPlaceHolderService.findAll()
                .hasCauseInstanceOf(CustomResponseErrorHandler.CustomException.class);
    }

    @Test
    public void response5xx() {
        responseActions.andRespond(withServerError());

        assertThatThrownBy(() -> consumerService.consume(URL, Object.class))
                .hasCauseInstanceOf(CustomException.class);
    }
}

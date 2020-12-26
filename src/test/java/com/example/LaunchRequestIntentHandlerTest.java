package com.example;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ui.SimpleCard;
import com.amazon.ask.model.ui.SsmlOutputSpeech;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class LaunchRequestIntentHandlerTest {

    @Inject
    LaunchRequestIntentHandler handler;

    @Test
    void testLaunchRequestIntentHandler() {
        LaunchRequest request = LaunchRequest.builder().build();
        HandlerInput input = HandlerInput.builder()
                .withRequestEnvelope(RequestEnvelope.builder()
                        .withRequest(request)
                        .build()
                ).build();

        assertTrue(handler.canHandle(input));
        Optional<Response> responseOptional = handler.handle(input);
        assertTrue(responseOptional.isPresent());
        Response response = responseOptional.get();
        assertTrue(response.getOutputSpeech() instanceof SsmlOutputSpeech);
        String speechText = "Welcome to the Alexa Skills Kit, you can say hello";
        String expectedSsml = "<speak>" + speechText + "</speak>";
        assertEquals(expectedSsml, ((SsmlOutputSpeech) response.getOutputSpeech()).getSsml());
        assertNotNull(response.getReprompt());
        assertNotNull(response.getReprompt().getOutputSpeech());
        assertTrue(response.getReprompt().getOutputSpeech() instanceof SsmlOutputSpeech);
        assertEquals(expectedSsml,((SsmlOutputSpeech) response.getReprompt().getOutputSpeech()).getSsml());
        assertTrue(response.getCard() instanceof SimpleCard);
        assertEquals("HelloWorld", ((SimpleCard) response.getCard()).getTitle());
        assertEquals(speechText, ((SimpleCard) response.getCard()).getContent());
        assertFalse(response.getShouldEndSession());
    }
}

package cz.tomasdvorak.graylogdemo;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;
import org.mockserver.model.Format;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.HttpStatusCode;
import org.mockserver.verify.VerificationTimes;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@SpringBootTest(
		// Provide the mock server/port as the gelf endpoint
		properties = { "graylog.server.url=http://localhost:1080/gelf" }
)
public class GraylogClientIntegrationTest {

	private static ClientAndServer mockServer;

	@BeforeAll
	static void setUp() {
		mockServer = startClientAndServer(1080);
		final HttpRequest post = new HttpRequest().withMethod("POST").withPath("/gelf");
		mockServer.when(post, Times.exactly(100)) // respond only to first 100 requests
				.respond(new HttpResponse()
						.withStatusCode(HttpStatusCode.ACCEPTED_202.code())
						.withBody("Accepted"));
	}

	@AfterAll
	static void tearDown() {
		mockServer.stop();
	}

	@Test
	void verifyMessageTransmission() {
		// in the moment context is loaded and the app running, all messages have already been sent
		final HttpRequest req = HttpRequest.request("/gelf");

		// verify that all 100 messages have been delivered to the server
		mockServer.verify(req, VerificationTimes.exactly(100));

		// obtain all recorded requests, parse them from JSON
		final String requests = mockServer.retrieveRecordedRequests(req, Format.JSON);
		final JsonPath jsonPath = JsonPath.from(requests);

		// verify GELF required headers
		Assertions.assertEquals("localhost", jsonPath.get("[0].body.json.host"));
		Assertions.assertEquals("request log", jsonPath.get("[0].body.json.short_message"));
		Assertions.assertNotNull(jsonPath.get("[0].body.json.timestamp"));

		// verify some of the additional attributes of the message (caution, keys start with underscore)
		Assertions.assertEquals("desktop", jsonPath.get("[0].body.json._ClientDeviceType"));
		Assertions.assertEquals(403, jsonPath.getInt("[0].body.json._ClientStatus"));
		Assertions.assertEquals("graylog.org", jsonPath.get("[0].body.json._ClientRequestReferer"));
		Assertions.assertEquals(1576929197, jsonPath.getInt("[0].body.json._EdgeStartTimestamp"));

	}

}

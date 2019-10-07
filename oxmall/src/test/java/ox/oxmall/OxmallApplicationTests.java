package ox.oxmall;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = OxmallApplication.class)
@AutoConfigureWebTestClient
@AutoConfigureMockMvc
public class OxmallApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private WebTestClient client;

	@Test
	public void index() throws Exception {
		ResultActions resultActions = mvc.perform(post("/index")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
				.andExpect(content().encoding("UTF-8"))
				/*.andExpect(content().string("Hello，Spring Boot！"))*/;
		System.out.println(resultActions.andReturn().getResponse().getContentAsString());
	}


	@Test
	public void index2() throws Exception {
		String result = client.post().uri("/index").contentType(MediaType.APPLICATION_JSON)
				.exchange().expectBody(String.class).returnResult().getResponseBody();
		System.out.println(result);
	}
}

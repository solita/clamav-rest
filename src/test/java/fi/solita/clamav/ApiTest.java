package fi.solita.clamav;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class, TestConfiguration.class })
@AutoConfigureMockMvc
public class ApiTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void putRequestNotAllowed() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.put("/")
            .content("{\"some\": \"content\"}"))
            .andExpect(status().isMethodNotAllowed());
  }

  @Test
  public void deleteRequestNotAllowed() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/"))
            .andExpect(status().isMethodNotAllowed());
  }

  @Test
  public void patchRequestNotAllowed() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.patch("/"))
            .andExpect(status().isMethodNotAllowed());
  }

  @Test
  public void headRequestNotAllowed() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.head("/"))
            .andExpect(status().isMethodNotAllowed());
  }

  @Test
  public void optionsRequestNotAllowed() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.options("/"))
            .andExpect(status().isMethodNotAllowed());
  }

  @Test
  public void traceRequestNotAllowed() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.TRACE,"/"))
            .andExpect(status().isMethodNotAllowed());
  }
}

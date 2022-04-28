package fi.solita.clamav;

import fi.solita.clamav.mock.ClamAVClientFactoryMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(
  classes = { Application.class, TestConfiguration.class },
  properties = {"spring.main.allow-bean-definition-overriding=true"}
)
@AutoConfigureMockMvc
public class ApiTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ClamAVClientFactoryMock clamAVClientFactoryMock;

  @Test
  public void pingApi() throws Exception {
    Mockito.when(clamAVClientFactoryMock.clamAVClientMock.ping())
            .thenReturn(true);

    mockMvc.perform(MockMvcRequestBuilders.get("/"))
            .andExpect(status().isOk())
            .andExpect(content().string("Clamd responding: true\n"));
  }

  @Test
  public void postScan() throws Exception {
    String resultFromClamd = "Result from clamd: OK";

    Mockito.when(clamAVClientFactoryMock.clamAVClientMock.scan(any(InputStream.class)))
            .thenReturn(resultFromClamd.getBytes(StandardCharsets.UTF_8));

    MockMultipartFile testFile = new MockMultipartFile(
            "file",
            "Test file contents".getBytes(StandardCharsets.UTF_8));

    mockMvc.perform(MockMvcRequestBuilders.fileUpload("/scan")
                    .file(testFile)
                    .param("name", "testFile.txt"))
            .andExpect(status().isOk())
            .andExpect(content().string("Everything ok : true\n"));
  }

  @Test
  public void postScanReply() throws Exception {
    String resultFromClamd = "Result from clamd: OK";

    Mockito.when(clamAVClientFactoryMock.clamAVClientMock.scan(any(InputStream.class)))
            .thenReturn(resultFromClamd.getBytes(StandardCharsets.UTF_8));

    MockMultipartFile testFile = new MockMultipartFile(
            "file",
            "Test file contents".getBytes(StandardCharsets.UTF_8));

    mockMvc.perform(MockMvcRequestBuilders.fileUpload("/scanReply")
            .file(testFile)
            .param("name", "testFile.txt"))
            .andExpect(status().isOk())
            .andExpect(content().string(resultFromClamd));
  }

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

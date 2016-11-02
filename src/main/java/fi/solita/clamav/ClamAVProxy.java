package fi.solita.clamav;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ClamAVProxy {

  @Value("${clamd.host}")
  private String hostname;

  @Value("${clamd.port}")
  private int port;

  @Value("${clamd.timeout}")
  private int timeout;

  /**
   * @return Clamd status.
   */
  @RequestMapping("/")
  public String ping() throws IOException {
    ClamAVClient a = new ClamAVClient(hostname, port, timeout);
    return "Clamd responding: " + a.ping() + "\n";
  }

  /**
   * @return Clamd scan result
   */
  @RequestMapping(value="/scan", method=RequestMethod.POST)
  public @ResponseBody String handleFileUpload(@RequestParam("name") String name,
                                               @RequestParam("file") MultipartFile file) throws IOException{
    if (!file.isEmpty()) {
      ClamAVClient a = new ClamAVClient(hostname, port, timeout);
      byte[] r = a.scan(file.getInputStream());
      return "Everything ok : " + ClamAVClient.isCleanReply(r) + "\n";
    } else throw new IllegalArgumentException("empty file");
  }
}

package fi.solita.clamav;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ClamAVProxy {
  private ClamAVClientFactory clamAVClientFactory;

  public ClamAVProxy(@Autowired ClamAVClientFactory clamAVClientFactory) {
    this.clamAVClientFactory = clamAVClientFactory;
  }

  /**
   * @return Clamd status.
   */
  @RequestMapping("/")
  public String ping() throws IOException {
    ClamAVClient a = clamAVClientFactory.newClient();
    return "Clamd responding: " + a.ping() + "\n";
  }

  /**
   * @return Clamd scan result
   */
  @RequestMapping(value="/scan", method=RequestMethod.POST)
  public @ResponseBody String handleFileUpload(@RequestParam("name") String name,
                                               @RequestParam("file") MultipartFile file) throws IOException{
    if (!file.isEmpty()) {
      ClamAVClient a = clamAVClientFactory.newClient();
      byte[] r = a.scan(file.getInputStream());
      return "Everything ok : " + ClamAVClient.isCleanReply(r) + "\n";
    } else throw new IllegalArgumentException("empty file");
  }

  /**
   * @return Clamd scan reply
   */
  @RequestMapping(value="/scanReply", method=RequestMethod.POST)
  public @ResponseBody String handleFileUploadReply(@RequestParam("name") String name,
                                                    @RequestParam("file") MultipartFile file) throws IOException{
    if (!file.isEmpty()) {
      ClamAVClient a = clamAVClientFactory.newClient();
      return new String(a.scan(file.getInputStream()));
    } else throw new IllegalArgumentException("empty file");
  }
}

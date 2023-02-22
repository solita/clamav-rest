package fi.solita.clamav;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

import org.apache.commons.io.input.TeeInputStream;
import org.apache.commons.io.output.TeeOutputStream;
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
      //Split input streams to send one to the validator and one to the scanner
      List<InputStream> inputStreams = splitInputStream(file.getInputStream());
      InputStream validationStream = inputStreams.get(0);
      MediaTypeValidator.verifyFileIsOfAcceptedType(validationStream, name);

      ClamAVClient a = clamAVClientFactory.newClient();
      InputStream scannerStream = inputStreams.get(1);
      byte[] r = a.scan(scannerStream);
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
      //Split input streams to send one to the validator and one to the scanner
      List<InputStream> inputStreams = splitInputStream(file.getInputStream());
      InputStream validationStream = inputStreams.get(0);
      MediaTypeValidator.verifyFileIsOfAcceptedType(validationStream, name);

      ClamAVClient a = clamAVClientFactory.newClient();
      InputStream scannerStream = inputStreams.get(1);
      return new String(a.scan(scannerStream));
    } else throw new IllegalArgumentException("empty file");
  }

  private List<InputStream> splitInputStream(InputStream input)
          throws IOException
  {
    Objects.requireNonNull(input);

    PipedOutputStream pipedOut01 = new PipedOutputStream();
    PipedOutputStream pipedOut02 = new PipedOutputStream();

    List<InputStream> inputStreamList = new ArrayList<>();
    inputStreamList.add(new PipedInputStream(pipedOut01));
    inputStreamList.add(new PipedInputStream(pipedOut02));

    TeeOutputStream tout = new TeeOutputStream(pipedOut01, pipedOut02);

    TeeInputStream tin = new TeeInputStream(input, tout, true);

    Executors.newSingleThreadExecutor().submit(tin::readAllBytes);

    return Collections.unmodifiableList(inputStreamList);
  }

}

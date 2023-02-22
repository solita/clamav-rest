package fi.solita.clamav;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

public class MediaTypeValidatorTest {

  @ParameterizedTest
  @ValueSource(strings = {"test.jpg", "test.png"})
  public void acceptedMediaTypes_returns(String fileName) throws Exception {
    InputStream stream = MediaTypeValidatorTest.class.getClassLoader().getResourceAsStream(fileName);
    MediaTypeValidator.verifyFileIsOfAcceptedType(stream, fileName);
  }

  @ParameterizedTest
  @ValueSource(strings = {"test.png.zip"})
  public void rejectedMediaTypes_throwsException(String fileName) {
    InputStream stream = MediaTypeValidatorTest.class.getClassLoader().getResourceAsStream(fileName);
    assertThrows(
            IllegalArgumentException.class,
            () -> MediaTypeValidator.verifyFileIsOfAcceptedType(stream, fileName)
    );
  }
}

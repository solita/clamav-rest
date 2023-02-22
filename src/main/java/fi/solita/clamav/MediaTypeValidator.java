package fi.solita.clamav;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.mime.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class MediaTypeValidator {

    private static Set<MediaType> acceptedMediaTypes = MediaType.set(
            MediaType.image("png"),
            MediaType.image("jpeg")
    );

    public static void verifyFileIsOfAcceptedType(InputStream inputStream, String fileName) throws IOException {
        TikaConfig config = TikaConfig.getDefaultConfig();
        Detector detector = config.getDetector();
        TikaInputStream stream = TikaInputStream.get(inputStream);
        Metadata metadata = new Metadata();
        metadata.add(TikaCoreProperties.RESOURCE_NAME_KEY, fileName);
        MediaType detectedType = detector.detect(stream, metadata);
        if(acceptedMediaTypes.contains(detectedType) == false) {
            throw new IllegalArgumentException("Detected type "+detectedType.toString()+" is not in the accepted types "
                    + String.join(",", acceptedMediaTypes.stream().map(MediaType::toString).toList()));
        }
    }
}

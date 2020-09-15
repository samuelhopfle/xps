package ch.less.infrastructure.xps.share;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

/**
 * helping class for general URI and file handling
 */
public final class XpsURIHelper {

    // file representation possibilities
    public static final String FILE_PROTOCOL_REPRESENTATION_3 = "file:///";
    public static final String FILE_PROTOCOL_REPRESENTATION_2 = "file://";
    public static final String FILE_PROTOCOL_REPRESENTATION_1 = "file:/";
    public static final String CORRECT_FILE_PROTOCOL = "file:////";

    /**
     * helping method to get a stream resource either from a file (local) or an URL connection (network)
     *
     * @param fileURI URI of the needed file
     * @return StreamSource of the file
     */
    public static StreamSource openSource(URI fileURI) {
        try {
            final URL fileURL = fileURI.toURL();
            // stream the file from a network source
            if (fileURL.getAuthority() != null) { // authority = network
                URLConnection connection = fileURL.openConnection();
                InputStream stream = connection.getInputStream();
                return new StreamSource(stream, fileURI.toString()); // file as an URL stream with the associated directory
            }
        } catch (IOException e) {
            // in case it does not work in the code, there will be more errors there
        }
        // open the file
        File templateFile = new File(fileURI); // file for the ressource
        return new StreamSource(templateFile); // FO template
    }

    /**
     * adapt the URI for network access
     *
     * @param uri URI as a String, that should be adapted
     * @return adapted URI as a String
     */
    public static String extendURIForNetzwork(String uri) {
        if (uri.startsWith(FILE_PROTOCOL_REPRESENTATION_3))
            return uri.replace(FILE_PROTOCOL_REPRESENTATION_3, CORRECT_FILE_PROTOCOL);
        else if (uri.startsWith(FILE_PROTOCOL_REPRESENTATION_2))
            return uri.replace(FILE_PROTOCOL_REPRESENTATION_2, CORRECT_FILE_PROTOCOL);
        else if (uri.startsWith(FILE_PROTOCOL_REPRESENTATION_1))
            return uri.replace(FILE_PROTOCOL_REPRESENTATION_1, CORRECT_FILE_PROTOCOL);
        else
            return uri;
    }
}

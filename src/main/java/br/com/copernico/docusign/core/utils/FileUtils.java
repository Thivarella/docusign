package br.com.copernico.docusign.core.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.text.Normalizer;

public class FileUtils {
    /**
     * Loads a file content and copy it into a byte array.
     *
     * @param path the absolute path within the class path
     * @return the new byte array that has been loaded from the file
     * @throws IOException in case of I/O errors
     */
    public static byte[] readFile(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        return StreamUtils.copyToByteArray(resource.getInputStream());
    }

    public static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }
}

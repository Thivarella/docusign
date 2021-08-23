package br.com.copernico.docusign.core.utils;

import br.com.copernico.docusign.OauthSession;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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

    public static void loadCredentials() throws IOException {
        File file = new File("/var/files/oauth.json");
        JsonObject object = (JsonObject) JsonParser.parseReader(new FileReader(file));
        OauthSession oauthSession = OauthSession.getInstance(object.get("access_token").getAsString(),
                object.get("token_type").getAsString(),
                object.get("refresh_token").getAsString(),
                object.get("expires_in").getAsString());

    }

    public static void saveNewCredentials(OauthSession oauthSession) throws Exception {
        File file = new File("/var/files/oauth.json");
        JsonObject object = (JsonObject) JsonParser.parseReader(new FileReader(file));
        object.addProperty("access_token", oauthSession.getAccessToken());
        object.addProperty("refresh_token", oauthSession.getRefreshToken());

        FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
        fileWriter.write(object.toString());
        fileWriter.flush();
        fileWriter.close();
    }
}

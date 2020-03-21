package de.pandemieduell.api;

import java.util.Base64;

import org.springframework.web.server.ResponseStatusException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public final class Authorization {
    private Authorization() {}

    public static UserCredentials getUserCredentials(String authorization) {
        if (authorization == null) {
            throw new ResponseStatusException(UNAUTHORIZED, "No authorization was provided!");
        }
        if (!authorization.toLowerCase().startsWith("basic")) {
            throw new ResponseStatusException(UNAUTHORIZED, "Wrong authorization was provided!");
        }
        String base64Credentials = authorization.substring("Basic".length()).trim();
        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(base64Credentials);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(UNAUTHORIZED, "Bad atuhorization was provided: malformed base64!", e);
        }
        String credentials = new String(decoded, UTF_8);
        final String[] values = credentials.split(":", 2);
        if (values.length < 2) {
            throw new ResponseStatusException(UNAUTHORIZED, "Bad atuhorization was provided: missing password!");
        }
        return new UserCredentials(values[0], values[1]);
    }
}

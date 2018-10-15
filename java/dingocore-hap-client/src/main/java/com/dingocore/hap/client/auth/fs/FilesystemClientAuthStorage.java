package com.dingocore.hap.client.auth.fs;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.dingocore.hap.client.auth.simple.SimpleAccessoryAuthInfo;
import com.dingocore.hap.client.auth.AbstractClientAuthStorage;
import com.dingocore.hap.client.auth.AccessoryAuthInfo;

/**
 * Created by bob on 9/7/18.
 */
public class FilesystemClientAuthStorage extends AbstractClientAuthStorage {

    private static final String LONG_TERM_SECRET_KEY = "ltsk";

    private static final String PIN = "pin";

    private static final String LONG_TERM_PUBLIC_KEY = "ltpk";

    public FilesystemClientAuthStorage(String identifier) throws IOException {
        super(identifier);
        initialize();
    }

    private void initialize() throws IOException {
        this.root = Paths.get(System.getProperty("user.home"), ".footbridge", "client", getIdentifier());

        if (!Files.exists(this.root)) {
            Files.createDirectories(this.root);
        }

        Path ltskFile = this.root.resolve(LONG_TERM_SECRET_KEY);

        if (Files.exists(ltskFile)) {
            this.ltsk = Base64.getDecoder().decode(Files.readAllBytes(ltskFile));
        } else {
            this.ltsk = generateLTSK();
            Files.write(ltskFile, Base64.getEncoder().encode(this.ltsk));
        }
    }

    @Override
    public byte[] getLTSK() {
        return this.ltsk;
    }

    @Override
    public AccessoryAuthInfo get(String identifier) {
        if (this.cache.containsKey(identifier)) {
            return this.cache.get(identifier);
        }

        Path file = getAccessoriesDir().resolve(identifier);
        if (!Files.exists(file)) {
            return null;
        }

        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(file.toFile())) {
            props.load(in);
            SimpleAccessoryAuthInfo info = new SimpleAccessoryAuthInfo(
                    identifier,
                    props.getProperty(PIN),
                    props.getProperty(LONG_TERM_PUBLIC_KEY)
            );
            this.cache.put(identifier, info);
            return info;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void put(String identifier, String pin, byte[] ltpk) throws IOException {
        Path file = getAccessoriesDir().resolve(identifier);

        Properties props = new Properties();
        props.setProperty(PIN, pin);
        props.setProperty(LONG_TERM_PUBLIC_KEY, Base64.getEncoder().encodeToString(ltpk));

        if ( ! Files.exists(file.getParent())) {
            Files.createDirectories(file.getParent());
        }

        try (FileOutputStream out = new FileOutputStream(file.toFile())) {
            props.store(out, "Created by footbridge.io");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Path getAccessoriesDir() {
        return this.root.resolve("accessories");
    }

    private Path root;

    private byte[] ltsk;

    private Map<String, SimpleAccessoryAuthInfo> cache = new HashMap<>();
}

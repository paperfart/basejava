package com.urise.webapp.util;

import com.urise.webapp.storage.SqlStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    // private static final File PROPS = new File("basejava/config/resumes.properties");
    private static final File PROPS = new File(getHomeDir(), "basejava\\config\\resumes.properties");

    private static final Config INSTANCE = new Config();

    private final File storageDir;
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;
    private final SqlStorage storage;

    private Config() {
        try (InputStream is = new FileInputStream(PROPS)) {
            Properties props = new Properties();
            props.load(is);
            storageDir = new File(props.getProperty("storage.dir"));
            dbUrl = props.getProperty("db.url");
            dbUser = props.getProperty("db.user");
            dbPassword = props.getProperty("db.password");
            storage = new SqlStorage(dbUrl, dbUser, dbPassword);

        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPS.getAbsolutePath());
        }
    }

    private static File getHomeDir() {
        String prop = System.getProperty("homeDir");
        File homeDir = new File(prop == null ? "." : prop);
        if (!homeDir.isDirectory()) {
            throw new IllegalStateException(homeDir + " is not directory");
        }
        return homeDir;
    }

    public SqlStorage getStorage() {
        return storage;
    }

    public static Config get() {
        return INSTANCE;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public File getStorageDir() {
        return storageDir;
    }

}

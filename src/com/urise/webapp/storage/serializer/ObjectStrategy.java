package com.urise.webapp.storage.serializer;

import com.urise.webapp.exeption.StorageException;
import com.urise.webapp.model.Resume;

import java.io.*;

public class ObjectStrategy implements SerializeStrategy {

    @Override
    public void writeResume(Resume resume, OutputStream outputStream) throws IOException {
        try (ObjectOutputStream os = new ObjectOutputStream(outputStream)) {
            os.writeObject(resume);
        }
    }

    @Override
    public Resume readResume(InputStream inputStream) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(inputStream)) {
            return (Resume) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new StorageException("Error read resume", null, e);
        }
    }


}

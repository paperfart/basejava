package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.*;

public class MapFullnameStorage extends AbstractStorage<Resume> {
    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    protected void deleteResume(Resume key) {
        storage.remove(key.getUuid());
    }

    @Override
    protected void saveResume(Resume resume, Resume key) {
        storage.put(resume.getUuid(), resume);
    }

    public void clear() {
        storage.clear();
    }

    @Override
    public void updateResume(Resume resume, Object key) {
        storage.replace((String) key, resume);
    }

    @Override
    public Resume getResume(Resume key) {
        return storage.get(key.getUuid());
    }

    @Override
    protected boolean checkResumeExist(Resume key) {
        return storage.containsValue(key);
    }

    @Override
    protected Resume searchKey(String uuid) {
        for (String key : storage.keySet()) {
            if (key.equals(uuid)) {
                return storage.get(key);
            }
        }
        return new Resume(uuid);
    }

    public Resume[] getAll() {
        Collection<Resume> values = storage.values();
        Resume[] result = values.toArray(Resume[]::new);
        Arrays.sort(result, Comparator.comparing(Resume::getUuid));
        return result;
    }

    /*public List<Resume> getAllSorted() {
        List<Resume> resumes = new ArrayList<>(storage.values());
        resumes.sort(Comparator.comparing(Resume::getFullName));
        return resumes;
    }*/

    @Override
    protected List<Resume> getResumes() {
        return new ArrayList<>(storage.values());
    }

    public int size() {
        return storage.size();
    }

}

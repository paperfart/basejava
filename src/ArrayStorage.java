/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    public int size = 0;
    public int optimized = optSize();

    private int optSize() {
        if (size != 0) {
            return size;
        }
        return storage.length;
    }

    void clear() {
        for (int i = 0; i < optimized; i++) {
            if (storage[i] != null) {
                storage[i] = null;
            }
        }
        size = 0;
    }

    void save(Resume r) {
        storage[size] = r;
        size++;
    }

    Resume get(String uuid) {
        for (int i = 0; i < optimized; i++) {
            if (storage[i] != null && storage[i].uuid.equals(uuid)) {
                return storage[i];
            }
        }
        return null;
    }

    void delete(String uuid) {
        for (int i = 0; i < optimized; i++) {
            if (storage[i] != null && storage[i].uuid.equals(uuid)) {
                for (int j = i; j < optimized - 1; j++) {
                    storage[j] = storage[j + 1];
                    storage[j + 1] = null;
                }
                size--;
            }
        }

    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] resumes = new Resume[size];
        for (int i = 0; i < resumes.length; i++) {
            resumes[i] = storage[i];
        }
        return resumes;
    }

    int size() {
        return size;
    }
}

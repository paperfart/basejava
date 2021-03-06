package com.urise.webapp.storage;

import com.urise.webapp.exeption.NotExistStorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.model.Section;
import com.urise.webapp.model.SectionType;
import com.urise.webapp.sql.SqlHelper;
import com.urise.webapp.util.JsonParser;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {
    SqlHelper helper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        helper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void update(Resume resume) {
        helper.transactionalExecute(conn -> {
            String uuid = resume.getUuid();
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid =?")) {
                ps.setString(1, resume.getFullName());
                ps.setString(2, uuid);
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(uuid);
                }
            }
            deleteAttribute(conn, uuid, "DELETE FROM contact  WHERE resume_uuid = ?  ");
            deleteAttribute(conn, uuid, "DELETE FROM section  WHERE resume_uuid = ?  ");
            insertContacts(resume, conn);
            insertSections(resume, conn);
            return null;
        });
    }


    @Override
    public void clear() {
        helper.execute("DELETE FROM resume", ps -> {
            ps.execute();
            return null;
        });
    }

    @Override
    public void save(Resume resume) {
        helper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, resume.getFullName());
                ps.execute();
            }
            insertContacts(resume, conn);
            insertSections(resume, conn);
            return null;
        });
    }


    @Override
    public Resume get(String uuid) {
        return helper.execute("" +
                "SELECT * FROM resume r " +
                "    LEFT JOIN contact c" +
                "           ON r.uuid = c.resume_uuid " +
                "    LEFT JOIN section s" +
                "           ON r.uuid = s.resume_uuid " +
                "        WHERE r.uuid =?", ps -> {
            ps.setString(1, uuid);
            ps.execute();
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume r = new Resume(uuid, rs.getString("full_name"));
            do {
                addContacts(rs, r);
                addSections(rs, r);
            } while (rs.next());
            return r;
        });
    }


    @Override
    public void delete(String uuid) {
        helper.execute("DELETE  FROM resume  WHERE uuid =?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        Map<String, Resume> map = new LinkedHashMap<>();
        helper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("uuid");
                    Resume r = new Resume(uuid, rs.getString("full_name"));
                    map.put(uuid, r);
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact ")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    addContacts(rs, map.get(rs.getString("resume_uuid")));
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section ")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    addSections(rs, map.get(rs.getString("resume_uuid")));
                }
            }
            return null;
        });
        return new ArrayList<>(map.values());
    }

    @Override
    public int size() {
        return helper.execute("SELECT COUNT(uuid) FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        });
    }

    private void deleteAttribute(Connection conn, String uuid, String sql) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, uuid);
            ps.execute();
        }
    }

    private void addSections(ResultSet rs, Resume r) throws SQLException {
        String value = rs.getString("s_value");
        if (value != null) {
            String type = rs.getString("s_type");
            r.addSection(SectionType.valueOf(type), JsonParser.read(value, Section.class));

           /* switch (SectionType.valueOf(type)) {
                case OBJECTIVE, PERSONAL -> r.addSection(SectionType.valueOf(type), new TextSection(value));
                case ACHIEVEMENT, QUALIFICATION -> {
                    Section section = new ListSection(Arrays.asList(value.split("\n")));
                    r.addSection(SectionType.valueOf(type), section);
                }
            }*/
        }
    }

    private void addContacts(ResultSet rs, Resume r) throws SQLException {
        String value = rs.getString("value");
        if (value != null) {
            String type = rs.getString("type");
            r.addContact(ContactType.valueOf(type), value);
        }
    }

    private void insertSections(Resume resume, Connection conn) throws SQLException {
        Map<SectionType, Section> sections = resume.getSections();
        if (sections.size() > 0) {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (resume_uuid, s_type,s_value) VALUES (?,?,?)")) {
                for (Map.Entry<SectionType, Section> e : sections.entrySet()) {
                    ps.setString(1, resume.getUuid());
                    String type = e.getKey().name();
                    ps.setString(2, type);
                    Section section = e.getValue();
                    ps.setString(3, JsonParser.write(section, Section.class));

                    /*switch (SectionType.valueOf(type)) {
                        case OBJECTIVE, PERSONAL -> {
                            TextSection text = (TextSection) e.getValue();
                            ps.setString(3, text.getText());
                        }
                        case ACHIEVEMENT, QUALIFICATION -> {
                            ListSection section = (ListSection) e.getValue();
                            List<String> list = section.getList();
                            String value = String.join("\n", list);
                            ps.setString(3, value);
                        }
                    }*/
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        }
    }

    private void insertContacts(Resume resume, Connection conn) throws SQLException {
        Map<ContactType, String> contacts = resume.getContacts();
        if (contacts.size() > 0) {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type,value) VALUES (?,?,?)")) {
                for (Map.Entry<ContactType, String> e : contacts.entrySet()) {
                    ps.setString(1, resume.getUuid());
                    ps.setString(2, e.getKey().name());
                    ps.setString(3, e.getValue());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        }
    }
}

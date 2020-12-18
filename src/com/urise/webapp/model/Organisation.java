package com.urise.webapp.model;

import com.urise.webapp.util.YearMonthAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.net.URL;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Organisation implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String company;
    private URL homepage;
    private List<Position> position;

    public Organisation() {
    }

    public Organisation(String company, URL homepage, Position... positions) {
        this(company, homepage, Arrays.asList(positions));
    }

    public Organisation(String company, URL homepage, List<Position> position) {
        this.company = company;
        this.homepage = homepage;
        this.position = position;
    }

    public List<Position> getPosition() {
        return position;
    }

    public URL getHomepage() {
        return homepage;
    }

    public String getCompany() {
        return company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organisation that = (Organisation) o;
        return company.equals(that.company) &&
                Objects.equals(homepage, that.homepage) &&
                position.equals(that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(company, homepage, position);
    }

    @Override
    public String toString() {
        return (company + "\n" +
                homepage + "\n" +
                position).replace("[", "").replace("]", "").replace(",", "")
                ;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Position implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        @XmlJavaTypeAdapter(YearMonthAdapter.class)
        private YearMonth started;
        @XmlJavaTypeAdapter(YearMonthAdapter.class)
        private YearMonth finished;
        private String title;
        private String description;

        public Position() {
        }

        public Position(YearMonth started, YearMonth finished, String title, String description) {
            this.started = started;
            this.finished = finished;
            this.title = title;
            this.description = description;
        }

        public YearMonth getStarted() {
            return started;
        }

        public YearMonth getFinished() {
            return finished;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return Objects.equals(started, position.started) &&
                    Objects.equals(finished, position.finished) &&
                    Objects.equals(title, position.title) &&
                    Objects.equals(description, position.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(started, finished, title, description);
        }

        @Override
        public String toString() {
            return (started + " - " + finished + " " + title + " " +
                    description + "\n").replace("[", "").replace("]", "").replace(",", "")
                    ;
        }
    }
}

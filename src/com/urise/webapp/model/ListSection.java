package com.urise.webapp.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ListSection extends Section<List<String>> {
    private static final long serialVersionUID = 1L;

    private final List<String> list;

    public ListSection(String... strings) {
        this(Arrays.asList(strings));
    }

    public ListSection(List<String> list) {
        super(list);
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListSection that = (ListSection) o;
        return list.equals(that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }

    @Override
    public String toString() {
        return (list + "").replace("[", "").replace("]", "").replace(",", "");
    }
}
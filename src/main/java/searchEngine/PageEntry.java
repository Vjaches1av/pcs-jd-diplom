package searchEngine;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;
    private final int page;
    private final int count;

    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }

    @Override
    public int compareTo(@NotNull PageEntry o) {
        return Integer.compare(o.count, count);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        PageEntry pageEntry = (PageEntry) obj;
        return Objects.equals(pdfName, pageEntry.pdfName)
                && page == pageEntry.page
                && count == pageEntry.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pdfName, page, count);
    }

    @Override
    public String toString() {
        return "searchEngine.PageEntry{" +
                "pdfName='" + pdfName + '\'' +
                ", page=" + page +
                ", count=" + count +
                '}';
    }
}

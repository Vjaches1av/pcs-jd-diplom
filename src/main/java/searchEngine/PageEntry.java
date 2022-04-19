package searchEngine;

import org.jetbrains.annotations.NotNull;

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
    public String toString() {
        return "searchEngine.PageEntry{" +
                "pdfName='" + pdfName + '\'' +
                ", page=" + page +
                ", count=" + count +
                '}';
    }
}

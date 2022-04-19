package searchEngine;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    private final Map<String, List<PageEntry>> map = new HashMap<>();

    private @NotNull Map<String, Integer> countNumberWords(@NotNull String pdfPage) {
        String[] words = pdfPage.split("\\P{IsAlphabetic}+");

        Map<String, Integer> wordCount = new HashMap<>();
        for (String word : words) {
            word = word.toLowerCase();
            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
        }

        return wordCount;
    }

    private void analyzePdfFile(File pdfFile) {
        try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(pdfFile))) {
            for (int pageNumber = 1; pageNumber <= pdfDocument.getNumberOfPages(); pageNumber++) {
                String pdfPage = PdfTextExtractor.getTextFromPage(pdfDocument.getPage(pageNumber));
                for (Map.Entry<String, Integer> entry : countNumberWords(pdfPage).entrySet()) {
                    List<PageEntry> pageEntryList = map.getOrDefault(entry.getKey(), new ArrayList<>());
                    pageEntryList.add(new PageEntry(pdfFile.getName(), pageNumber, entry.getValue()));
                    map.put(entry.getKey(), pageEntryList);
                }
            }
        } catch (IOException e) {
            System.err.println("Файл \"" + pdfFile.getName() + "\" не найден или недоступен для чтения");
        }
    }

    private @NotNull List<File> searchPdfs(@NotNull File pathname) {
        List<File> listPdfs = new ArrayList<>();

        File[] files = pathname.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    listPdfs.addAll(searchPdfs(file));
                } else if (file.getName().endsWith(".pdf")) {
                    listPdfs.add(file);
                }
            }
        }

        return listPdfs;
    }

    public BooleanSearchEngine(File pdfsDir) {
        if (pdfsDir != null && pdfsDir.exists() && pdfsDir.isDirectory()) {
            for (File pdfFile : searchPdfs(pdfsDir)) {
                analyzePdfFile(pdfFile);
            }
        } else {
            throw new IllegalArgumentException("Указанная папка не существует или недоступна");
        }
    }

    @Override
    public @Nullable List<PageEntry> search(@NotNull String word) {
        List<PageEntry> pageEntryList = map.get(word.toLowerCase());
        if (pageEntryList != null) {
            pageEntryList.sort(PageEntry::compareTo);
        }
        return pageEntryList;
    }
}

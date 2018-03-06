package com.slpl.measure;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WordData {
    // 文字毎
    private long wordId = 1;
    private long deleteWordId = 0;
    private double inputTime = 0;
    private String inputText = "";
    private int cursorPosition = 0;
    private String date = "";
    private Integer deleteCount = 0;
    private int missDelete = 0;
    private String detectedWord = null;
    private int touchDownX = 0;
    private int touchDownY = 0;
    private int touchUpX = 0;
    private int touchUpY = 0;
    private int missTouchX = 0;
    private int missTouchY = 0;

    WordData inputTimeMils(long startTime) {
        long endTime = System.currentTimeMillis();
        this.inputTime += (endTime - startTime) / 1000.0;
        return this;
    }

    void setDateWithFormat(String format) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        this.date = sdf.format(calendar.getTime());
    }

    void incrementDeleteCount() {
        this.deleteCount++;
    }

    void missDelete(boolean miss) {
        this.missDelete = miss ? 1 : 0;
    }
}

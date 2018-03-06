package com.slpl.measure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SentenceData {
    private String date = "";
    private int deleteCountTotal = 0;
    private double inputTime = 0;
    private String detectedText;
    private String inputText;

    public SentenceData() {
    }
}

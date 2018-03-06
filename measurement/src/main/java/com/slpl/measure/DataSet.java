package com.slpl.measure;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataSet {
    private SentenceData sentenceData;
    private List<WordData> wordDataList;

    public DataSet() { // 必須
    }
}

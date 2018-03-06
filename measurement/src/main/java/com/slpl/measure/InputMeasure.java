package com.slpl.measure;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ac.slpl.slplite.database.SLPLite;
import ac.slpl.slplite.database.config.SLPLiteConfig;

public class InputMeasure {
    private final static String SOUND_ERROR = "SOUND_ERROR";
    private final String NEW_LINE = System.getProperty("line.separator");
    private long startTime = 0;
    private long mWordId = 1;
    private WordData mWordData = new WordData();
    private List<WordData> mWordDataList = new ArrayList<>();

    // SLPLite
    private SLPLite mSLPLite;
    private String DATABASE_NAME = "slplite_wear.db";
    private int DATABASE_VERSION = 1;
    private static final String TABLE_SENTENCE_DATA = "sentence_data";
    private static final String TABLE_WORD_DATA = "word_data";

    public void setupSLPLite(Context context) {
        Map<String, Class> tables = new HashMap<>();
        tables.put(TABLE_SENTENCE_DATA, SentenceData.class);
        tables.put(TABLE_WORD_DATA, WordData.class);

        SLPLiteConfig cfg = new SLPLiteConfig(DATABASE_NAME, DATABASE_VERSION, tables);
        mSLPLite = new SLPLite(context, cfg);
    }

    public void measureDeleteWord(int cursor) {
        for (int i = mWordDataList.size() - 1; i >= 0; i--) {
            WordData data = mWordDataList.get(i);
            if (data.getCursorPosition() == cursor) {
                mWordData.setMissTouchX(data.getTouchUpX());
                mWordData.setMissTouchY(data.getTouchUpY());
                mWordData.setDeleteWordId(data.getWordId());
                mWordData.incrementDeleteCount();

                // 修正で追加した文字を削除した場合
                if (data.getDeleteCount() > 0) data.missDelete(true);
                mWordDataList.set(i, data);
                return;
            }
        }
    }

    public void measureTouchDown(float touchX, float touchY) {
        mWordData.setTouchDownX((int) touchX);
        mWordData.setTouchDownY((int) touchY);
    }

    public void measureDetectSound(String detectedWord, String originalWord) {
        if (detectedWord == null || detectedWord.isEmpty() || mWordDataList.size() == 0) return;
        int last = mWordDataList.size() - 1;
        WordData data = mWordDataList.get(last);
        if (data.getDetectedWord().equals(originalWord) || originalWord.equals("memopad")) {
            data.setDetectedWord(detectedWord);
            data.inputTimeMils(startTime);
            data.setDateWithFormat("MM/dd HH:mm:ss:SSS");
            setStartTime();
        } else {
            measureDetectWord(detectedWord, SOUND_ERROR, 0, 0, 0);
        }
    }

    public void measureDetectWord(String detectedWord, String inputText, int cursor, float releaseX, float releaseY) {
        if (detectedWord == null) return;
        mWordData.setWordId(mWordId);
        mWordData.setInputText(currentInputText(inputText));
        mWordData.setCursorPosition(cursor);
        mWordData.setDateWithFormat("MM/dd HH:mm:ss:SSS");
        mWordData.setTouchUpX((int) releaseX);
        mWordData.setTouchUpY((int) releaseY);
        mWordData.setDetectedWord(detectedWord);
        mWordData.inputTimeMils(startTime);

        mWordDataList.add(mWordData);
        mWordId++;
        setStartTime();
        mWordData = new WordData();
    }

    private String currentInputText(String text) {
        String[] texts = text.split(NEW_LINE);
        if (texts.length == 0) {
            return "";
        } else {
            return texts[texts.length - 1];
        }
    }

    public void setStartTime() {
        startTime = System.currentTimeMillis();
    }

    public void save(String inputText) {
        DataSet dataSet = new DataSet();
        dataSet.setWordDataList(mWordDataList);
        dataSet.setSentenceData(createSentence(inputText));
        mSLPLite.writeData(dataSet, null);
    }

    private SentenceData createSentence(String inputText) {
        int deleteCountTotal = 0;
        double inputTime = 0;
        String detectedText = "";
        String date = mWordDataList.get(0).getDate();
        for (WordData wordData : mWordDataList) {
            deleteCountTotal += wordData.getDeleteCount();
            inputTime += wordData.getInputTime();
            detectedText += wordData.getDetectedWord();
        }

        SentenceData sentenceData = new SentenceData();
        sentenceData.setDate(date);
        sentenceData.setInputTime(inputTime);
        sentenceData.setDeleteCountTotal(deleteCountTotal);
        sentenceData.setDetectedText(detectedText);
        sentenceData.setInputText(inputText);
        return sentenceData;
    }
}

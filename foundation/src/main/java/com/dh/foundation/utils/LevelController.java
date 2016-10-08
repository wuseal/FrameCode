package com.dh.foundation.utils;

/**
 * 层级管控者
 * Created By: Seal.Wu
 * Date: 2016/6/23
 * Time: 17:54
 */
public class LevelController implements ILevelControl {

    private OnZeroLevelListener onZeroLevelListener;

    private OnStartUpLevelListener onStartUpLevelListener;

    private LevelChangeListener levelChangeListener;

    public void setLevelChangeListener(LevelChangeListener levelChangeListener) {
        this.levelChangeListener = levelChangeListener;
    }

    public void setOnZeroLevelListener(OnZeroLevelListener onZeroLevelListener) {
        this.onZeroLevelListener = onZeroLevelListener;
    }

    public void setOnStartUpLevelListener(OnStartUpLevelListener onStartUpLevelListener) {
        this.onStartUpLevelListener = onStartUpLevelListener;
    }

    public OnZeroLevelListener getOnZeroLevelListener() {
        return onZeroLevelListener;
    }

    public OnStartUpLevelListener getOnStartUpLevelListener() {
        return onStartUpLevelListener;
    }

    public LevelChangeListener getLevelChangeListener() {
        return levelChangeListener;
    }

    private int level = 0;


    @Override
    public void addOneLevel() {

        if (onStartUpLevelListener != null) {
            onStartUpLevelListener.onStartUpLevel(level);
        }
        int levelPre = level;
        level++;
        int levelAfter = level;

        if (levelChangeListener != null) {
            levelChangeListener.onLevelChange(levelPre,levelAfter);
        }
    }

    @Override
    public void reduceOneLevel() {
        int levelPre = level;
        level--;
        int levelAfter = level;

        if (levelChangeListener != null) {
            levelChangeListener.onLevelChange(levelPre,levelAfter);
        }
        if (level == 0 && onZeroLevelListener != null) {
            onZeroLevelListener.onZeroLevel(false);
        }

    }

    @Override
    public void forceInitToLevelZero() {
        level = 0;
        if (onZeroLevelListener != null) {
            onZeroLevelListener.onZeroLevel(true);
        }
    }
}

package com.example.tabadvertsbusiness.player.helpers;

import android.os.CountDownTimer;

import java.util.concurrent.TimeUnit;

public class FaceDetectionStarted extends CountDownTimer {

    public FaceDetectionStarted(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        long elapsedhour = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished));

        long elapsedMinute = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));

        long elapsedSecond = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
        String elapsedTime = ""+elapsedhour+":"+elapsedMinute+":"+elapsedSecond;
    }

    @Override
    public void onFinish() {

    }
}

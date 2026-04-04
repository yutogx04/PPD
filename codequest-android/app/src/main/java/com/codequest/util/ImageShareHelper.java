package com.codequest.util;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
public class ImageShareHelper {
    public static Bitmap generateScoreImage(String pseudo, int score, String grade, String challengeTitle) {
        int width = 600;
        int height = 400;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint bgPaint = new Paint();
        bgPaint.setColor(Color.parseColor("#0D0D1A"));
        canvas.drawRect(0, 0, width, height, bgPaint);
        Paint titlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        titlePaint.setColor(Color.parseColor("#6C63FF"));
        titlePaint.setTextSize(28);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("CodeQuest", 30, 50, titlePaint);
        Paint userPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        userPaint.setColor(Color.WHITE);
        userPaint.setTextSize(22);
        canvas.drawText(pseudo + " a obtenu", 30, 100, userPaint);
        Paint gradePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gradePaint.setTextSize(80);
        gradePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        gradePaint.setColor(getGradeColor(grade));
        canvas.drawText(grade, 250, 230, gradePaint);
        Paint scorePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(24);
        scorePaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(score + " / 100", width / 2f, 280, scorePaint);
        Paint challengePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        challengePaint.setColor(Color.parseColor("#A78BFA"));
        challengePaint.setTextSize(18);
        challengePaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(challengeTitle, width / 2f, 340, challengePaint);
        Paint ctaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ctaPaint.setColor(Color.parseColor("#64748B"));
        ctaPaint.setTextSize(14);
        ctaPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Rejoins-moi sur CodeQuest !", width / 2f, 380, ctaPaint);
        return bitmap;
    }
    public static Bitmap generateBadgeImage(String pseudo, String badgeName) {
        int width = 600;
        int height = 300;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint bgPaint = new Paint();
        bgPaint.setColor(Color.parseColor("#0D0D1A"));
        canvas.drawRect(0, 0, width, height, bgPaint);
        Paint titlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        titlePaint.setColor(Color.parseColor("#FACC15"));
        titlePaint.setTextSize(24);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("🏆 Badge Débloqué !", width / 2f, 80, titlePaint);
        Paint badgePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        badgePaint.setColor(Color.WHITE);
        badgePaint.setTextSize(36);
        badgePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        badgePaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(badgeName, width / 2f, 160, badgePaint);
        Paint userPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        userPaint.setColor(Color.parseColor("#A78BFA"));
        userPaint.setTextSize(20);
        userPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("par " + pseudo, width / 2f, 210, userPaint);
        Paint ctaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ctaPaint.setColor(Color.parseColor("#64748B"));
        ctaPaint.setTextSize(14);
        ctaPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("CodeQuest — Maîtrise le code, un défi à la fois.", width / 2f, 270, ctaPaint);
        return bitmap;
    }
    public static Uri saveBitmapAndGetUri(Context context, Bitmap bitmap) throws IOException {
        File cachePath = new File(context.getCacheDir(), "shared_images");
        cachePath.mkdirs();
        File file = new File(cachePath, "share_" + System.currentTimeMillis() + ".png");
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.close();
        return FileProvider.getUriForFile(context,
                context.getPackageName() + ".fileprovider", file);
    }
    public static void shareImage(Context context, Uri imageUri, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(intent, "Partager via"));
    }
    private static int getGradeColor(String grade) {
        switch (grade) {
            case "S": return Color.parseColor("#10B981");
            case "A": return Color.parseColor("#6C63FF");
            case "B": return Color.parseColor("#F59E0B");
            case "C": return Color.parseColor("#F97316");
            default:  return Color.parseColor("#EF4444");
        }
    }
}

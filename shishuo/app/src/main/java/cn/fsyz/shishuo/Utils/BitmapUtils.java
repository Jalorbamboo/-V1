package cn.fsyz.shishuo.Utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

/**
 * Created by JALOR on 2018/2/13.
 */

public class BitmapUtils {
    /**
     * 将矩形的Bitmap对象转换为圆形的Bitmap
     *
     * @param source:待处理的 矩形的Bitmap
     * @return ：需返回的圆形的Bitmap
     */
    public static Bitmap circleBitmap(Bitmap source) {
        //获取Bitmap的宽度
        int width = source.getWidth();
        //返回一个正方形的Bitmap对象
        Bitmap bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        //提供指定宽高的canvas
        Canvas canvas = new Canvas(bitmap);
        //提供画笔
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        //背景：在画布上绘制一个圆
        canvas.drawCircle(width / 2, width / 2, width / 2, paint);

        //设置图片相交情况下的处理方式
        //setXfermode：设置当绘制的图像出现相交情况时候的处理方式的,它包含的常用模式有哪几种
        //PorterDuff.Mode.SRC_IN 取两层图像交集部门,只显示上层图像,注意这里是指取相交叉的部分,然后显示上层图像
        //PorterDuff.Mode.DST_IN 取两层图像交集部门,只显示下层图像
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //前景：在画布上绘制一个bitmap
        canvas.drawBitmap(source, 0, 0, paint);
        return bitmap;


    }

    public static Bitmap zoom(Bitmap source, float width, float height) {
        Matrix matrix = new Matrix();
        float scaleX = width / source.getWidth();
        float scaleY = height / source.getHeight();
        matrix.postScale(scaleX, scaleY);

        Bitmap bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        return bitmap;
    }
}

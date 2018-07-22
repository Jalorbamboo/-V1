package cn.fsyz.shishuo.Utils;

import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

/**
 * Created by JALOR on 2018/2/14.
 */

public class CircleTransform implements Transformation {
    /**
     * @param source :还未处理的矩形的Bitmap对象
     * @return ：返回的是处理后的圆形Bitmap对象
     */
    @Override
    public Bitmap transform(Bitmap source) {

        //2.圆形处理
        Bitmap bitmap = BitmapUtils.circleBitmap(source);
        //必须要回收source，否则会报错
        source.recycle();
        return bitmap;//返回圆形的Bitmap对象
    }

    /**
     * 该方法没有什么实际意义，但是要保证其返回的值不能为null！
     * @return
     */
    @Override
    public String key() {
        return "";
    }
}
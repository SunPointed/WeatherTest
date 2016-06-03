package sunpointed.lqy.weathertest.Utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by lqy on 16/6/3.
 */
public class BitmapUtils {
    public static Bitmap scaleImageToFixSize(Bitmap paramBitmap, float width, float height) {
        if (paramBitmap == null) {
            return null;
        }
        width /= paramBitmap.getWidth();
        height /= paramBitmap.getHeight();
        Matrix localMatrix = new Matrix();
        localMatrix.postScale(width, height);
        return Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), localMatrix, false);
    }
}

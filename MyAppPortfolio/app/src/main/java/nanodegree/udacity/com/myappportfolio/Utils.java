package nanodegree.udacity.com.myappportfolio;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by jill on 7/20/2015.
 */
public class Utils {
    private static Toast mToast=null;
    /**
     * Show a toast message.
     */
    public static void showToast(Context context,
                                 String message) {
        // Cancel any prior toast so it doesn't block this one.
        if (mToast != null)
            mToast.cancel();
        mToast = Toast.makeText(context,message,Toast.LENGTH_LONG);
        mToast.show();
    }

}


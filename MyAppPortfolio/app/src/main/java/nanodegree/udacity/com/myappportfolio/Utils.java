package nanodegree.udacity.com.myappportfolio;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by jill on 7/20/2015.
 */
public class Utils {
    /**
     * Show a toast message.
     */
    public static void showToast(Context context,
                                 String message) {
        Toast.makeText(context,
                message,
                Toast.LENGTH_SHORT).show();
    }

}

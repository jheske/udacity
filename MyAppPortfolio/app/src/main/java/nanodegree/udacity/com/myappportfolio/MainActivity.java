package nanodegree.udacity.com.myappportfolio;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        // NOTE: The Capstone button is themed, so I am unable to
        // use its android:onClick method in the xml file.
        // If I do, the app crashes when user clicks the button.
        //
        Button btnCapstone = (Button) findViewById(R.id.btnCapstone);
        btnCapstone.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // Show toast indicating which button was clicked
        switch (view.getId()) {
            case R.id.btnSpotify:
                Utils.showToast(this, getResources().getString(R.string.spotify_toast));
                break;
            case R.id.btnScores:
                Utils.showToast(this, getResources().getString(R.string.scores_toast));
                break;
            case R.id.btnLibrary:
                Utils.showToast(this, getResources().getString(R.string.library_toast));
                break;
            case R.id.btnBuild:
                Utils.showToast(this, getResources().getString(R.string.build_toast));
                break;
            case R.id.btnXyz:
                Utils.showToast(this, getResources().getString(R.string.xyz_toast));
                break;
            case R.id.btnCapstone:
                Utils.showToast(this, getResources().getString(R.string.capstone_toast));
                break;
            default:
                Utils.showToast(this, getResources().getString(R.string.library_toast));
        }
    }
}

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
                Utils.showToast(this, "This button will launch my Spotify app!");
                break;
            case R.id.btnScores:
                Utils.showToast(this, "This button will launch my Scores app!");
                break;
            case R.id.btnLibrary:
                Utils.showToast(this, "This button will launch my Library app!");
                break;
            case R.id.btnBuild:
                Utils.showToast(this, "This button will launch my Built it Bigger app!");
                break;
            case R.id.btnXyz:
                Utils.showToast(this, "This button will launch my XZY app!");
                break;
            case R.id.btnCapstone:
                Utils.showToast(this, "This button will launch my Capstone app!");
                break;
            default:
                Utils.showToast(this, "Invalid selection! ");
        }
    }
}

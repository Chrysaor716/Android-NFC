package cs.androidnfc;

import android.os.Build;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertTrue;
import static org.robolectric.shadows.ShadowView.clickOn;


/**
 * Created by Suhan on 3/4/16.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class MainActivityTest {


    @Test
    public void clickFabButton_shouldStartWifiScan() {
        MainActivity activity = Robolectric.setupActivity(MainActivity.class);
//        activity.findViewById(R.id.fab).performClick();
        clickOn(activity.findViewById(R.id.fab));
//        onView(withId(R.id.fab)).perform(click());
        assertTrue(activity.findViewById(R.id.fab).isClickable());

    }



}

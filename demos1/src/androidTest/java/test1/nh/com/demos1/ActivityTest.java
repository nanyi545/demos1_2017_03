package test1.nh.com.demos1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import test1.nh.com.demos1.activities.ButterKnifeActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Administrator on 15-12-31.
 */
@RunWith(AndroidJUnit4.class)
public class ActivityTest {


//    @Rule
//    // third parameter is set to false which means the activity is not started automatically
//    public ActivityTestRule<SQLiteBoilerActivity> rule =
//            new ActivityTestRule(SQLiteBoilerActivity.class, true, false);
//
//    @Test
//    public void demonstrateIntentPrep() {
//        Intent intent = new Intent();
//        intent.putExtra("EXTRA", "Test");
//        rule.launchActivity(intent);
//        onView(withId(R.id.iv_insTest)).check(matches(withText("Test")));
//    }


    @Rule
    public ActivityTestRule<ButterKnifeActivity> activityTestRule =
            new ActivityTestRule<>(ButterKnifeActivity.class);
    @Test
    public void demonstrateTest() {
        onView(withId(R.id.bk_tv1)).check(matches(withText("zzzz")));
    }
    @Test
    public void demonstrateTest1() {
        onView(withId(R.id.bk_tv1)).check(matches(withText("zzzz123")));
    }
    @Test
    public void demonstrateTest2() {
        onView(withId(R.id.bk_tv1)).check(matches(withText("测试使用butterKnife")));
    }
    @Test
    public void demonstrateTest3() {
        onView(withId(R.id.bk_tv1)).check(matches(withText("测试使用butterKnife")));
    }



    @Mock
    Context context;
    @Test
    public void testIntentShouldBeCreated() {
        MockitoAnnotations.initMocks(this);
//        Context context = Mockito.mock(Context.class);
        Intent intent = ButterKnifeActivity.createQuery(context, "query", "value");
        assertNotNull(intent);
        Bundle extras = intent.getExtras();
        assertNotNull(extras);
        assertEquals("query", extras.getString("QUERY"));
        assertEquals("value", extras.getString("VALUE"));
    }


}

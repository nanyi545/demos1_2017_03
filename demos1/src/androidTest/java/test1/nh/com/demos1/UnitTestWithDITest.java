package test1.nh.com.demos1;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;
import test1.nh.com.demos1.testDemo.UnitTestWithDIActivity;
import test1.nh.com.demos1.testDemo.di_pack.components.IComponent;
import test1.nh.com.demos1.testDemo.di_pack.modules.DebugDataModule;
import test1.nh.com.demos1.testDemo.models.ItestData;
import test1.nh.com.demos1.utils.DMapplication;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Administrator on 16-1-12.
 */
@RunWith(AndroidJUnit4.class)
public class UnitTestWithDITest {

    @Inject
    ItestData mockTestData;

    // whether to replace the injected Dependency in target activity (UnitTestWithDIActivity) with a mock ??
    boolean mockState=true;

    @Singleton
    @Component(modules = DebugDataModule.class)
    public interface TestComponent extends IComponent {
        void inject(UnitTestWithDITest myTest);
    }


    @Rule
    public ActivityTestRule<UnitTestWithDIActivity> activityRule = new ActivityTestRule<>(
            UnitTestWithDIActivity.class,
            true,     // initialTouchMode
            false);   // launchActivity. False so we can customize the intent per test method


    @Before
    public void setUp() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        DMapplication app
                = (DMapplication) instrumentation.getTargetContext().getApplicationContext();
        TestComponent component = DaggerUnitTestWithDITest_TestComponent.builder()
                .debugDataModule(new DebugDataModule(mockState))
                .build();
        app.setDateComponent(component);   // set the component in DMapplication--> so that the UnitTestWithDIActivity will be injected with the mockDataSource
        component.inject(this);
    }


    @Test
    public void aMockString() {
        if (mockState)
        Mockito.when(mockTestData.obtainTestString("")).thenReturn("this is mock string");

        activityRule.launchActivity(new Intent());
        onView(withId(R.id.unit_tv1))
                .check(matches(withText("this is mock string")));
    }


    @Test
    public void aMockString2() {
        if (mockState)
        Mockito.when(mockTestData.obtainTestString("")).thenReturn("this is mock string");

        activityRule.launchActivity(new Intent());
        onView(withId(R.id.unit_tv1))
                .check(matches(withText("this is obtained from internet")));
    }


}

package com.miko.zd.anquanjianchanative;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import  com.robotium.solo.Solo;
import  com.miko.zd.anquanjianchanative.MainActivity;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private static String username = "刘德华";
    private static String year = "2017年";
    private static String month = "7月";
    private static String day = "18日";

    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    private Solo solo;

    @Before
    public void Setup()
    {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),activityActivityTestRule.getActivity());
    }

    @After
    public void TearDown()
    {
        solo.finishOpenedActivities();
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.miko.zd.anquanjianchanative", appContext.getPackageName());
    }

    @Test
    public void testUnlogin()
    {
        solo.unlockScreen();
        EditText name = (EditText)solo.getView(R.id.id_ed_user);
        solo.clearEditText(name);
        solo.enterText(name,username);

        TextView y = (TextView)solo.getView(R.id.id_tv_year_login);
        solo.clickOnView((View)y);

        EditText yy = (EditText)solo.getEditText("年");
        solo.clearEditText(yy);
        solo.enterText(yy,year);
        EditText mm = (EditText)solo.getEditText("月");
        solo.clearEditText(mm);
        solo.enterText(mm,month);
        EditText dd = (EditText)solo.getEditText("日");
        solo.clearEditText(dd);
        solo.enterText(dd,day);

        solo.clickOnButton("完成");

        Button login = (Button)solo.getView(R.id.id_bt_login);
        solo.clickOnView((View)login);


    }

}

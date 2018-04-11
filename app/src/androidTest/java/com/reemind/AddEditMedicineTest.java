package com.reemind;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import com.reemind.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddEditMedicine {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void addEditMedicine() {
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(60000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
        
        ViewInteraction pd = onView(
allOf(withText("Sign in"),
withParent(withId(R.id.mGoogleLogin)),
isDisplayed()));
        pd.perform(click());
        
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(3592457);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
        
        ViewInteraction floatingActionButton = onView(
allOf(withId(R.id.fab_button), isDisplayed()));
        floatingActionButton.perform(click());
        
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(3589603);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
        
        ViewInteraction appCompatEditText = onView(
allOf(withId(R.id.drug_name), isDisplayed()));
        appCompatEditText.perform(click());
        
        ViewInteraction appCompatEditText2 = onView(
allOf(withId(R.id.drug_name), isDisplayed()));
        appCompatEditText2.perform(replaceText("Panadol"), closeSoftKeyboard());
        
        ViewInteraction appCompatEditText3 = onView(
withId(R.id.drug_description));
        appCompatEditText3.perform(scrollTo(), replaceText("pain killer"), closeSoftKeyboard());
        
        ViewInteraction linearLayout = onView(
withId(R.id.icon_select));
        linearLayout.perform(scrollTo(), click());
        
        ViewInteraction appCompatEditText4 = onView(
allOf(withId(R.id.drug_description), withText("pain killer")));
        appCompatEditText4.perform(scrollTo(), click());
        
        ViewInteraction appCompatImageButton = onView(
withClassName(is("android.support.v7.widget.AppCompatImageButton")));
        appCompatImageButton.perform(scrollTo(), click());
        
        ViewInteraction appCompatImageButton2 = onView(
withClassName(is("android.support.v7.widget.AppCompatImageButton")));
        appCompatImageButton2.perform(scrollTo(), click());
        
        ViewInteraction appCompatImageButton3 = onView(
withClassName(is("android.support.v7.widget.AppCompatImageButton")));
        appCompatImageButton3.perform(scrollTo(), click());
        
        pressBack();
        
        ViewInteraction appCompatTextView = onView(
allOf(withId(R.id.start_date), withText("11/4/2018")));
        appCompatTextView.perform(scrollTo(), click());
        
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(40);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
        
        ViewInteraction appCompatButton = onView(
allOf(withId(R.id.ok), withText("OK"), isDisplayed()));
        appCompatButton.perform(click());
        
        ViewInteraction appCompatTextView2 = onView(
allOf(withId(R.id.start_time), withText("15:55")));
        appCompatTextView2.perform(scrollTo(), click());
        
        ViewInteraction appCompatButton2 = onView(
allOf(withId(R.id.ok), withText("OK"), isDisplayed()));
        appCompatButton2.perform(click());
        
        ViewInteraction appCompatEditText5 = onView(
withId(R.id.frequency_input));
        appCompatEditText5.perform(scrollTo(), replaceText("3"), closeSoftKeyboard());
        
        ViewInteraction actionMenuItemView = onView(
allOf(withId(R.id.item_save), withContentDescription("Cart"), isDisplayed()));
        actionMenuItemView.perform(click());
        
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(3530111);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
        
        ViewInteraction appCompatButton3 = onView(
allOf(withId(R.id.month_button), withText("Select month"), isDisplayed()));
        appCompatButton3.perform(click());
        
        ViewInteraction appCompatTextView3 = onView(
allOf(withId(R.id.title), withText("March"), isDisplayed()));
        appCompatTextView3.perform(click());
        
        ViewInteraction appCompatTextView4 = onView(
allOf(withId(R.id.name_view), withText("Levit Nudi"), isDisplayed()));
        appCompatTextView4.perform(click());
        
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(3584716);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
        
        ViewInteraction appCompatImageView = onView(
withClassName(is("android.support.v7.widget.AppCompatImageView")));
        appCompatImageView.perform(scrollTo(), click());
        
        }

    }

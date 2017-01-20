package in.pathri.codenvydownload.screens;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import in.pathri.codenvydownload.R;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BuildScreenTest {

    @Rule
    public ActivityTestRule<BuildScreen> mActivityTestRule = new ActivityTestRule<>(BuildScreen.class);

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @Test
    public void buildScreenTest() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText("Setup"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction linearLayout = onView(
                allOf(childAtPosition(
                        withId(android.R.id.list),
                        0),
                        isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.login_text), isDisplayed()));
        editText.perform(click());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.login_text), isDisplayed()));
        editText2.perform(replaceText("pathrikumark@gmail.com"), closeSoftKeyboard());

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.password_text), isDisplayed()));
        editText3.perform(replaceText("pkk211288"), closeSoftKeyboard());

        ViewInteraction button = onView(
                allOf(withId(android.R.id.button1), withText("Login"),
                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        button.perform(click());

        ViewInteraction linearLayout2 = onView(
                allOf(childAtPosition(
                        withId(android.R.id.list),
                        1),
                        isDisplayed()));
        linearLayout2.perform(click());

        ViewInteraction checkedTextView = onView(
                allOf(withId(android.R.id.text1), withText("MyAndroid"),
                        childAtPosition(
                                allOf(withClassName(is("com.android.internal.app.AlertController$RecycleListView")),
                                        withParent(withClassName(is("android.widget.FrameLayout")))),
                                0),
                        isDisplayed()));
        checkedTextView.perform(click());

        ViewInteraction linearLayout3 = onView(
                allOf(childAtPosition(
                        withId(android.R.id.list),
                        2),
                        isDisplayed()));
        linearLayout3.perform(click());

        ViewInteraction checkedTextView2 = onView(
                allOf(withId(android.R.id.text1), withText("HelloWorld"),
                        childAtPosition(
                                allOf(withClassName(is("com.android.internal.app.AlertController$RecycleListView")),
                                        withParent(withClassName(is("android.widget.FrameLayout")))),
                                0),
                        isDisplayed()));
        checkedTextView2.perform(click());

        ViewInteraction linearLayout4 = onView(
                allOf(childAtPosition(
                        withId(android.R.id.list),
                        3),
                        isDisplayed()));
        linearLayout4.perform(click());

        ViewInteraction checkedTextView3 = onView(
                allOf(withId(android.R.id.text1), withText("newMaven"),
                        childAtPosition(
                                allOf(withClassName(is("com.android.internal.app.AlertController$RecycleListView")),
                                        withParent(withClassName(is("android.widget.FrameLayout")))),
                                0),
                        isDisplayed()));
        checkedTextView3.perform(click());

        pressBack();

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.build), withText("Build, Download and Install"), isDisplayed()));
        appCompatButton.perform(click());

    }
}

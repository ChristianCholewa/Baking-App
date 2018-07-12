package com.example.android.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityBasicTest {
    @Rule public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickRecipeButtonWorks(){

        onView(withRecyclerView(R.id.rv_recipes)
                .atPositionOnView(0, R.id.rv_item_titel))
                .check(matches(withText("Nutella Pie")));

        onView(withRecyclerView(R.id.rv_recipes)
                .atPositionOnView(1, R.id.rv_item_titel))
                .check(matches(withText("Brownies")));

        // open Nutella Pie
        onView(withId(R.id.rv_recipes)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withRecyclerView(R.id.rv_steplist)
                .atPositionOnView(0, R.id.rv_step_item_titel))
                .check(matches(withText("Ingredients")));

        onView(withRecyclerView(R.id.rv_steplist)
                .atPositionOnView(1, R.id.rv_step_item_titel))
                .check(matches(withText("Recipe Introduction")));

        // open Ingredients of Nutella Pie
        onView(withId(R.id.rv_steplist)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withRecyclerView(R.id.rv_ingredients_list)
                .atPositionOnView(0, R.id.ingredients_item_title))
                .check(matches(withText("Graham Cracker crumbs")));
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }
}





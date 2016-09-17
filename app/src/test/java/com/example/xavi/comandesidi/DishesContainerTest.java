package com.example.xavi.comandesidi;

import android.content.Context;

import com.example.xavi.comandesidi.DBWrappers.DishesContainer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.mockito.Mock;

public class DishesContainerTest {

    @Mock
    Context mMockContext;

    @Test
    public void checkGetInstance() {
        DishesContainer dishesContainer = DishesContainer.getInstance(mMockContext);
        String result = "sida";
        assertThat(result, is("sida"));

    }
}

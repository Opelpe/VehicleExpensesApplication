package com.pepe.vehicleexpensesapplication.ui.feautures.history;


import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import android.content.Context;
import android.content.SharedPreferences;

public class HistoryPresenterTest {

    private HistoryPresenter presenter;

    @Mock
    private SharedPreferences sharedPrefs;

    @Mock
    private Context context;

    @Mock
    private HistoryContract.View view;

    @Before
    public void setUp() throws Exception {
        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPrefs);
//       presenter = new HistoryPresenter(view, context);
    }

//    public void testOnViewCreated() {
//    }
//
@Test
    public void testOnFloatingRefillButtonClicked() {
        presenter.onFloatingRefillButtonClicked();
//        verify(view).setHistoryFragmentToolbar();
        verify(view).startRefillActivity();

    }
//
//    public void testCheckIsAnonymous() {
//    }

    @Test
    public void a() {
        Assert.assertTrue(true);

    }
}
package com.appcali.pantalla_principal.ui.AsisAdministrador;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AsisAdminViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AsisAdminViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
package com.appcali.pantalla_principal.ui.AsisUsuarios;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AsisUsuariosViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AsisUsuariosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
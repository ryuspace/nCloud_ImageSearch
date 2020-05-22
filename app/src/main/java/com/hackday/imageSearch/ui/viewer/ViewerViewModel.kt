package com.hackday.imageSearch.ui.viewer

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.hackday.imageSearch.model.PhotoInfo
import com.hackday.imageSearch.repository.PhotoInfoRepositoryImpl
import com.hackday.imageSearch.repository.PhotoInfoRepositoryInjector
import com.hackday.imageSearch.ui.photo.PhotoViewModel
import com.hackday.imageSearch.ui.photoinfo.PhotoInfoViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver

class ViewerViewModel : ViewModel() {

    val _vphoto = MutableLiveData<PhotoInfo>()
    val vphoto : LiveData<PhotoInfo> = _vphoto

    val photoInfoRepository = PhotoInfoRepositoryInjector.getPhotoRepositoryImpl()

    fun getPhotoByUri(uri : String) = photoInfoRepository.getPhotoByUri(uri)

    fun inputdialog(photo : PhotoInfo){
        _vphoto.value = photo
    }

}
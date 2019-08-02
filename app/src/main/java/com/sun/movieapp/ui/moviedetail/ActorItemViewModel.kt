package com.sun.movieapp.ui.moviedetail

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.sun.movieapp.base.BaseViewModel
import com.sun.movieapp.model.Actor

class ActorItemViewModel: BaseViewModel() {
    private val mActor: MutableLiveData<Actor> = MutableLiveData()
    var listener: View.OnClickListener? = null

    fun bind(actor: Actor) {
        mActor.value = actor
    }

    fun getName(): LiveData<String> = Transformations.map(mActor, { it.name })
    fun getProfilePath(): LiveData<String> = Transformations.map(mActor, { it.profilePath })

    fun onItemClick(view: View?) {
        listener?.onClick(view)
    }
}

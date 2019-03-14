package com.openclassrooms.realestatemanager.ui.base

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject

@SuppressLint("Registered")
abstract class BaseUiActivity<A, R, T : BaseTranslator<A, R>> : BaseActivity() {

    private val translator: T by lazy { translator() }
    protected val actions: PublishSubject<A> = PublishSubject.create()
    private val disposables = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        translator.sendAction(actions).addTo(disposables)
        translator.getUiModel()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    render(it)
                }
                .addTo(disposables)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    abstract fun render(ui: R)

    abstract fun translator(): T
}
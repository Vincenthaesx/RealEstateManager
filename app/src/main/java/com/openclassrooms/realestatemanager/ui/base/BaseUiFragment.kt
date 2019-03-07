package com.openclassrooms.realestatemanager.ui.base

import android.os.Bundle
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject

abstract class BaseUiFragment<A, R, T : BaseTranslator<A, R>> : BaseFragment() {

    private val translator: T by lazy { translator() }
    protected val actions: PublishSubject<A> = PublishSubject.create()
    private val disposables = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        translator.sendAction(actions).addTo(disposables)
        translator.getUiModel()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    render(it)
                }
                .addTo(disposables)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
    }

    abstract fun render(ui: R)

    abstract fun translator(): T

}
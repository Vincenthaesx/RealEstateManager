package com.openclassrooms.realestatemanager.ui.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.openclassrooms.realestatemanager.RealEstateManagerApplication
import com.openclassrooms.realestatemanager.di.RepositoryComponent
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

abstract class BaseTranslator<R, U> : ViewModel(), RepositoryComponent.Injectable {
    private val actions: Subject<R> = PublishSubject.create<R>()

    private val state: Observable<U> = actions.flatMap { it ->
        Observable.just(it)
                .publish {
                    it.reduce()
                }
    }
            // todo use share or replay(1).autoConnect() or RxReplayingShare
            .share()
            .distinctUntilChanged()

    abstract fun Observable<R>.reduce(): Observable<U>

    open fun sendAction(action: Observable<R>): Disposable {
        // to make sure subject won't receive onError or onComplete from the action
        return action.subscribe(actions::onNext)
    }

    open fun getUiModel(): Observable<U> {
        return state
    }

    companion object Factory : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val t = super.create(modelClass)
            if (t is RepositoryComponent.Injectable) {
                t.inject(RealEstateManagerApplication.repoComponent)
            }
            return t
        }

    }
}

inline fun <reified VM : ViewModel> FragmentActivity.getViewModel(): VM {
    return ViewModelProviders.of(this, BaseTranslator).get(VM::class.java)
}

inline fun <reified VM : ViewModel> FragmentActivity.getViewModel(id: String): VM {
    return ViewModelProviders.of(this, BaseTranslator).get(id, VM::class.java)
}

inline fun <reified VM : ViewModel> Fragment.getViewModel(id: String): VM {
    return ViewModelProviders.of(requireActivity(), BaseTranslator).get(id, VM::class.java)
}

inline fun <reified VM : ViewModel> Fragment.getViewModel(): VM {
    return ViewModelProviders.of(this, BaseTranslator).get(VM::class.java)
}

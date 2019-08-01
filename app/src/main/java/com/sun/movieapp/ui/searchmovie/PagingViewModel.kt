package com.sun.movieapp.ui.searchmovie

import androidx.lifecycle.MutableLiveData
import com.sun.movieapp.base.BaseViewModel
import com.sun.movieapp.utils.extensions.async
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import io.reactivex.subjects.BehaviorSubject

class PagingViewModel(
    private val mCurrentPage: BehaviorSubject<Int>
): BaseViewModel() {

    private val mTotalPages: BehaviorSubject<Int> = BehaviorSubject.create()
    val isThreeHidden: MutableLiveData<Boolean> = MutableLiveData()
    val isTwoHidden: MutableLiveData<Boolean> = MutableLiveData()
    val pageOne: MutableLiveData<String> = MutableLiveData()
    val pageTwo: MutableLiveData<String> = MutableLiveData()
    val pageThree: MutableLiveData<String> = MutableLiveData()
    val highlightIndex: MutableLiveData<Int> = MutableLiveData()
    data class Input(
        val firstButton: Observable<Unit>,
        val lastButton: Observable<Unit>,
        val nextButton: Observable<Unit>,
        val prevButton: Observable<Unit>,
        val pageOneButton: Observable<Unit>,
        val pageTwoButton: Observable<Unit>,
        val pageThreeButton: Observable<Unit>
    )

    fun updateTotalPages(totalPages: Int) {
        mTotalPages.onNext(totalPages)
        mCurrentPage.onNext(1)
    }

    fun transform(input: Input) {
        rx {
            mCurrentPage
                .withLatestFrom(mTotalPages, BiFunction { current: Int, total: Int ->
                    (current / 3 == total / 3 &&
                            current % 3 != 0 &&
                            total % 3 != 0) || total < 3
                })
                .async()
                .subscribe {
                    isThreeHidden.value = !it
                }
        }
        rx {
            mCurrentPage
                .withLatestFrom(mTotalPages, BiFunction { current: Int, total: Int ->
                    (current / 3 == total / 3 &&
                            current % 3 != 0 &&
                            total % 3 == 1) || total < 3
                })
                .async()
                .subscribe {
                    isTwoHidden.value = !it
                }
        }
        rx {
            input.firstButton
                .withLatestFrom(mCurrentPage, BiFunction { _: Unit, current: Int ->
                    if (current != 1) mCurrentPage.onNext(1)
                })
                .async()
                .subscribe({}, {})
        }
        rx {
            input.lastButton
                .withLatestFrom(mCurrentPage, mTotalPages, Function3 { _: Unit, current: Int, total: Int ->
                    if (current != total) mCurrentPage.onNext(total)
                })
                .async()
                .subscribe({}, {})

        }
        rx {
            input.prevButton
                .withLatestFrom(mCurrentPage, BiFunction { _: Unit, current: Int ->
                    if (current > 1) mCurrentPage.onNext(current - 1)
                })
                .async()
                .subscribe({}, {})
        }
        rx {
            input.nextButton
                .withLatestFrom(mCurrentPage, mTotalPages, Function3 { _: Unit, current: Int, total: Int ->
                    if (current < total) mCurrentPage.onNext(current + 1)
                })
                .async()
                .subscribe({}, {})
        }
        rx {
            input.pageOneButton
                .withLatestFrom(mCurrentPage, BiFunction { _: Unit, current: Int ->
                    val index = current / 3
                    val page = if (current == 3 * index) { 3 * (index - 1) + 1 }  else { 3 * index + 1 }
                    if (current != page) mCurrentPage.onNext(page)
                })
                .async()
                .subscribe({}, {})
        }
        rx {
            input.pageTwoButton
                .withLatestFrom(mCurrentPage, BiFunction { _: Unit, current: Int ->
                    val index = current / 3
                    val page = if (current == 3 * index) { 3 * (index - 1) + 2 }  else { 3 * index + 2 }
                    if (current != page) mCurrentPage.onNext(page)
                })
                .async()
                .subscribe({}, {})
        }
        rx {
            input.pageThreeButton
                .withLatestFrom(mCurrentPage, BiFunction { _: Unit, current: Int ->
                    val index = current / 3
                    val page = if (current == 3 * index) { 3 * (index - 1) + 3 }  else { 3 * index + 3 }
                    if (current != page) mCurrentPage.onNext(page)
                })
                .async()
                .subscribe({}, {})
        }
        rx {
            mCurrentPage
                .async()
                .map {
                    val index = it / 3
                    return@map if (it == 3 * index) { "${(3 * (index - 1) + 1)}" } else { "${(3 * index + 1)}" }
                }
                .subscribe {
                    pageOne.value = it
                }
        }
        rx {
            mCurrentPage
                .async()
                .map {
                    val index = it / 3
                    return@map if (it == 3 * index) { "${(3 * (index - 1) + 2)}" } else { "${(3 * index + 2)}" }
                }
                .subscribe {
                    pageTwo.value = it
                }
        }
        rx {
            mCurrentPage
                .async()
                .map {
                    val index = it / 3
                    return@map if (it == 3 * index) { "${(3 * (index - 1) + 3)}" } else { "${(3 * index + 3)}" }
                }
                .subscribe {
                    pageThree.value = it
                }
        }
        rx {
            mCurrentPage
                .async()
                .subscribe {
                    highlightIndex.value = it % 3
                }
        }
    }
}

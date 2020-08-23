package com.example.myecommerce

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.addTo(compositeDisposable: CompositeDisposable) = Unit.also { compositeDisposable.add(this) }
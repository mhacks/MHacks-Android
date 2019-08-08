package org.mhacks.app.usecase

import androidx.lifecycle.MediatorLiveData
import io.reactivex.disposables.CompositeDisposable
import org.mhacks.app.data.models.common.RetrofitException
import org.mhacks.app.data.models.Outcome
import retrofit2.HttpException
import java.io.IOException

abstract class UseCase<in P, R> {

    protected val disposables = CompositeDisposable()

    protected val resultMediator = MediatorLiveData<Outcome<R>>()

    open fun observe(): MediatorLiveData<Outcome<R>> = resultMediator

    protected fun asRetrofitException(throwable: Throwable): RetrofitException? {
        if (throwable is HttpException) {
            val response = throwable.response()
            if (throwable.code() == 401) {
                return RetrofitException.unauthorizedError(
                        response?.raw()!!.request.url.toString(), response)
            }
            return RetrofitException.httpError(
                    response?.raw()!!.request.url.toString(), response)
        }
        return if (throwable is IOException) {
            RetrofitException.networkError(throwable)
        } else null
    }

    open fun onCleared() {
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }
}
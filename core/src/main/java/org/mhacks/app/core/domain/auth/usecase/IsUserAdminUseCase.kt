package org.mhacks.app.core.domain.auth.usecase

import io.reactivex.Single
import org.mhacks.app.core.domain.auth.AuthRepository
import org.mhacks.app.core.usecase.SingleUseCase
import timber.log.Timber
import javax.inject.Inject

class IsUserAdminUseCase @Inject constructor(
        private val authRepository: AuthRepository
) : SingleUseCase<Unit, Boolean>() {

    override fun getSingle(parameters: Unit): Single<Boolean> =
            authRepository
                    .getCachedAuth()
                    .map {
                        Timber.d("User is admin: %s", it.isAdmin)
                        it.isAdmin
                    }
}
package org.mhacks.app.eventlibrary.usecase

import io.reactivex.Single
import org.mhacks.app.core.usecase.SingleUseCase
import org.mhacks.app.eventlibrary.data.model.Event
import org.mhacks.app.eventlibrary.EventRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetAndCacheEventsUseCase @Inject constructor(
        private val eventsRepository: EventRepository
) : SingleUseCase<Unit, List<Event>>() {

    override fun getSingle(parameters: Unit) =
            eventsRepository
                    .getEventCache()
                    .delay(400, TimeUnit.MILLISECONDS)
                    .flatMap {
                        if (it.isEmpty())
                            eventsRepository.getEventRemote()
                                    .doOnSuccess { response ->
                                        eventsRepository.deleteAndUpdateEvents(response.events)
                                    }
                                    .map { response ->
                                        response.events
                                    }
                        else Single.just(it)
                    }
}
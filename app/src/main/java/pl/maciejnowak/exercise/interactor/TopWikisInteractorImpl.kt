package pl.maciejnowak.exercise.interactor

import pl.maciejnowak.exercise.network.FandomApiService
import pl.maciejnowak.exercise.network.model.ExpandedWikiaResultSet
import java.io.IOException

class TopWikisInteractorImpl : TopWikisInteractor {

    private val service by lazy { FandomApiService.create() }

    override fun fetch(limit: Int): ExpandedWikiaResultSet? {
        return try {
            val response = service.getTopWikis(limit).execute()
            if(response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: IOException) {
            null
        }
    }
}
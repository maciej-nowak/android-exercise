package pl.maciejnowak.exercise.interactor

import pl.maciejnowak.exercise.network.Network
import pl.maciejnowak.exercise.network.model.ExpandedWikiaResultSet
import java.io.IOException

class TopWikisInteractorImpl : TopWikisInteractor {

    override fun fetch(limit: Int): ExpandedWikiaResultSet? {
        return try {
            val response = Network.fandomService.getTopWikis(limit).execute()
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
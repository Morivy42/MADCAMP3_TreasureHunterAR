package io.madcamp.treasurehunterar.collection

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CollectionRepository constructor(
    private val collectionService: CollectionService
) {
    suspend fun getCollections() : List<Collection> =
        collectionService.getCollections()
}
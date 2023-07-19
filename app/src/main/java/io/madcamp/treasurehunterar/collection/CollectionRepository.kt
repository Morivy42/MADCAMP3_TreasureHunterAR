package io.madcamp.treasurehunterar.collection

class CollectionRepository constructor(
    private val collectionService: CollectionService
) {
    suspend fun getCollections() : List<Collection> =
        collectionService.getCollections()
}
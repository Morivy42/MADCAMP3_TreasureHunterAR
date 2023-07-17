package io.madcamp.treasurehunterar.collection

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    collectionRepository: CollectionRepository
) : ViewModel() {
    private val collectionId: String = savedStateHandle["cid"] ?:
    throw IllegalArgumentException("Missing collection ID")

    private val _collection = MutableLiveData<Collection>()
    val collection = _collection as LiveData<Collection>

    private val _collectionList = MutableLiveData<List<Collection>>()
    val collectionList = _collectionList as LiveData<List<Collection>>

    init {
        viewModelScope.launch {
            try {
                // Calling the repository is safe as it moves execution off
                // the main thread
                val collection = collectionRepository.getCollectionById(collectionId)
                _collection.value = collection
                val collectionList = collectionRepository.getCollectionList()
                _collectionList.value = collectionList
            } catch (error: Exception) {
                // Show error message to user
                Log.d("CollectionViewModel", error.toString())
            }

        }
    }

}
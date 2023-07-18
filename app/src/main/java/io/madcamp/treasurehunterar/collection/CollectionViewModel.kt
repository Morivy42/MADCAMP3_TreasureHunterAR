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

    private val _updateResult = MutableLiveData<Boolean>()
    val updateResult: LiveData<Boolean> = _updateResult

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
/*
    fun updateCollectionList(item: String) {
        val currentList = _collectionList.value?.toMutableList()
        currentList?.find { it.name == item }?.let {
            it.copy(isFound = true)
            _updateResult.value = true // Update successful
            _collectionList.value = currentList // Update the collectionList with the modified list
        } ?: run {
            _updateResult.value = false // Item not found, update failed
        }
    }
 */
}

//class DemoScreenViewModel : ViewModel() {
//    sealed class State {
//        object Loading: State()
//        data class Data(val data: String): State()
//    }
//
//    private var _state = MutableStateFlow<State>(State.Loading)
//    val state = _state.asStateFlow()
//
//    init {
//        viewModelScope.launch {
//            while (isActive) {
//                val data = makeDataLoadCallOnEntry()
//                _state.value = State.Data(data)
//                // wait one minute and repeat your request
//                delay(60 * 1000L)
//            }
//        }
//    }
//
//    suspend fun makeDataLoadCallOnEntry(): String {
//        delay(1000)
//        return "Hello world"
//    }
//}
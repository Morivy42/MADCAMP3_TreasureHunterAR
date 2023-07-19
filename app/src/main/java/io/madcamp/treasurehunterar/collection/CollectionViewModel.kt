package io.madcamp.treasurehunterar.collection

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

sealed interface CollectionUiState {
    data class Success(val Collections: List<Collection>) : CollectionUiState
    object Error : CollectionUiState
    object Loading : CollectionUiState
}
class CollectionViewModel : ViewModel() {

    var collectionUiState : CollectionUiState by mutableStateOf(CollectionUiState.Loading)

    init {
        getCollections()
    }

    private fun getCollections() {
        viewModelScope.launch {
            collectionUiState = CollectionUiState.Loading
            collectionUiState = try {
                val collectionList = CollectionApi.retrofitService.getCollections()
                CollectionUiState.Success(
                    collectionList
                )
            } catch (e: IOException) {
                Log.d("GetCollectionsException", e.toString())
                CollectionUiState.Error
            } catch (e: HttpException) {
                Log.d("GetCollectionsException", e.toString())
                CollectionUiState.Error
            }
        }
    }

    fun reloadCollections() {
        viewModelScope.launch {
            collectionUiState = CollectionUiState.Loading
            collectionUiState = try {
                val collectionList = CollectionApi.retrofitService.getCollections()
                CollectionUiState.Success(collectionList)
            } catch (e: IOException) {
                Log.d("GetCollectionsException", e.toString())
                CollectionUiState.Error
            } catch (e: HttpException) {
                Log.d("GetCollectionsException", e.toString())
                CollectionUiState.Error
            }
        }
    }
}

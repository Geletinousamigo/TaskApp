package com.nikhil.task.repository

import androidx.lifecycle.MutableLiveData
import com.nikhil.task.network.SearchApiService
import com.nikhil.task.network.SearchRequestBody
import com.nikhil.task.network.SearchResponseItem
import com.nikhil.task.utils.NetworkResult
import org.json.JSONObject
import java.net.ConnectException
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val api: SearchApiService
) {
    private val _searchData = MutableLiveData<NetworkResult<List<SearchResponseItem?>?>>()
    val searchData get() = _searchData

    suspend fun getSearchResults(query: String) {

        _searchData.postValue(NetworkResult.Loading())

        try {
            val response = api.getSearchQueryResult(searchRequestBody = SearchRequestBody(query))
            if (response.isSuccessful && response.body()!=null) {
                _searchData.postValue(NetworkResult.Success(response.body()))
            }
            else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _searchData.postValue(NetworkResult.Error(errorObj.getString("Message")))
            }
        } catch (e:ConnectException) {
            _searchData.postValue(NetworkResult.Error("Network Problem!"))
        }
        catch (e: Exception) {
            _searchData.postValue(NetworkResult.Error(e.message))
        }
    }
}
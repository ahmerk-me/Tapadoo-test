package com.tapadootest.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cells.library.networking.TapadooAPICall
import com.tapadootest.app.models.books.BookItem
import com.tapadootest.app.models.books.GetBooks
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BooksViewModel :ViewModel() {

    var booksArrayList: MutableLiveData<ArrayList<BookItem>> = MutableLiveData()
    var bookData: MutableLiveData<BookItem> = MutableLiveData()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    var isError: MutableLiveData<Boolean> = MutableLiveData()
    var errorText = "Some Error Occurred"


    fun getBooks() {

        isLoading.value = true

        val apiCall: Call<GetBooks?>? = TapadooAPICall.apiInterface()?.getBooks()

        apiCall?.enqueue(object : Callback<GetBooks?> {
            override fun onResponse(
                call: Call<GetBooks?>,
                response: Response<GetBooks?>
            ) {

                if (response.isSuccessful) {

                    booksArrayList.value = response.body()!!

                }

                isLoading.value = false

            }

            override fun onFailure(call: Call<GetBooks?>, t: Throwable) {

                isError.value = true
                errorText = t.toString()

                isLoading.value = false

            }

        })

    }


    fun getBookById(id: Int) {

        isLoading.value = true

        val apiCall: Call<BookItem?>? = TapadooAPICall.apiInterface()?.getBookById(id)

        apiCall?.enqueue(object : Callback<BookItem?> {
            override fun onResponse(
                call: Call<BookItem?>,
                response: Response<BookItem?>
            ) {

                if (response.isSuccessful && response.body() != null) {

                    bookData.value = response.body()!!

                }

                isLoading.value = false

            }

            override fun onFailure(call: Call<BookItem?>, t: Throwable) {

                isError.value = true
                errorText = t.toString()

                isLoading.value = false

            }

        })
    }
}
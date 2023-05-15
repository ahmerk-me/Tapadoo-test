package com.tapadootest.app.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tapadootest.app.R
import com.tapadootest.app.classes.*
import com.tapadootest.app.databinding.FragmentBookDetailBinding
import com.tapadootest.app.models.books.BookItem
import com.tapadootest.app.view.activity.MainActivity
import com.tapadootest.app.viewmodel.BooksViewModel

class BookDetailFragment(private val act: MainActivity) : Fragment() {

    private val TAG = BookDetailFragment::class.java.simpleName

    lateinit var mSessionManager: SessionManager

    lateinit var languageSessionManager: LanguageSessionManager

    lateinit var binding: FragmentBookDetailBinding

    lateinit var viewModel: BooksViewModel

    var bookId: Int = -1


    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println("Entered BookDetailFragment !!!====<>>>>>")

        try {

            mSessionManager = SessionManager(act)

            languageSessionManager = LanguageSessionManager(act)

            if (arguments?.containsKey("bookId") == true) {

                bookId = arguments?.getInt("bookId") ?: -1

            }

            viewModel = ViewModelProvider(this)[BooksViewModel::class.java]

        } catch (e: Exception) {

            Log.e(
                TAG + " onCreateLine>>LineNumber: " +
                        Thread.currentThread().stackTrace[2].lineNumber, e.message.toString()
            )

        }
    }


    @SuppressLint("LongLogTag")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        try {

            binding = FragmentBookDetailBinding.inflate(inflater, container, false)

            initViews()

        } catch (e: Exception) {

            Log.e(
                TAG + " onCreateLine>>LineNumber: " +
                        Thread.currentThread().stackTrace[2].lineNumber, e.message.toString()
            )

        }

        return binding.root

    }


    open fun initViews() {

        act.tabNumber = 0
        act.setupDefaultSettings()
        act.setTextFonts(binding.root)

        act.binding.appBarHome.appBarNormal.title.text = act.getString(R.string.BookDetailLabel)

        observeViewModel()

        if (bookId > 0) {
            viewModel.getBookById(bookId)
        }

    }


    private fun setData(data: BookItem) {

        with(binding.layoutBookDetail) {

            tvTitle.text = data.title

            tvAuthor.text =
                act.getString(R.string.AuthorLabel).replace("aaa", data.author ?: " ")

            tvPrice.text = act.getString(R.string.PriceLabel).replace(
                "aaa",
                java.lang.StringBuilder().append(data.price)
                    .append(" ")
                    .append(data.currencyCode)
                    .toString()
            )

            tvIsbn.text = act.getString(R.string.IsbnLabel).replace("aaa", data.isbn ?: " ")

        }

        binding.tvDescription.text = data.description ?: " "

    }


    private fun observeViewModel() {

        viewModel.bookData.observe(viewLifecycleOwner) { data ->

            if (data != null)

                setData(data)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->

            if (isLoading != null) {
                act.binding.appBarHome.mloading.visibility =
                    if (isLoading) View.VISIBLE else View.GONE
                if (isLoading) {
                    DisableLayout(act.binding.root as ViewGroup)
                    DisableLayout(binding.root as ViewGroup)
                } else {
                    EnableLayout(act.binding.root as ViewGroup)
                    EnableLayout(binding.root as ViewGroup)
                }
            }
        }

        viewModel.isError.observe(viewLifecycleOwner) { isError ->
            if (isError) {
                Snackbar.make(binding.root, viewModel.errorText, Snackbar.LENGTH_LONG).show()
            }
        }
    }

}
package com.tapadootest.app.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tapadootest.app.R
import com.tapadootest.app.adapters.BooksAdapter
import com.tapadootest.app.classes.*
import com.tapadootest.app.databinding.FragmentBookListBinding
import com.tapadootest.app.models.books.BookItem
import com.tapadootest.app.view.activity.MainActivity
import com.tapadootest.app.viewmodel.BooksViewModel

class BookListFragment(private val act: MainActivity) : Fragment() {

    private val TAG = BookListFragment::class.java.simpleName

    lateinit var mSessionManager: SessionManager

    lateinit var languageSessionManager: LanguageSessionManager

    lateinit var binding: FragmentBookListBinding

    lateinit var viewModel: BooksViewModel

    lateinit var mAdapterBooks: BooksAdapter

    lateinit var mlayoutManager: LinearLayoutManager


    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println("Entered HomeFragment !!!====<>>>>>")

        try {

            mSessionManager = SessionManager(act)

            languageSessionManager = LanguageSessionManager(act)

            viewModel = ViewModelProvider(this)[BooksViewModel::class.java]

            viewModel.getBooks()

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

            binding = FragmentBookListBinding.inflate(inflater, container, false)

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

        mlayoutManager = LinearLayoutManager(act)
        mlayoutManager.orientation = RecyclerView.VERTICAL
        binding.rvBooksRecycler.layoutManager = mlayoutManager
        binding.rvBooksRecycler.itemAnimator = DefaultItemAnimator()

        act.binding.appBarHome.appBarNormal.title.text = act.getString(R.string.BookListLabel)

        observeViewModel()
        setData()
    }


    private fun setData() {

        mAdapterBooks = BooksAdapter(act, ArrayList(), object : BooksAdapter.OnItemClickListener {
            override fun onItemClick(list: ArrayList<BookItem>, position: Int) {

                var bundle = Bundle()
                bundle.putInt("bookId", list[position].id)

                var fragment = BookDetailFragment(act)
                fragment.arguments = bundle

                Navigator.loadFragment(act, fragment, R.id.content_home, true, "bookList")

            }
        })

        binding.rvBooksRecycler.adapter = mAdapterBooks

        binding.swipe.setOnRefreshListener { viewModel.getBooks()
            Handler(Looper.myLooper()!!).postDelayed(Runnable {
                binding.swipe.isRefreshing = false
            }, 2000)}

    }


    private fun observeViewModel() {

        viewModel.booksArrayList.observe(viewLifecycleOwner) { list ->

            Log.d("observeViewModel", "updateList -> " + list?.size)

            binding.rvBooksRecycler.visibility =
                if (list != null && list.size > 0) View.VISIBLE else View.GONE

//            viewModel.setPaging(binding.rvReportsRecycler, mlayoutManagerReports, list ?: ArrayList())

            mAdapterBooks.updateList(list ?: ArrayList())

            showListEmptyMessage(
                act.binding.appBarHome.tvNoData,
                act.getString(R.string.NoDataFoundLabel),
                act,
                viewModel.booksArrayList.value.isNullOrEmpty()
            )

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
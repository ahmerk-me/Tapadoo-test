package com.tapadootest.app.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tapadootest.app.R
import com.tapadootest.app.databinding.RowBookItemBinding
import com.tapadootest.app.models.books.BookItem
import com.tapadootest.app.view.activity.MainActivity

class BooksAdapter(
    val act: MainActivity,
    private val itemsData: ArrayList<BookItem>,
    private val listener: OnItemClickListener?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            RowBookItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemView)
    }

    inner class MyViewHolder(b: RowBookItemBinding) : RecyclerView.ViewHolder(b.root) {

        var binding: RowBookItemBinding = b

        fun bind(listener: OnItemClickListener, position: Int) {

            act.setTextFonts(binding.root)

            binding.linearParent.setOnClickListener {

                listener.onItemClick(itemsData, position)
            }

        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        (holder as MyViewHolder).bind(listener!!, holder.adapterPosition)

        itemsData[holder.adapterPosition].let {

            with(holder.binding.layoutBookDetail) {

                with(it) {

                    tvTitle.text = title

                    tvAuthor.text =
                        act.getString(R.string.AuthorLabel).replace("aaa", author ?: " ")

                    tvPrice.text = act.getString(R.string.PriceLabel).replace(
                        "aaa",
                        java.lang.StringBuilder().append(price)
                            .append(" ")
                            .append(currencyCode)
                            .toString()
                    )

                    tvIsbn.text = act.getString(R.string.IsbnLabel).replace("aaa", isbn ?: " ")
                }

            }

        }

    }


    override fun getItemCount(): Int {

        return itemsData.size

    }


    fun updateList(newList: ArrayList<BookItem>) {

        var list = newList.clone() as ArrayList<BookItem>

        with(itemsData) {

            clear()

            addAll(list)
        }

        notifyDataSetChanged()

    }


    interface OnItemClickListener {

        fun onItemClick(list: ArrayList<BookItem>, position: Int)

    }
}
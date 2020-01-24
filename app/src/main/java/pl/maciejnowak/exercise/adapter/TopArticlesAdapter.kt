package pl.maciejnowak.exercise.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.maciejnowak.exercise.R
import pl.maciejnowak.exercise.database.model.TopArticle
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TopArticlesAdapter(private val context: Context, private val items: MutableList<TopArticle> = mutableListOf())
    : RecyclerView.Adapter<TopArticlesAdapter.ViewHolder>() {

    private val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val user: TextView = view.findViewById(R.id.user)
        val date: TextView = view.findViewById(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_top_article, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.apply {
            title.text = item.title
            user.text = item.user
            date.text = formatter.format(item.timestamp * TimeUnit.SECONDS.toMillis(1))

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun update(items: List<TopArticle>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}
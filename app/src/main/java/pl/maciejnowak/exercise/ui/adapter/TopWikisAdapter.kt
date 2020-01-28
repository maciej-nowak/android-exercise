package pl.maciejnowak.exercise.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import pl.maciejnowak.exercise.R
import pl.maciejnowak.exercise.database.model.TopWiki

class TopWikisAdapter(private val context: Context, private val items: MutableList<TopWiki> = mutableListOf())
    : RecyclerView.Adapter<TopWikisAdapter.ViewHolder>() {

    private val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.image)
        val title: TextView = view.findViewById(R.id.title)
        val counter: TextView = view.findViewById(R.id.counter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_top_wiki, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.apply {
            title.text = item.title
            counter.text = context.getString(R.string.articles_count, item.articlesCounter)
            Glide.with(context).load(item.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .placeholder(R.drawable.ic_image_placeholder)
                .centerCrop()
                .into(image)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun update(items: List<TopWiki>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}
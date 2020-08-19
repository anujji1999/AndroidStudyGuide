package com.adammcneilly.androidstudyguide.articlelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.adammcneilly.androidstudyguide.R
import com.adammcneilly.androidstudyguide.databinding.ListItemArticleBinding
import com.adammcneilly.androidstudyguide.models.Article

/**
 * This adapter class is responsible for taking a list of [articles] and binding them into a
 * [RecyclerView].
 *
 * @property[clickListener] This will handle all the callbacks for click events on an article item.
 */
class ArticleAdapter(
    private val clickListener: ArticleClickListener
) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    var articles: List<Article> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemArticleBinding.inflate(inflater, parent, false)
        return ArticleViewHolder(binding, clickListener)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.bindArticle(article)
    }

    class ArticleViewHolder(
        private val binding: ListItemArticleBinding,
        private val clickListener: ArticleClickListener
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private var article: Article? = null

        init {
            binding.root.setOnClickListener(this)
            binding.bookmarkButton.setOnClickListener(this)
        }

        fun bindArticle(article: Article) {
            this.article = article
            binding.articleTitle.text = article.htmlTitle.getSpanned()
            binding.articleAuthor.text = itemView.context.getString(
                R.string.by_author,
                article.authorName
            )

            val bookmarkIcon = if (article.bookmarked) {
                R.drawable.ic_bookmark_selected
            } else {
                R.drawable.ic_bookmark_unselected
            }
            binding.bookmarkButton.setImageResource(bookmarkIcon)
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.bookmark_button -> {
                    val isBookmarked = article?.bookmarked == true
                    article = article?.copy(bookmarked = !isBookmarked)

                    val bookmarkIcon = if (article?.bookmarked == true) {
                        R.drawable.ic_bookmark_selected
                    } else {
                        R.drawable.ic_bookmark_unselected
                    }
                    binding.bookmarkButton.setImageResource(bookmarkIcon)

                    ViewCompat.postOnAnimationDelayed(binding.bookmarkButton, Runnable {
                        article?.let(clickListener::onBookmarkClicked)
                    }, 50)
                }
                else -> article?.let(clickListener::onArticleClicked)
            }
        }
    }
}

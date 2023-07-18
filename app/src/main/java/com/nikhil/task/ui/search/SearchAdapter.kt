package com.nikhil.task.ui.search


import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nikhil.task.R
import com.nikhil.task.databinding.SearchItemBinding
import com.nikhil.task.network.SearchResponseItem


class SearchAdapter() :
    ListAdapter<SearchResponseItem, SearchAdapter.SearchViewHolder>(ComparatorDiffUtil()) {

    private val TAG = "SearchAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val searchResponseItem = getItem(position)
        searchResponseItem?.let {
            holder.bind(it)
        }

    }

    inner class SearchViewHolder(private val binding: SearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(searchResult: SearchResponseItem) {
            Log.d(TAG, "bind: $searchResult")
            binding.itemDescriptionImg.setOnClickListener {
                collapseDescription()
            }

            setCardData(searchResult = searchResult)
            val resumeUriString = searchResult.details[0].aws_path
            binding.root.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(resumeUriString))
                binding.root.context.startActivity(intent)
            }

        }



        private fun collapseDescription() {
            if (binding.candidateDesc.visibility == View.GONE) {
                binding.candidateDesc.visibility = View.VISIBLE
                binding.itemDescriptionImg.setImageResource(R.drawable.ic_expand_less_black_24dp)
            } else  {
                binding.candidateDesc.visibility = View.GONE
                binding.itemDescriptionImg.setImageResource((R.drawable.ic_expand_more_black_24dp))
            }
            val animation = ObjectAnimator.ofInt(binding.candidateDesc, "maxLines", binding.candidateDesc.maxLines)
            animation.setDuration(500).start()

        }
        private fun setCardData(searchResult: SearchResponseItem) {
            binding.candidateName.text = searchResult.details[0].candidate_name.orEmpty()
            binding.candidateEmail.text = String.format(binding.root.resources.getString(R.string.emailwithplaceholder), searchResult.email_id)

            binding.candidatePhone.text =String.format(binding.root.resources.getString(R.string.phonewithplaceholder), searchResult.mobile_number)
            binding.candidateDesc.text = String.format(binding.root.resources.getString(R.string.descwithplaceholder), searchResult.description)
            binding.candidateScore.text = String.format(binding.root.resources.getString(R.string.scorewithplaceholder), searchResult.score)
        }

    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<SearchResponseItem>() {
        override fun areItemsTheSame(oldItem: SearchResponseItem, newItem: SearchResponseItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SearchResponseItem, newItem: SearchResponseItem): Boolean {
            return oldItem == newItem
        }
    }
}
package im.vector.app.eachchat.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by chengww on 2020/10/27
 * @author chengww
 */
open class DataBindingAdapter<V : ViewDataBinding, T>(var layoutId: Int,
                                                      var data: List<T>,
                                                      private val withListChangeListener: Boolean = true) : RecyclerView.Adapter<BaseViewHolder>() {

    var itemClickListener: ItemClickListener<T>? = null
    var itemChildClickListener: ItemChildClickListener<T>? = null
    var itemLongClickListener: ItemClickListener<T>? = null

    private val callback = OnListChangeCallback<T>()

    init {
        if (withListChangeListener && data is ObservableList<T>) {
            (data as ObservableList<T>).addOnListChangedCallback(callback)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): BaseViewHolder {
        val binding: V = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context), layoutId, viewGroup, false)
        return BaseViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding: V? = DataBindingUtil.getBinding(holder.itemView)
        onDataBind(binding, position)
        binding?.executePendingBindings()
        itemClickListener?.let { listener ->
            holder.itemView.setOnClickListener {
                runCatching {
                    listener.invoke(data[holder.absoluteAdapterPosition], holder.absoluteAdapterPosition)
                }
            }
        } ?: holder.itemView.setOnClickListener(null)

        itemLongClickListener?.let { listener ->
            holder.itemView.setOnLongClickListener {
                runCatching {
                    listener.invoke(data[holder.absoluteAdapterPosition], holder.absoluteAdapterPosition)
                }
                true
            }
        } ?: holder.itemView.setOnLongClickListener(null)

        itemChildClickListener?.let { listener ->
            holder.itemView.findViewById<View>(listener.childId)?.setOnClickListener {
                runCatching {
                    listener.onClick(data[holder.absoluteAdapterPosition], holder.absoluteAdapterPosition)
                }
            }
        }
    }

    /**
     * offers the data binding variables,
     * only a variable named `item` offered by default.
     *
     * @param binding instance of [ViewDataBinding]
     * @param position position of the [data]
     */
    open fun onDataBind(binding: V?, position: Int) {
        binding?.setVariable(BR.item, data[position])
    }

    internal inner class OnListChangeCallback<T> : ObservableList.OnListChangedCallback<ObservableList<T>>() {
        @SuppressLint("NotifyDataSetChanged")
        override fun onChanged(sender: ObservableList<T>) {
            notifyDataSetChanged()
        }

        override fun onItemRangeChanged(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
            notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeInserted(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
            notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeMoved(sender: ObservableList<T>, fromPosition: Int, toPosition: Int, itemCount: Int) {
            notifyItemMoved(fromPosition, toPosition)
        }

        override fun onItemRangeRemoved(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
            notifyItemRangeRemoved(positionStart, itemCount)
        }
    }

    override fun getItemCount() = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun setNewData(data: List<T>) {
        if (withListChangeListener) (this.data as? ObservableList<T>)?.removeOnListChangedCallback(callback)
        this.data = data
        if (withListChangeListener) (this.data as? ObservableList<T>)?.addOnListChangedCallback(callback)
        notifyDataSetChanged()
    }
}

typealias ItemClickListener<T> = (t: T, pos: Int) -> Unit

class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

abstract class ItemChildClickListener<T>(val childId: Int) {
    abstract fun onClick(t: T, position: Int )
}


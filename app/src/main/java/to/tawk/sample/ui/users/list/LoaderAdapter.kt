package to.tawk.sample.ui.users.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import to.tawk.sample.R

class LoaderAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return LoadingVh(LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemCount() = 1

    inner class LoadingVh(itemView:View):RecyclerView.ViewHolder(itemView){

    }

}
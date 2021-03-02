package to.tawk.sample.ui.users.list

import android.content.Context
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import to.tawk.sample.data.User
import to.tawk.sample.databinding.ItemUserBinding

class UsersListAdapter(private val context: Context): ListAdapter<User, UsersListAdapter.UserVH>(
    UsersDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserVH {
       return UserVH(ItemUserBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: UserVH, position: Int) {
       holder.bindData(getItem(holder.adapterPosition))
    }


    inner class UserVH(private val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root){

        /**
         * Color matrix that flips the components (`-1.0f * c + 255 = 255 - c`)
         * and keeps the alpha intact.
         */
        private val NEGATIVE = floatArrayOf(
            -1.0f,
            0f,
            0f,
            0f,
            255f,
            0f,
            -1.0f,
            0f,
            0f,
            255f,
            0f,
            0f,
            -1.0f,
            0f,
            255f,
            0f,
            0f,
            0f,
            1.0f,
            0f
        )

        fun bindData(user: User){
            with(user){
                binding.model = this
            }

            if(adapterPosition%4==0){
                binding.imageView.colorFilter = ColorMatrixColorFilter(NEGATIVE)
            }

            binding.executePendingBindings()
        }
    }


}
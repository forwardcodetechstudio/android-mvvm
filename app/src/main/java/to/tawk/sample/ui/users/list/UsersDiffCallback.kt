package to.tawk.sample.ui.users.list

import androidx.recyclerview.widget.DiffUtil
import to.tawk.sample.data.User

class UsersDiffCallback: DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id==newItem.id
    }

}
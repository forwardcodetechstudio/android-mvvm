package to.tawk.sample

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class BindingAdapter {


    companion object {
        @JvmStatic
        @BindingAdapter("bind:image")
        fun bindAvatar(imageView: ImageView, url: String?) {
            Glide.with(imageView)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imageView)
        }
    }
}
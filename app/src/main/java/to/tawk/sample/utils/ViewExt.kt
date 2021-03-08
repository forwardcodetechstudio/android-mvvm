package to.tawk.sample.utils

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar


class ViewExt {

    companion object{

        fun View.hideKeyboard() {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
        }

        fun View.showSnackBar(message: String?){
            Snackbar.make(this, message ?: "Something went wrong!", Snackbar.LENGTH_SHORT).show()
        }

        fun Context.isNetworkAvailable():Boolean{
            val connectivityManager=getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo=connectivityManager.activeNetworkInfo
            return  networkInfo!=null && networkInfo.isConnected
        }
    }
}
package to.tawk.sample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import dagger.hilt.android.AndroidEntryPoint
import to.tawk.sample.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private  var noInternetReceiver : BroadcastReceiver?=null
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        noInternetReceiver = object : BroadcastReceiver() {
            override fun onReceive(contxt: Context?, intent: Intent?) {
                if (intent?.action=="no-internet") {

                    if(intent.getBooleanExtra("available",false)){
                        binding.noInternetNotice.visibility= View.GONE
                    }
                    else{
                        binding.noInternetNotice.visibility= View.VISIBLE
                    }

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(noInternetReceiver!=null)
        LocalBroadcastManager.getInstance(this).registerReceiver(noInternetReceiver!!, IntentFilter("no-internet"))

    }

    override fun onPause() {
        if(noInternetReceiver!=null)
        LocalBroadcastManager.getInstance(this).unregisterReceiver(noInternetReceiver!!)
        super.onPause()

    }
}
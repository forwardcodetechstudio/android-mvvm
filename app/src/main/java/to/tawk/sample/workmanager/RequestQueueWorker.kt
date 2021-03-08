package to.tawk.sample.workmanager

import android.content.Context
import android.content.Intent
import androidx.hilt.work.HiltWorker
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class RequestQueueWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters
//    @Assisted private val repository: UsersRepository
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {


        LocalBroadcastManager.getInstance(context)
            .sendBroadcast(Intent("no-internet").putExtra("available", true))

        var result: Result = Result.failure()

        val pageSize = workerParams.inputData.getInt("pageSize", 0)
        val since = workerParams.inputData.getInt("since", 0)

        // make api calls
//        repository.getUsers(since,pageSize).collect()
//        {
//            result = when(it.status){
//                Status.SUCCESS->{
//                    Result.success()
//                }
//                Status.ERROR->{
//                    Result.retry()
//                }
//                else -> {
//                    Result.failure()
//                }
//            }
//        }

        return result

    }


}
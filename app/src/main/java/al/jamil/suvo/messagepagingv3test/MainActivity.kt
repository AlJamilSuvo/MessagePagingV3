package al.jamil.suvo.messagepagingv3test

import al.jamil.suvo.messagepagingv3test.databinding.ActivityMainBinding
import al.jamil.suvo.messagepagingv3test.noPaging.NonPagingDataLoadActivity
import al.jamil.suvo.messagepagingv3test.paging.PagingDataLoadActivity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.btNonPaging.setOnClickListener {
            startActivity(Intent(this, NonPagingDataLoadActivity::class.java))
        }

        binding.btPaging.setOnClickListener {
            startActivity(Intent(this, PagingDataLoadActivity::class.java))
        }




    }

}
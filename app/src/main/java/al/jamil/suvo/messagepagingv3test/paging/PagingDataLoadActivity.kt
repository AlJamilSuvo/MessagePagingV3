package al.jamil.suvo.messagepagingv3test.paging

import al.jamil.suvo.messagepagingv3test.databinding.ActionDialogLayoutBinding
import al.jamil.suvo.messagepagingv3test.databinding.DataLoadActivityBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PagingDataLoadActivity : AppCompatActivity() {
    private lateinit var binding: DataLoadActivityBinding
    private lateinit var viewModel: PagingViewModel
    private var msgCnt = 0
    private val adapter = MessagePagingAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        var startTime = System.currentTimeMillis()
        super.onCreate(savedInstanceState)
        binding = DataLoadActivityBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[PagingViewModel::class.java]
        binding.fab.setOnClickListener {
            setActionCenter()
        }
        binding.rvItem.adapter = adapter
        binding.tvInfo.text = "Loading..."

        lifecycleScope.launch {
            viewModel.getMessagePagingFlow().collectLatest {
                adapter.submitData(it)

            }
        }

        lifecycleScope.launch {
            viewModel.getInsertRowId().collectLatest {
                adapter.refresh()
            }
        }

        lifecycleScope.launch {
            viewModel.getShowProgress().collectLatest {
                binding.cvProgress.visibility =
                    if (it) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launch {
            viewModel.getProgressValue().collectLatest {
                binding.progressBar.progress = (it * 100).toInt()
            }
        }

        lifecycleScope.launch {
            viewModel.getMessageCnt().collectLatest {
                msgCnt = it
                binding.tvMsgCnt.text = "Message Cnt $it"
            }
        }

        binding.rvItem.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = binding.rvItem.layoutManager as LinearLayoutManager
                val pos = layoutManager.findFirstVisibleItemPosition()
                if (msgCnt > 0) {
                    val progress = (pos.toFloat() / msgCnt.toFloat()) * 100
                    binding.scrollBar.progress = progress.toInt()
                    binding.tvScrollPos.text = "$pos/$msgCnt"
                }
            }
        })

        binding.scrollBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val pos = progress * msgCnt / 100
                    binding.rvItem.scrollToPosition(pos)
                }

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        adapter.addLoadStateListener {
            when (it.source.refresh) {
                is LoadState.NotLoading -> {
                    if (adapter.itemCount == 0) binding.tvInfo.text = "No Message"
                    else binding.tvInfo.text = ""

                    if (startTime > 0) {
                        val time = System.currentTimeMillis() - startTime
                        binding.tvInitialLoadingTime.text = "Initial Loading time $time ms"
                        startTime = -1
                    }
                }


                LoadState.Loading -> {
                    binding.tvInfo.text = "Loading..."
                }

                else -> {

                }
            }
        }
    }

    private fun setActionCenter() {
        val builder = AlertDialog.Builder(this)
        val dialogBinding = ActionDialogLayoutBinding.inflate(LayoutInflater.from(this))
        builder.setView(dialogBinding.root)
        val dialog = builder.create()
        dialogBinding.addMsg.setOnClickListener {
            viewModel.addMessage(10000)
            dialog.dismiss()
        }
        dialogBinding.deleteAll.setOnClickListener {
            viewModel.deleteAll()
            dialog.dismiss()
        }
        dialog.show()
    }
}
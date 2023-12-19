package al.jamil.suvo.messagepagingv3test.paging

import al.jamil.suvo.messagepagingv3test.db.Message
import al.jamil.suvo.messagepagingv3test.db.MessageDb
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState


class MessagePagingDataSource : PagingSource<Int, Message>() {


    override fun getRefreshKey(state: PagingState<Int, Message>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        return state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
            ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Message> {
        val messageDb = MessageDb.getInstance()
        val currentPageIndex = params.key ?: 0
        val offset = currentPageIndex * Config.PAGE_SIZE
        val messages =
            messageDb?.pagingDao()?.getMessagePaging(offset, Config.PAGE_SIZE) ?: emptyList()
        val messageCnt = messageDb?.commonDao()?.getMessageCnt() ?: 0

        val prevKey = if (currentPageIndex == 0) null else currentPageIndex - 1
        val nextKey = if (messages.size == Config.PAGE_SIZE) currentPageIndex + 1 else null

        val itemBefore = currentPageIndex * Config.PAGE_SIZE
        val itemAfter = messageCnt - (currentPageIndex * Config.PAGE_SIZE) - messages.size

        Log.d("MsgPaging3", "loaded page # $currentPageIndex")

        return LoadResult.Page(
            messages,
            prevKey, nextKey,
            itemBefore,
            itemAfter
        )
    }
}
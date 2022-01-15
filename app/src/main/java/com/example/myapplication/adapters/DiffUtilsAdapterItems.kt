import androidx.recyclerview.widget.DiffUtil
import com.example.myapplication.adapters.AdapterItem


class DiffUtilsAdapterItems (private val newData: List<AdapterItem>?, private val oldData: List<AdapterItem>?):
    DiffUtil.Callback(){

    override fun getOldListSize(): Int {
        return oldData?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return newData?.size ?: 0
    }

    override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
        return newData!![newPos].key.equals(oldData!![oldPos].key)
    }

    override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
        return newData!![newPos] == oldData!![oldPos]
    }


}
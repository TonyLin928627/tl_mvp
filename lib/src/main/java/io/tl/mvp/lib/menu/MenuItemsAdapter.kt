package io.tl.mvp.lib.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
//import com.robohorse.pagerbullet.PagerBullet
//import com.smartspaceplc.smartspace.BuildConfig
//import com.smartspaceplc.smartspace.R
//import com.smartspaceplc.smartspace.appV2.dataBridge.dynamicConf.AppSection
//import com.smartspaceplc.smartspace.appV2.dataBridge.mapsData.Node
import io.reactivex.subjects.PublishSubject
import io.tl.mvp.lib.R

class MenuItemsAdapter: RecyclerView.Adapter<MenuItemsAdapter.MenuItemsViewHolder>() {
    val mMenuItemList = mutableListOf<MenuItem>()

    val clickSubject = PublishSubject.create<MenuItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout__menu_item, parent, false)
        return MenuItemsViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MenuItemsViewHolder, position: Int) {
        holder.homeDrawerMenuItem = mMenuItemList[position]
    }

    override fun getItemCount(): Int {
        return mMenuItemList.size
    }

    inner class MenuItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemNameTv: TextView = (itemView as TextView)

        init {
            itemView.setOnClickListener{
                homeDrawerMenuItem?.let {theHomeDrawerMenuItem ->
                    clickSubject.onNext(theHomeDrawerMenuItem)
                }
            }
        }

        var homeDrawerMenuItem: MenuItem? = null
            set(newValue){
                field = newValue

                newValue?.let{
                    itemNameTv.setText(it.nameTxtResId)
                } ?: kotlin.run {
                    itemNameTv.text = ""
                }

            }
    }
}
//endregion
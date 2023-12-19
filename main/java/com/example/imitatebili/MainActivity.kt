package com.example.imitatebili

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.StatefulAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.imitatebili.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var bloggerList = BloggerSender().onCreateList()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 创建一个布局管理器，用于确定RecyclerView中每个项的位置和大小
        val layoutManager = LinearLayoutManager(this)   // 创建一个布局管理器，将目前这个main布局传给它
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.recyclerView.layoutManager = layoutManager    // 再将布局管理器赋给recyclerView，来管理recyclerView
        // 创建一个适配器实例
        val adapter = BloggerAdapter(bloggerList, binding.viewPager2, this)
        binding.recyclerView.adapter = adapter    // 再把这个实例赋给recyclerView，recyclerView的适配器即BloggerAdapter

        // 创建viewPager2的适配器（adapter 适配器）
//        binding.viewPager2.adapter = object : FragmentStateAdapter(this) {
//            val count : Int = adapter.itemCount
//            override fun getItemCount() = count   // 告诉viewpager里面有几个页面
//            override fun createFragment(position: Int) =     // 每一个位置要创建哪一个页面给它
//                when(position) {
//                    0 -> BloggerFragment1()              // 位置是0的话就创建一个BloggerFragment1的实例给它
//                    1 -> BloggerFragment2()
//                    2 -> BloggerFragment3()
//                    else -> BloggerFragment4()
//                }
//        }
        val viewPager2 = ViewPager2Adapter(binding.viewPager2, bloggerList, this)


        // 将recyclerView与viewPager2关联起来
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                // 在这里更新 RecyclerView 的数据，可以根据 position 来获取当前页码和位置信息
                binding.recyclerView.scrollToPosition(position)
            }
        })

//        val data2 = intent.getIntExtra("delete", 1)
//        Log.d("MainActivity", data2.toString())
//        when (data2) {
//            0 -> bloggerList.remove(Blogger("三太子敖丙", R.drawable.aobing_pic))
//            1 -> bloggerList.removeAt(1)   // 目前有两个问题：1.不知道如何从detail传到main。2.不知道如何删除recyclerview和viewpager2的元素，而且要能够做到连续删除
//            2 -> bloggerList.remove(Blogger("屑老板不破产", R.drawable.xielaoban_pic))
//            3 -> bloggerList.remove(Blogger("遇见狂神说", R.drawable.kuangshen_pic))
//        }
//        Log.d("MainActivity", data2.toString())
        adapter.updateData(bloggerList)
//        adapter.notifyDataSetChanged()
        binding.recyclerView.adapter = adapter
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("MainActivity", "jjjjjj")

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Log.d("data", data.toString())
            val deleteIndex = data?.getIntExtra("delete", 0) ?: return
            if (deleteIndex in 0..3) {
                bloggerList.removeAt(deleteIndex)
            } else {
                // 处理其他情况，例如显示一个错误消息或者什么都不做
            }
        }
    }
}


// 创建一个Blogger类， 用来创建博主实例
class Blogger (val name: String, val imageId: Int)


// 创建一个用来初始化博主列表的类
class BloggerSender {
    fun onCreateList(): MutableList<Blogger> {
        val aobing = Blogger("三太子敖丙", R.drawable.aobing_pic)
        val yupi = Blogger("程序员鱼皮", R.drawable.yupi_pic)
        val xielaoban = Blogger("屑老板不破产", R.drawable.xielaoban_pic)
        val kuangshen = Blogger("遇见狂神说", R.drawable.kuangshen_pic)
        return mutableListOf(aobing, yupi, xielaoban, kuangshen)
    }
}



// recyclerView的适配器，以下是它的标准写法
class BloggerAdapter(private var bloggerList: List<Blogger>, private var viewPager2: ViewPager2, private val activity: Activity) :     // 传入博主列表作为参数
    RecyclerView.Adapter<BloggerAdapter.ViewHolder>() {     // 继承于RecyclerView.Adapter<BloggerAdapter.ViewHolder>()这个类

    // 自定义一个内部类，用来缓存每个博主的信息
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {      // 传入一个View参数，其通常就是RecyclerView子项的最外层布局
        // val bloggerImage: ImageView = view.findViewById(R.id.bloggerImage)
        val bloggerName: TextView = view.findViewById(R.id.bloggerName)
        val bloggerImage: ImageView = view.findViewById(R.id.bloggerImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.blogger_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.bloggerImage.setOnClickListener {
            viewPager2.currentItem = viewHolder.bindingAdapterPosition
        }

        viewHolder.bloggerImage.setOnLongClickListener {
            val data = viewHolder.bindingAdapterPosition
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra("position", data)
            activity.startActivityForResult(intent, 1)
            false
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val blogger = bloggerList[position]
        holder.bloggerImage.setImageResource(blogger.imageId)
        holder.bloggerName.text = blogger.name

//        holder.bloggerImage.setOnClickListener {
//            viewPager2.currentItem = position
//        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<Blogger>) {
        this.bloggerList = newData // 更新内部数据集
        this.notifyDataSetChanged() // 通知数据已更改
    }

    override fun getItemCount() = bloggerList.size     // 让 recyclerView 的位置个数等于博主列表的元素个数
}

class ViewPager2Adapter(private var viewPager2: ViewPager2, private var bloggerList: List<Blogger>, private val activity: Activity) : RecyclerView.Adapter<ViewPager2Adapter.ViewHolder>(){

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text : TextView = view.findViewById(R.id.)
    }

    override fun getItemCount() = bloggerList.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewPager2Adapter.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewPager2Adapter.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}
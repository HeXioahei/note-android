package com.example.imitatebili

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.imitatebili.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding2 : ActivityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding2.root)
        val data = intent.getIntExtra("position", 0)

        val imageView = binding2.imageDetail
        val textView = binding2.nameDetail
        val button = binding2.unfollowButton
        when (data) {
            0 -> { imageView.setImageResource(R.drawable.aobing_pic)
                textView.setText(R.string.detial_aobing) }
            1 -> { imageView.setImageResource(R.drawable.yupi_pic)
                textView.setText(R.string.detial_yupi) }
            2 -> { imageView.setImageResource(R.drawable.xielaoban_pic)
                textView.setText(R.string.detial_xielaoban) }
            3 -> { imageView.setImageResource(R.drawable.kuangshen_pic)
                textView.setText(R.string.detial_kuangshen) }
        }

        button.setOnClickListener {
            Toast.makeText(this, "取关成功", Toast.LENGTH_SHORT).show()
            val intent = Intent()
            val data2 = data
            intent.putExtra("delete", data2)
            setResult(RESULT_OK, intent)
//            val intent = Intent(this, MainActivity::class.java)
//            intent.putExtra("delete", data)
//            startActivity(intent)
            finish()
        }
    }
}
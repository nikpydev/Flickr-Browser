package com.example.flickrbrowser

import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_photo_details.*

class PhotoDetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_details)

        activateToolbar(true)

//        val photo = intent.getSerializableExtra(PHOTO_TRANSFER) as Photo
        val photo = intent.extras?.getParcelable<Photo>(PHOTO_TRANSFER) as Photo
        photo_title.text = photo.title
        photo_tags.text = photo.tags
        photo_author.text = photo.author

        Picasso.get().load(photo.link).error(R.drawable.broken_image)
            .placeholder(R.drawable.placeholder).into(photo_image)
    }

}

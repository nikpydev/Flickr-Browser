package com.example.flickrbrowser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : BaseActivity(), GetRawData.OnDownloadComplete,
    GetFlickrJsonData.OnDataAvailable, RecyclerItemClickListener.OnRecyclerClickListener {

    private val TAG = "MainActivity"

    private val flickerRecyclerViewAdapter = FlickrRecyclerViewAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d(TAG, "onCreate(): called")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activateToolbar(false)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addOnItemTouchListener(RecyclerItemClickListener(this, recycler_view, this))
        recycler_view.adapter = flickerRecyclerViewAdapter

        val url = createUri(
            "https://www.flickr.com/services/feeds/photos_public.gne",
            "android,oreo",
            "en-us",
            true
        )
        val getRawData = GetRawData(this)
        getRawData.execute(url)

        Log.d(TAG, "onCreate(): ends")
    }

    override fun onItemClick(view: View, postion: Int) {
        Log.d(TAG, ".onItemClicked: starts")
        Toast.makeText(this, "Normal tap at position $postion", Toast.LENGTH_SHORT).show()
    }

    override fun onItemLongClick(view: View, posititon: Int) {
        Log.d(TAG, ".onItemLongClick: starts")
//        Toast.makeText(this, "Long tap at position $posititon", Toast.LENGTH_SHORT).show()
        val photo = flickerRecyclerViewAdapter.getPhoto(posititon)
        if (photo != null) {
            val intent = Intent(this, PhotoDetailsActivity::class.java)
            intent.putExtra(PHOTO_TRANSFER, photo)
            startActivity(intent)
        }
    }

    private fun createUri(
        baseURL: String,
        searchCriteria: String,
        lang: String,
        matchAll: Boolean
    ): String {
        Log.d(TAG, ".createUri starts")

        return Uri.parse(baseURL)
            .buildUpon().appendQueryParameter("tags", searchCriteria)
            .appendQueryParameter("tagmode", if (matchAll) "ALL" else "ANY")
            .appendQueryParameter("lang", lang).appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .build().toString()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.

        Log.d(TAG, "onCreateOptionsMenu(): called")

        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        Log.d(TAG, "onOptionsItemSelected(): called")

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

//    companion object {
//        private const val TAG = "MainActivity"
//    }

    override fun onDownloadComplete(data: String, status: DownloadStatus) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete() called")

            val getFlickrJsonData = GetFlickrJsonData(this)
            getFlickrJsonData.execute(data)
        } else {
            // Download failed
            Log.d(TAG, "onDownloadComplete(): failed with status $status. Error message is: $data")
        }
    }

    override fun onDataAvailable(data: List<Photo>) {
        Log.d(TAG, ".onDataAvailable called")
        flickerRecyclerViewAdapter.loadNewData(data)
        Log.d(TAG, ".onDataAvailable ends")
    }

    override fun onError(exception: Exception) {
        Log.e(TAG, "onError called with ${exception.message}")
    }
}

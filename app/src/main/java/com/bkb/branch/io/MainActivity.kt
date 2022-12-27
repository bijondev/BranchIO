package com.bkb.branch.io

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.Branch.BranchLinkCreateListener
import io.branch.referral.BranchError
import io.branch.referral.SharingHelper
import io.branch.referral.util.ContentMetadata
import io.branch.referral.util.LinkProperties
import io.branch.referral.util.ShareSheetStyle
import org.json.JSONObject
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buo = BranchUniversalObject()
                .setCanonicalIdentifier("content/12345")
                .setTitle("My Content Title")
                .setContentDescription("My Content Description")
                .setContentImageUrl("https://lorempixel.com/400/400")
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .setContentMetadata(ContentMetadata().addCustomMetadata("key1", "value1"))

        Log.i("buo", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + buo)


        /*branchUniversalObject.generateShortUrl(this, linkProperties, BranchLinkCreateListener { url, error ->
            if (error == null) {
                Log.i("MyApp", "got my Branch link to share: $url")
            }
        })*/

        val shareSheetStyle = ShareSheetStyle(
            this@MainActivity,
            "Your Awesome Deal",
            "You will never believe what happened next!"
        )
                .setCopyUrlStyle(
                    resources.getDrawable(android.R.drawable.ic_menu_send),
                    "Copy",
                    "Added to clipboard"
                )
                .setMoreOptionStyle(
                    resources.getDrawable(android.R.drawable.ic_menu_search),
                    "Show more"
                )
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL)
                .setAsFullWidthStyle(true)
                .setSharingTitle("Share With")


        val lp = LinkProperties()
                .setChannel("facebook")
                .setFeature("sharing")
                .setCampaign("content 123 launch")
                .setStage("new user")
                .addControlParameter("desktop_url", "http://example.com/home")
                .addControlParameter("custom", "data")
                .addControlParameter("custom_random", Long.toString())
        Log.i("lp", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + lp)
        /*buo.generateShortUrl(this, lp, Branch.BranchLinkCreateListener {

            url?, error? ->
            if (error == null) {
                Log.i("BRANCH SDK", "got my Branch link to share: " + url)
            }
        })*/

        buo.generateShortUrl(this, lp, BranchLinkCreateListener { url, error ->
            Log.i("error", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + error)
            Log.i("url", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + url)
            if (error == null) {
                Log.i("MyApp", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> $url")
            }
        }
        )
    }


    override fun onStart() {
        super.onStart()
        // Branch init
        Log.d("branchListener", ">>>>>>>>>>>>>>>>>>>>>>>>>>>" + this.intent?.data)
        // 0Branch.sessionBuilder(this).withCallback(branchListener).withData(if (intent != null) intent.data else null).init()
        // Branch.sessionBuilder(this).withCallback(branchListener).withData(this.intent?.data).init()
        // Branch.sessionBuilder(this).withCallback(branchListener).withData("jhgfdhgdfhgfg".toUri()).init()

        Branch.sessionBuilder(this).withCallback(object : Branch.BranchReferralInitListener {
            override fun onInitFinished(referringParams: JSONObject?, error: BranchError?) {
                if (error == null) {
                    Log.i("BRANCH SDK", referringParams.toString())
                } else {
                    Log.e("BRANCH SDK",error.message)
                }
            }
        }).withData(this.intent.data).init()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        this.intent = intent
        // Branch reinit (in case Activity is already in foreground when Branch link is clicked)
        //Branch.sessionBuilder(this).withCallback(branchListener).withData(this.intent?.data).init()
        Branch.sessionBuilder(this).withCallback(branchListener).reInit()
    }

    object branchListener : Branch.BranchReferralInitListener {
        override fun onInitFinished(referringParams: JSONObject?, error: BranchError?) {
            if (error == null) {
                Log.i("BRANCH SDK", referringParams.toString())
                // Retrieve deeplink keys from 'referringParams' and evaluate the values to determine where to route the user
                // Check '+clicked_branch_link' before deciding whether to use your Branch routing logic
            } else {
                Log.e("BRANCH SDK", error.message)
            }
        }
    }


}
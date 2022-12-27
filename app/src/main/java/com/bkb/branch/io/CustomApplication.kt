package com.bkb.branch.io

import android.app.Application
import android.content.Intent
import android.util.Log
import org.json.JSONObject

import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.BranchError
import io.branch.referral.util.LinkProperties


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

class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Branch logging for debugging
        Branch.enableDebugMode()

        // Branch object initialization
        Branch.getAutoInstance(this)
    }
}
# BranchIO
Android branchIO sample


Branch is a mobile linking platform that allows developers to deep link into their apps from various sources, such as emails, social media, or advertising campaigns. Here is an example of how to implement Branch in an Android app:

    Add the Branch library to your app's build.gradle file:
```
implementation 'io.branch.sdk.android:library:3.1.1'
```
    Add the Branch key to your app's AndroidManifest.xml file:
```
<meta-data
    android:name="io.branch.sdk.BranchKey"
    android:value="YOUR_BRANCH_KEY" />
```
    Initialize Branch in your app's main Activity:
```
@Override
protected void onStart() {
    super.onStart();
    Branch.getInstance().initSession(new Branch.BranchReferralInitListener() {
        @Override
        public void onInitFinished(JSONObject referringParams, BranchError error) {
            if (error == null) {
                // Retrieve deeplink keys from 'referringParams' and evaluate the values to determine where to route the user
                // Check '+clicked_branch_link' before deciding to route
            } else {
                Log.e("MyApp", error.getMessage());
            }
        }
    }, this.getIntent().getData(), this);
}

@Override
public void onNewIntent(Intent intent) {
    this.setIntent(intent);
}
```
    To create a deep link, use the Branch Universal Object to define the link's properties and use the LinkProperties object to set any additional parameters:
```
BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
        .setCanonicalIdentifier("item/12345")
        .setTitle("My Item")
        .setContentDescription("This is my item")
        .setContentImageUrl("https://example.com/my_item.png")
        .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
        .addContentMetadata("key1", "value1")
        .addContentMetadata("key2", "value2");

LinkProperties linkProperties = new LinkProperties()
        .setChannel("facebook")
        .setFeature("sharing")
        .addControlParameter("$desktop_url", "http://example.com/home")
        .addControlParameter("custom_parameter_1", "custom_value_1")
        .addControlParameter("custom_parameter_2", "custom_value_2");

branchUniversalObject.generateShortUrl(this, linkProperties, new Branch.BranchLinkCreateListener() {
    @Override
    public void onLinkCreate(String url, BranchError error) {
        if (error == null) {
            Log.i("MyApp", "got my Branch link to share: " + url);
        }
    }
});
```
I hope this helps give you an idea of how to implement Branch in an Android app. Let me know if you have any questions.

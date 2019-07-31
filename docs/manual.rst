有道广告聚合SDK Android使用说明文档
========================================

简介
======

聚合sdk定义：可以聚合多方广告SDK，协调各广告位中的广告请求、展现等逻辑，使得多个SDK可以在一个广告位正常运作起来。

有道聚合SDK为海外app服务，提供了一种便捷的方式可以快速接入多家广告平台。目前聚合sdk集成了五种广告平台分别为Facebook、Admob、百度、有道智选、Inmobi、头条。

在移动广告开发者管理系统中提供聚合平台入口，平台中可以设置广告策略，聚合SDK根据配置的广告策略拉取各个广告平台的广告，提供给app最终可供展示的广告对象。

通过聚合SDK可以动态调整填充策略提高广告收益；提供快速的接入方式降低接入成本；通过聚合平台配置提供足够的灵活性，广告填充策略变化是无需重新发版，实现收入最大化。

SDK接入
=============

兼容性
----------------

支持设备：运行了Android4.0.3以及以上系统的Android 设备

集成前提
----------------

开始集成SDK之前开发者需要在\ `移动广告开发者管理系统 <http://witake.youdao.com/publisher>`__\ 中点击进入聚合SDK平台，按提示获取一个聚合广告位ID，以便在集成广告代码的时候使用。

SDK包导入
----------------

（1）自动集成，目前只支持内部媒体，支持nexus仓库

增加仓库

::

    //测试包
    maven { url "http://nexus.corp.youdao.com/nexus/content/repositories/ead_snapshot" }
    //正式包
    maven { url "http://nexus.corp.youdao.com/nexus/content/repositories/ead" }

gradle中增加对应的依赖包，需要依赖聚合SDK包，和需要的平台adapter包，具体根据需求依赖对应的包即可

::

   //聚合sdk包，必须依赖
   implementation 'com.youdao.sdk:mediation-ads:1.4.3-SNAPSHOT'

   //admob adapter包，集成admob广告则依赖该包，反之不用依赖
   implementation 'com.youdao.sdk:mediation-adapter-admob:17.2.0.1-SNAPSHOT'

   //facebook adapter包，集成facebook广告则依赖该包，反之不用依赖
   implementation 'com.youdao.sdk:mediation-adapter-facebook:5.7.1.1-SNAPSHOT'

   //inmobi adapter包，集成inmobi广告则依赖该包，反之不用依赖
   implementation 'com.youdao.sdk:mediation-adapter-inmobi:9.0.4.1-SNAPSHOT'

   //zhixuan adapter包，集成智选广告则依赖该包，反之不用依赖
   implementation 'com.youdao.sdk:mediation-adapter-zhixuan:4.1.4.1-SNAPSHOT'

   //百度adapter包，集成百度广告则依赖下面包，反之不用依赖
   implementation 'com.youdao.sdk:mediation-adapter-baidu:1.2.7.4.1-SNAPSHOT'
   implementation files('libs/DuappsAd-CW-1.2.7.4-release.aar')
   implementation files('libs/DuVideoSdk-v1.1.0-release.aar')

   //头条adapter包，集成头条广告则依赖下面包，反之不用依赖
   implementation 'com.youdao.sdk:mediation-adapter-toutiao:2.5.3.1-SNAPSHOT'
   implementation files('libs/open_ad_sdk.aar')

（2）inmobi广告SDK提示，接入inmobi SDK可能会导致超出64K方法数限时，建议支持多dex配置。

::

  defaultConfig {
    ...
    multiDexEnabled true // add this to enable multi-dex
  }

  depedencies {
    ...
    implementation 'com.android.support:multidex:1.0.1'
  }

AndroidManifest配置
--------------------

1. Admob APP_ID

::

  <meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="Admob APP_ID" />


2. 百度APP_ID

::

  <meta-data
    android:name="app_license"
    android:value="百度APP_ID" />

3. inmobi广告添加

::

  <meta-data android:name="com.google.android.gms.version"
  android:value="@integer/google_play_services_version" />

4. Facebook视频广告要求启用硬件加速渲染，否则可能会在观看时遇到黑屏。适用于所有类型视频广告。因此需要在增加如下配置：

::

  <application android:hardwareAccelerated="true" ...>
  //如果不希望整个应用层级开启硬件加速，可以给特定广告Activity开启硬件加速，如下所示
  <activity android:name="com.facebook.ads.AudienceNetworkActivity" android:hardwareAccelerated="true" .../>

代码混淆配置
----------------
如果您需要使用proguard混淆代码，需确保不要混淆SDK的代码。 请在proguard-rules.pro文件(或其他混淆文件)尾部添加如下配置:

::

  -keep class com.google.android.gms.ads.** {public *;}
  -keep class com.facebook.ads.NativeAd
  -keep class com.youdao.sdk.** { *;}
  -keep class com.duapps.ad.**{*;}
  -keep class com.youdao.admediationsdk.** { *;}
  -keep class com.inmobi.** { *; }

**注意:SDK代码被混淆后会导致广告无法展现或者其它异常**

SDK初始化
----------------

在完成聚合SDK接⼊操作之前，应⽤⾸先需要对聚合SDK做初始化。没有进⾏初始化的聚合⼴告位ID⽆法拉取⼴告。

1. 在asserts目录下面创建Json⽂件，将聚合广告位ID与默认的平台配置建⽴对应关系。具体格式如下：

::

    {
      "admob_app_id": "ca-app-pub-3940256099942544~3347511713",
      "inmobi_account_id": "ff923f527bb9451ca0023bc8e4104aeb",
      "config": [
        {
          "m_pid": "f50ea4b479bc58dad3bd6c92bff72145",
          "ad_type": "native",
          "cache_size": 3,
          "default": {
            "ad_source": "Admob",
            "placement_id": "ca-app-pub-3940256099942544/2247696110"
          }
        },
        {
          "m_pid": "2bc9a354762340bae25a117a89c6ba88",
          "ad_type": "interstitial",
          "default": {
            "ad_source": "Admob",
            "placement_id": "ca-app-pub-3940256099942544/1033173712"
          }
        },
        {
          "m_pid": "banner-mediation-pid",
          "ad_type": "banner",
          "default": {
            "ad_source": "Admob",
            "placement_id": "ca-app-pub-3940256099942544/6300978111"
          }
        },
        {
          "m_pid": "rewarded-video-mediation-pid",
          "ad_type": "rewarded_video",
          "default": {
            "ad_source": "Admob",
            "placement_id": "ca-app-pub-3940256099942544/5224354917"
          }
        }
      ]
    }

**注意：**

**（1）如果要接入admob广告必须要配置admob_app_id**

**（2）如果要接入inmobi广告必须要配置inmobi_account_id**

**（3）config中配置了聚合广告位ID和默认配置的对应关系。m_pid表示聚合广告位ID；ad_type表示广告类型，目前支持四种类型分别为：native、interstitial、banner、rewarded_video，配置为其他类型无效,并且配置的类型和聚合平台上对应位置的类型要保持一致；cache_size是原生广告可以配置的缓存个数，其他类型广告配置无效；每个聚合广告位ID必须有default配置，当未拉取到配置或配置失效时采用默认配置为聚合广告位拉取广告**

**（4）default配置中ad_source表示平台名称，取值范围为Facebook、Admob、Baidu、Zhixuan、Inmobi,大小写敏感；placement_id表示对应广告平台上面申请的广告位ID**

**（5）如果不希望通过静态创建json⽂件的⽅式进⾏初始化，可以直接创建符合json格式的字符串并传值**

2. 在application的OnCreate()⽅法中使⽤YdMediationSdk.initialize进行初始化。接口说明如下所示：

::

  public static void initialize(Context context, String adConfigJson)


=============== ========================================
         参数                     说明
=============== ========================================
    context                applicationContext

   adConfigJson        聚合广告位ID和默认广告配置对应关系
=============== ========================================

初始化示例代码如下所示：

::

  String defaultConfig = ConfigHelper.getConfigJsonStr(this, "test.json");
  YdMediationSdk.setLaunchChannel("TEST");
  YdMediationSdk.initialize(this, defaultConfig);

3. 填写投放渠道⽤以区分不同app投放渠道的数据，此接⼝可选择使⽤，不是必需。如果使用需要在initialize之前调用setLaunchChannel方法，如上代码所示。

4. 调试过程中可以通过调用此方法YoudaoMediationSdk.setLogDebugLevel(true)打开调试开关，上线前要关掉，防止日志打印过多。

聚合SDK原生广告
==================

原生广告可见demo中NativeAdSampleActivity示例进行接入。注意：聚合原生广告的所有调用请在主线程中调用。

广告拉取
-------------

1. 构建广告拉取对象YoudaoNativeAd，在 Activity 的整个生命周期内，只需使用一个YoudaoNativeAd对象，即可请求多个原生广告，但是每次加载成功的广告都需要在展示完成之后或者页面销毁的时候进行销毁，防止内存泄漏。

注意：需要activity context以及聚合广告位ID来作为参数，这两个参数为必选参数，否则广告无法拉取

注意：其他的请求参数通过一个map进行设置。如果不设置则采用默认配置。如果设置某个平台的请求参数，可以在map中加入参数的key和value，key可以从具体平台的loader中获取。构造代码如下所示：

注意：可以设置整个广告拉取的超时时间，表示最长需要多长时间去拉取广告，如果超时未返回广告则回调失败，目前超时默认时长为15秒，可设置（单位ms）

::

  Map<String, Object> adRequestParams = new HashMap<>();

  //zhixuan 原生广告参数，如果不设置，默认为Boolean.False
  //adRequestParams.put(ZhixuanNativeAdLoader.KEY_PARAMETER_UPLOAD_LAST_CREATIVE_ID, Boolean.TRUE);

  //admob 原生广告参数
  //adRequestParams.put(AdmobNativeAdLoader.KEY_PARAMETER_NATIVE_AD_OPTIONS, new NativeAdOptions.Builder().build());

  mNativeAdLoader = new YoudaoNativeAd(YoudaoParameter.builder()
         .context(this)
         //设置加载参数,不设置则为默认参数
         .extraParameters(extraParameters)
         //设置广告超时，不设置为默认值15s
         .adLoadTimeout(15000)
         .build();

2. 加载广告

开发者可根据产品需求，选择合适的时机获取广告数据。加载广告通过上一步定义的mNativeAdLoader对象，调用fillAd接口缓存广告和loadAd接口获取缓存广告或实时加载广告，方法申明如下所示：

::

  /**
   * 缓存广告
   */
  fillAd();

  /**
   * 加载广告
   */
  loadAd(@NonNull YoudaoAdLoadListener adLoadListener, YoudaoAdImpressionListener impressionEventListener, YoudaoAdClickEventListener clickEventListener);

调⽤ fillAd() 接⼝可以提前缓存⼴告，并且表示后续缓存池会自动填充广告，在 loadAd() ⼴告时如果缓存池中有广告则可更快速回调给用户。建议在⼴告展示的前置场景
调⽤该⽅法；

调用loadAd()表示要获取广告，广告通过adLoadListener回调给用户，同时loadAd可以设置展示回调和点击回调。

举例说明广告拉取场景方便大家理解：

场景1：直接调用loadAd，如app开屏页面显示广告，调用loadAd，发起异步加载广告请求，等待广告加载成功，渲染广告。

场景2：提前fillAd广告，需要广告时再loadAd，缩短了广告拉取的等待时间，如词典app中，进入首页后调用fillAd缓存查词页广告，在进入查词页面时需要显示广告则调用loadAd，此时之前fillAd已经缓存了广告，就可以直接取到广告去渲染，并且取完广告之后会马上补充缓存池；如果之前缓存广告失败，则此时会直接发起异步请求加载广告，等待广告回调，回调成功后进行渲染。

loadAd时必须设置YoudaoAdLoadListener，且不能为null，否则无法加载广告，接口如下所示：
::

  public interface YoudaoAdLoadListener {
      /**
       * 原生广告加载成功回调
       *
       * @param ad 广告ad
       */
      void onAdLoaded(BaseNativeAd ad);

      /**
       * 原生广告加载失败回调
       *
       * @param errorCode    平台返回的错误码或者聚合SDK返回的错误码
       * @param errorMessage 错误信息
       */
      void onAdLoadFailed(int errorCode, String errorMessage);
  }

广告点击事件监听回调接口如下所示，该接口为可选配置，如不需要监听点击则可不设置。

::

  public interface YoudaoAdClickEventListener {
      /**
       * 原生广告被点击回调
       */
      void onAdClicked();
  }

广告展示事件回调接口如下所示，该接口为可选配置，如不需要监听展示则可不设置。

::

  public interface YoudaoAdImpressionListener {
      /**
       * 原生广告被展示曝光回调
       */
      void onAdImpressed();
  }

3. 其他配置参数设置

智选全局配置参数可在拉取广告前单独设置，如视频播放参数，下载配置等等。

广告渲染
-------------

广告拉取成功后会通过YoudaoAdLoadListener中onAdLoaded回调将广告对象回调给开发者，开发者根据广告对象进行渲染。

聚合sdk提供两种可选的方式渲染广告：

1. 通过回调的广告对象方法getNativeAd可以获取到各个广告平台的广告对象，开发者根据各个广告平台要求自行渲染广告，获取平台广告对象代码如下所示；

::

  @@Override
  public void onAdLoaded(BaseNativeAd ad) {
      if (ad instanceof ZhixuanNativeAd) {
          //智选广告对象可自行渲染
          NativeResponse nativeResponse = (NativeResponse) ad.getNativeAd();
      } else if (ad instanceof AdmobNativeAd) {
          //admob广告对象可自行渲染
          UnifiedNativeAd admobAd = (UnifiedNativeAd) ad.getNativeAd();
      } else if (ad instanceof BaiduNativeAd) {
          //百度广告对象可自行渲染
          DuNativeAd duNativeAd = (DuNativeAd) ad.getNativeAd();
      } else if (ad instanceof FacebookNativeAd) {
          //facebook广告对象可自行渲染
          NativeAd nativeAd = (NativeAd) ad.getNativeAd();
      } else if(mAd instanceof InMobiNativeAd){
           //inmobi广告对象可自行渲染
           showInMobiAd((InMobiNativeAd) mAd);
      }
  }

2. 提供每个平台一个渲染器只要按照要求把resId配置进去自动渲染广告。如果渲染器不能满足需求，可以按照1方法中所示进行渲染。

（1）智选广告自动渲染

::

  //配置渲染render对象
  ZhixuanNativeAdRender adRender = new ZhixuanNativeAdRender(
                new ViewBinder.Builder(R.layout.zhixuan_native_ad)
                        .titleId(R.id.native_title)
                        .textId(R.id.native_text)
                        .mainImageId(R.id.native_main_image)
                        .iconImageId(R.id.native_icon_image).build(), false);
  //通过render对象创建View
  View adView = adRender.createAdView(getActivity(), mNativeAdContainer);

  //把广告view加载到container中
  mNativeAdContainer.removeAllViews();
  mNativeAdContainer.addView(adView);

  //调用渲染方法
  adRender.renderAdView(adView, ad.getNativeAd());

  //注册点击事件
  adRender.registerViewForInteraction(adView, new int[]{R.id.native_main_image}, ad.getNativeAd());

  //调用展示上报
  ad.recordImpression(adView);

（2）百度广告自动渲染

::

  //配置渲染render对象
  BaiduNativeAdRender adRender = new BaiduNativeAdRender(
                      new BaiduViewBinder.Builder(R.layout.baidu_big_ad_card)
                              .setTitleViewId(R.id.card_name)
                              .setIconViewId(R.id.card_icon)
                              .setStarRatingViewId(R.id.card_rating)
                              .setShortDescViewId(R.id.card__des)
                              .setCallToActionViewId(R.id.card_btn)
                              .setImageViewId(R.id.card_image)
                              .build()

  //通过render对象创建View
  View adView = adRender.createAdView(getActivity(), mNativeAdContainer);

  //把广告view加载到container中
  mNativeAdContainer.removeAllViews();
  mNativeAdContainer.addView(adView);

  //调用渲染方法
  adRender.renderAdView(adView, ad.getNativeAd());

  //注册点击事件
  adRender.registerViewForInteraction(adView, ad.getNativeAd());

  //调用展示上报
  ad.recordImpression(adView);

（3）admob广告自动渲染

::

  //配置渲染render对象
  AdmobNativeAdRender adRender = new AdmobNativeAdRender(
                  new AdmobViewBinder.Builder(R.layout.admob_ad_unified)
                          .setMediaViewId(R.id.ad_media)
                          .setHeadlineViewId(R.id.ad_headline)
                          .setBodyViewId(R.id.ad_body)
                          .setCallToActionViewId(R.id.ad_call_to_action)
                          .setIconViewId(R.id.ad_app_icon)
                          .setPriceViewId(R.id.ad_price)
                          .setStarRatingViewId(R.id.ad_stars)
                          .setStoreViewId(R.id.ad_store)
                          .setAdvertiserViewId(R.id.ad_advertiser)
                          .build()
          );

  //通过render对象创建View
  View adView = adRender.createAdView(getActivity(), mNativeAdContainer);

  //把广告view加载到container中
  mNativeAdContainer.removeAllViews();
  mNativeAdContainer.addView(adView);

  //调用渲染方法
  adRender.renderAdView(adView, ad.getNativeAd());

  //调用展示上报
  ad.recordImpression(adView);

（4）facebook广告自动渲染

::

  //配置渲染render对象
  AdmobNativeAdRender adRender = new AdmobNativeAdRender(
                new AdmobViewBinder.Builder(R.layout.admob_ad_unified)
                        .setMediaViewId(R.id.ad_media)
                        .setHeadlineViewId(R.id.ad_headline)
                        .setBodyViewId(R.id.ad_body)
                        .setCallToActionViewId(R.id.ad_call_to_action)
                        .setIconViewId(R.id.ad_app_icon)
                        .setPriceViewId(R.id.ad_price)
                        .setStarRatingViewId(R.id.ad_stars)
                        .setStoreViewId(R.id.ad_store)
                        .setAdvertiserViewId(R.id.ad_advertiser)
                        .build()
        );

  //通过render对象创建View
  View adView = adRender.createAdView(getActivity(), mNativeAdContainer);

  //把广告view加载到container中
  mNativeAdContainer.removeAllViews();
  mNativeAdContainer.addView(adView);

  //调用渲染方法
  adRender.renderAdView(adView, ad.getNativeAd());

  //注册点击事件
  adRender.registerViewForInteraction(adView, R.id.native_ad_media, R.id.native_ad_icon, new int[]{R.id.native_ad_title, R.id.native_ad_call_to_action}, ad.getNativeAd());

  //调用展示上报
  ad.recordImpression(adView);

（5）inmobi广告自动渲染

::

  //配置渲染render对象
  InMobiNativeAdRender adRender = new InMobiNativeAdRender(
                    new InMobiViewBinder.Builder(R.layout.inmobi_native_ad)
                            .setCallToActionViewId(R.id.adAction)
                            .setDescriptionViewId(R.id.adDescription)
                            .setIconViewId(R.id.adIcon)
                            .setRatingViewId(R.id.adRating)
                            .setTitleViewId(R.id.adTitle)
                            .setPrimaryViewId(R.id.adContent)
                            .build()
            );

  //通过render对象创建View
  View adView = adRender.createAdView(getActivity(), mNativeAdContainer);

  //把广告view加载到container中
  mNativeAdContainer.removeAllViews();
  mNativeAdContainer.addView(adView);

  //调用渲染方法
  adRender.renderAdView(adView, ad.getNativeAd());

  //设置点击事件
  adRender.registerViewForInteraction(adView, new int[]{R.id.adAction}, ad.getNativeAd());

  //调用展示上报
  ad.recordImpression(adView);

广告展示上报
-------------

原生广告广告都必须调用ad.recordImpression(adView)，如不调用可能会影响展示数据的监测。

资源释放
-------------

原生广告拉取对象YoudaoNativeAd和原生广告BaseNativeAd对象都需要在合适时机销毁,以便系统正确进行垃圾回收处理，防止内存泄漏。

原生广告拉取对象在Activity类或者Fragment类的onDestroy方法中调用destroy方法来销毁对象。销毁方法如下代码所示：

::

  if (mNativeAdLoader != null) {
       mNativeAdLoader.destroy();
       mNativeAdLoader = null;
  }

所有加载成功的BaseNativeAd广告对象当完成展示后或者页面销毁时，应该将其销毁，销毁方法如下所示。可参考demo实现，显示新广告之后，旧的广告就销毁掉，在页面销毁时将所有广告销毁。

::

  if (mNativeAd != null) {
        mNativeAd.destroy();
        mNativeAd = null;
  }

聚合SDK插屏广告
==================

插屏广告可见demo中InterstitialAdSampleActivity示例进行接入。注意：聚合插屏广告的所有调用请在主线程中调用。

广告拉取
-------------

1. 构建广告拉取对象YoudaoInterstitialAd。在Activity的整个生命周期内，只需使用一个YoudaoInterstitialAd对象，即可请求并展示多个插屏广告，但必须在一个广告展示完成之后才能加载下一个广告。

注意：需要activity context以及聚合广告位ID来作为参数，这两个参数为必选参数，否则广告无法拉取

注意：其他的请求参数通过一个map进行设置。如果不设置则采用默认配置。如果设置某个平台的请求参数，可以在map中加入参数的key和value，key可以从具体平台的loader中获取。构造代码如下所示：

注意：可以设置整个广告拉取的超时时间，表示最长需要多长时间去拉取广告，如果超时未返回广告则回调失败，目前超时默认时长为15秒，可设置（单位ms）

::

   Map<String, Object> extraParameters = new HashMap<>();

   //百度插屏广告参数，分为全屏广告和半屏广告，如果不设置，默认为Boolean.TRUE，表示全屏广告
   //extraParameters.put(BaiduInterstitialAd.KEY_PARAMETER_IS_SCREEN, Boolean.FALSE);

   //智选插屏广告参数，如果不设置，默认为ZhixuanInterstitialParameter.ZHIXUAN_SPLASH，表示开屏广告
   //extraParameters.put(ZhixuanInterstitialAd.KEY_PARAMETER_INTERSTITIAL, ZhixuanInterstitialParameter.ZHIXUAN_NORMAL);

   YoudaoParameter parameter = YoudaoParameter.builder()
            .context(this)
            .mediationPid(M_PID)
            //设置加载参数,不设置则为默认参数
            .extraParameters(extraParameters)
            //设置广告超时，不设置为默认值15s
            .adLoadTimeout(15000)
            .build();
   mInterstitialAd = new YoudaoInterstitialAd(parameter);

2.缓存广告，作用为填充缓存池，前提是缓存开关打开（服务端配置），建议在⼴告展示的前置场景调用。如果缓存池中有广告则可在用户需要广告时更快速回调广告给用户。

应用场景如：如词典app中，进入首页后调用fillAd缓存查词页广告，在进入查词页面时需要显示广告则调用loadAd，此时之前fillAd已经缓存了广告，就可以直接取到广告去渲染，并且取完广告之后会马上补充缓存池；如果之前缓存广告失败，则此时会直接发起异步请求加载广告，等待广告回调，回调成功后进行渲染。

::

  /**
   * 缓存广告
   */
  fillAd();

3. 加载插屏广告，取广告接口，调用该接口会获取缓存池广告或实时请求线上广告，参数为插屏广告对应的回调。

::

  /**
   * 加载插屏广告
   *
   * @param adListener 插屏广告回调
   */
  public void loadAd(@NonNull YoudaoInterstitialAdListener adListener);

插屏广告回调YoudaoInterstitialAdListener，接口如下所示：

::

    public interface YoudaoInterstitialAdListener {
        /**
         * 广告加载成功
         */
        void onInterstitialLoaded();

        /**
         * 广告加载失败
         *
         * @param errorCode    errorCode,平台返回的错误码或聚合SDK定义的错误码
         * @param errorMessage errorMessage,平台返回的错误信息或聚合SDK定义的错误信息
         */
        void onInterstitialFailed(int errorCode, String errorMessage);

        /**
         * 广告被展示
         */
        void onInterstitialShown();

        /**
         * 广告被点击
         */
        void onInterstitialClicked();

        /**
         * 广告被关闭
         */
        void onInterstitialDismissed();
    }

3. 重新加载广告，监听interstitialAdListener的onInterstitialDismissed方法，监听到该方法表示广告展示完成，可以加载新的插屏广告。

::

  YoudaoInterstitialAdListener interstitialAdListener = new YoudaoInterstitialAdListener() {

              ...

              @Override
              public void onInterstitialDismissed() {
                  CommonUtil.showToast("已关闭");
                  //在上一个插屏广告展示完成后才可以加载下一个插屏广告
                  mInterstitialAd.loadAds(mInterstitialListener);
              }
  }
  mInterstitialAd.loadAds(mInterstitialListener);

广告渲染
-------------

和原生广告不同，只需要调用show方法就可以，其中封装了对应的展示细节。如果不确定广告是否可以展示，可以调用isReady方法进行判断。

::

  mInterstitialAd.show();

资源释放
-------------

最后在Activity类或者Fragment类的onDestroy方法中调用mInterstitialAd的destroy方法来销毁对象，防止内存泄漏。如下代码所示：

::

  if(mInterstitialAd != null){
        mInterstitialAd.destroy();
        mInterstitialAd = null;
  }

聚合SDK横幅广告
==================

横幅广告可见demo中BannerAdSampleActivity示例进行接入。注意：聚合横幅广告的所有调用请在主线程中调用。

广告拉取
-------------

1. 初始化YoudaoBannerAdView，参数为activity context和聚合广告位ID和其他参数。

注意：需要activity context以及聚合广告位ID来作为参数，这两个参数为必选参数，否则广告无法拉取

注意：其他的请求参数通过一个map进行设置。如果不设置则采用默认配置。如果设置某个平台的请求参数，可以在map中加入参数的key和value，key可以从具体平台的loader中获取。构造代码如下所示：

注意：可以设置整个广告拉取的超时时间，表示最长需要多长时间去拉取广告，如果超时未返回广告则回调失败，目前超时默认时长为15秒，可设置（单位ms）


::

  Map<String, Object> extraParameters = new HashMap<>();

  //admob banner广告参数，如果不设置，默认为AdSize.BANNER
  extraParameters.put(AdmobBannerAd.KEY_PARAMETER_AD_SIZE, AdSize.BANNER);

  //facebook banner广告参数，如果不设置默认为AdSize.BANNER_HEIGHT_50
  extraParameters.put(FacebookBannerAd.KEY_PARAMETER_AD_SIZE, com.facebook.ads.AdSize.BANNER_HEIGHT_50);

   //inmobi banner广告参数
   extraParameters.put(InmobiBannerAd.KEY_PARAMETER_BANNER_PARAMETER
                  , InmobiBannerParameter.builder()
                          .width(960)
                          .height(150)
                          .build());

   mBannerAdView = new YoudaoBannerAdView(YoudaoParameter.builder()
                  .context(this)
                  .mediationPid(M_PID)
                  //设置加载参数,不设置则为默认参数
                  .extraParameters(extraParameters)
                  //设置广告超时，不设置为默认值15s
                  .adLoadTimeout(15000)
                  .build());

3. 设置加载广告回调，如下所示：

::

    mBannerAdView.setListener(new YoudaoBannerAdListener() {
            @Override
            public void onAdLoaded() {
                YoudaoLog.d(TAG, " onBannerAdLoaded , pid = %s", M_PID);
                loadComplete();
            }

            @Override
            public void onAdLoadFailed(int errorCode, String errorMessage) {
                YoudaoLog.d(TAG, " onBannerAdLoadFailed , pid = %s", M_PID);
                loadComplete();
            }

            @Override
            public void onAdClicked() {
                YoudaoLog.d(TAG, " onAdClicked , pid = %s", M_PID);
            }

            @Override
            public void onAdClosed() {
                YoudaoLog.d(TAG, " onAdClosed , pid = %s", M_PID);
            }

            @Override
            public void onAdImpressed() {
                YoudaoLog.d(TAG, " onAdImpression , pid = %s", M_PID);
            }
        });

4.添加mBannerAdView到布局中，该步骤必须在调用load方法之前，否则可能会导致某些平台加载广告失败。代码如下所示：

::

  RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
  layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
  ViewGroup nativeAdContainer = findViewById(R.id.native_ad_container);
  nativeAdContainer.addView(mBannerAdView, layoutParams);

5.加载横幅广告，代码如下所示。智选平台不支持横幅广告。并且每个平台都可设置横幅广告刷新频率，inmobi刷新频率在请求参数中配置，其他平台均为平台上做设置。

::

  mBannerAdView.loadAds();

广告渲染
-------------

和其他广告类型不同，横幅广告会自动渲染到mBannerAdView上。

资源释放
-------------

最后在Activity类或者Fragment类的onDestroy方法中调用广告对象的destroy方法来销毁对象，防止内存泄漏。如下代码所示：

::

  mBannerAdView.destroy();

聚合SDK激励视频广告
=======================

激励视频广告可见demo中RewardedVideoAdSampleActivity示例进行接入。注意：激励视频广告的所有调用请在主线程中调用。

广告拉取
-------------

1. 构建广告拉取对象YoudaoRewardedVideoAd，参数为Activity context以及激励视频的聚合广告位ID。在Activity的整个生命周期内，只需使用一个YoudaoRewardedVideoAd对象，即可请求并展示多个激励视频广告，但必须在一个广告展示完成之后才能加载下一个广告。

注意：需要activity context以及聚合广告位ID来作为参数，这两个参数为必选参数，否则广告无法拉取

注意：其他的请求参数通过一个map进行设置。如果不设置则采用默认配置。如果设置某个平台的请求参数，可以在map中加入参数的key和value，key可以从具体平台的loader中获取。构造代码如下所示：

注意：可以设置整个广告拉取的超时时间，表示最长需要多长时间去拉取广告，如果超时未返回广告则回调失败，目前超时默认时长为15秒，可设置（单位ms）

::

  Map<String, Object> extraParameters = new HashMap<>();

  //facebook 激励视频广告参数，如果不设置，默认为Boolean.TRUE
  extraParameters.put(FacebookRewardedVideoAd.KEY_PARAMETER_REWARD_DATA, new RewardData("userId", "currency"));

  mYoudaoRewardedVideoAd = new YoudaoRewardedVideoAd(YoudaoParameter.builder()
                            .context(this)
                            .mediationPid(M_PID)
                            //设置加载参数,不设置则为默认参数
                            .extraParameters(extraParameters)
                             //设置广告超时，不设置为默认值15s
                            .adLoadTimeout(15000)
                            .build());

2.缓存广告，作用为填充缓存池，前提是缓存开关打开（服务端配置），建议在⼴告展示的前置场景调用。如果缓存池中有广告则可在用户需要广告时更快速回调广告给用户。

应用场景如：如词典app中，进入首页后调用fillAd缓存查词页广告，在进入查词页面时需要显示广告则调用loadAd，此时之前fillAd已经缓存了广告，就可以直接取到广告去渲染，并且取完广告之后会马上补充缓存池；如果之前缓存广告失败，则此时会直接发起异步请求加载广告，等待广告回调，回调成功后进行渲染。

::

  /**
   * 缓存广告
   */
  fillAd();

3. 加载激励视频广告，取广告接口，调用该接口会获取缓存池广告或实时请求线上广告，参数为激励视频广告对应的回调。

::

  /**
   * 加载激励视频广告
   *
   * @param adListener 激励视频广告回调
   */
  public void loadAd(@NonNull YoudaoRewardedVideoAdListener rewardedVideoAdListener);

激励视频广告回调YoudaoRewardedVideoAdListener，接口如下所示：

::

    public interface YoudaoRewardedVideoAdListener {
        /**
         * 广告加载成功
         */
        void onAdLoaded();

        /**
         * 广告加载失败
         *
         * @param errorCode    errorCode,平台返回的错误码或聚合SDK定义的错误码
         * @param errorMessage errorMessage,平台返回的错误信息或聚合SDK定义的错误信息
         */
        void onAdLoadFailed(int errorCode, String errorMessage);

        /**
         * 广告被展示
         */
        void onAdImpression();

        /**
         * 激励视频播放完成
         */
        void onAdPlayComplete();

        /**
         * 广告被点击
         */
        void onAdClicked();

        /**
         * 广告被缓存，并非所有平台都会回调该接口，如果平台有该回调才回调
         */
        void onAdCached();

        /**
         * 广告被关闭
         */
        void onAdClosed();

        /**
         * 激励奖励激活
         *
         * @param extra 奖励extra信息，其中key根据平台不同
         */
        void onAdRewarded(@Nullable Map<Object, Object> extra);
    }

4. 重新加载广告，监听YoudaoRewardedVideoListener的onAdClosed方法，监听到该方法表示广告展示完成，可以加载新的激励视频广告。

::

  YoudaoRewardedVideoAdListener rewardVideoAdListener = new YoudaoRewardedVideoAdListener() {
              ...
              @Override
              public void onAdClosed() {
                   YoudaoLog.d("RewardedVideoAdSampleFragment onAdClosed");
                   //表示上一个广告展示完成，可以加载下一个广告
                   mYoudaoRewardedVideoAd.loadAds(mAdListener);
              }
  }
  mYoudaoRewardedVideoAd.loadAds(mAdListener);

广告渲染
-------------

和原生广告不同，只需要调用show方法就可以，其中封装了对应的展示细节。如果不确定广告是否可以展示，可以调用isReady方法进行判断。

::

  mYoudaoRewardedVideoAd.show();

资源释放
-------------

最后在Activity类或者Fragment类的onDestroy方法中调用mYoudaoRewardedVideoAd的destroy方法来销毁对象，防止内存泄漏。如下代码所示：

::

  if (mYoudaoRewardedVideoAd != null) {
        mYoudaoRewardedVideoAd.destroy();
        mYoudaoRewardedVideoAd = null;
  }

版本更新记录
=============

=============== ========  =====================
    上线日期      版本号        更新内容
=============== ========  =====================
2019.04.10       v1.0.0    聚合sdk支持原生广告
2019.07.31       v1.1.0    聚合sdk支持插屏、聚合sdk支持inmobi平台原生和插屏广告、更新平台sdk(百度、facebook和智选)
2019.09.24       v1.2.0    聚合sdk支持横幅广告和激励视频广告
2019.10.25       v1.3.0    聚合sdk原生广告支持缓存
2020.6.24        v1.4.1    聚合sdk插屏广告和激励视频广告支持缓存
2020.6.24        v1.4.2    聚合sdk adapter结构调整和其他需求调整
2020.6.24        v1.4.3    聚合sdk打点优化和analyzer接入
=============== ========  =====================
# FrameCode

FrameCode for android team

Latest Version is :1.4.0


<h3>How to use</h3>
<h4>In gradle you can set it in by a single line Code in your build.gradle:</h4>
<code>compile 'com.dahanis:foundation:1.4.0'</code>
<h4>像上面那样直接在gradle中的dependencies {...}的大括号内加入<br>
compile 'com.dahanis:foundation:1.4.0'
即可完成引入</h4>
#####集成步骤：
* 先在build.gradle文件中引入<code>compile 'com.dahanis:foundation:1.4.0'</code>
* 然后让app的application继承FoundationApplication就可以了

#一．网络部分

####网络请求框架
简介：基于volley进行二次开发的网络请求框架
<h4>示例代码</h4>

```java

    /**
     *         http://m.dahanis.com:24080/BasicService.asmx/GetVehicleLengthList?
     *         token=Zrmp6OJN8JilVNd66DSRntEQAzPtNXNdQzGUK8FDhavb9Lv%2BGSEmpqVvPXLgk0S00F1isuQY5R4%3D&userId=60
     */
    /**
     * 先定义地址
     */
    String url = "http://m.dahanis.com:24080/BasicService.asmx/GetVehicleLengthList";
    /**
     * 传入请求参数
     */
    RequestParams params = new RequestParams();
    /**
     * 支持headers的添加
     */
    Map<String, String> headers = new HashMap<>();
    headers.put("user-agent", "android-volley-dahanis-foundation-app");
    params.setHeaders(headers);
    
    params.putParams("token","Zrmp6OJN8JilVNd66DSRntEQAzPtNXNdQzGUK8FDhavb9Lv%2BGSEmpqVvPXLgk0S00F1isuQY5R4%3D");
    params.putParams("userId", "60");
    /**
     * 开始正式请求
     */
    AutoPrintHttpNetUtils.getData(url, params, new HttpNetUtils.RequestListener<BaseBean<List<String>>>() {

        @Override
        public void onSuccess(BaseBean<List<String>> listBaseBean) {
            /**
             * 此处返回你所需要的对象
             *  比如你需要一个Foo对象，那么只需要把RequestListener<>尖括号里的类型改成Foo就行了
             *  像这样：RequestListener<Foo>
             */
        }

        @Override
        public void onFailed(Throwable throwable) {
            /**
             * 此处处理你请求失败的情况
             */

        }

        @Override
        public void onFinished() {
            /**
             * 此处是不管你的请求是否成功都会进行调用的
             * 所以此处可以做收尾工作
             */
        }
    }).setTag(this.toString());//最后一行设置当前请求任务的tag，以后可以用这个tag进行取消任务操作
    
    
    @Override
    protected void onDestroy() {
        
        AutoPrintHttpNetUtils.cancelAll(this.toString());
        
        super.onDestroy();
    }
        
     记住：tag不能直接用activity和fragment本身的实例对象，否则会造成内存泄漏问题
     
```

使用DhHttpNetUtils同名方法，可以更全套化节省开发时间，内部集成了请稍候对话框和网络情况提示，让开发只专注于界面开发，从而无需关注网络相关内容

注：使用网络请求框架时，最好在Activity的生命周期的onDestroy方法中调用AutoPrintHttpNetUtils.cancelAll(Object tag)进行销毁取消未完成的请求操作，否则可能导致窗口溢出．


####网络图片加载框架
示例代码<br>

```java

   ImageNetLoader imageNetLoader = new ImageNetLoader();
   imageNetLoader.loadImage(imageView,imageUrl);
   
   /**
    *  在activity的onDestroy方法中选择性调用以下方法中的任一一个进行加载任务取消操作，避免内存泄漏
    */
   imageNetLoader.cancel(url);
   imageNetLoader.cancelAll();
   
```


上面方法中两个参数依次为对应的imageView和需要获取并设置的网络图片地址,后面两个方法为取消加载任务，当退出activity的时候要调用
有了这个图片加载框你再也不需要担心OOM了,里面还有更多图片加载的方法，等待你去探索

* 如果在ListView中加载图片需要fling状态不加载图片，静止或触摸滚动再去加载可以给listView设置BitmapOnScrollListener到ListView中

    ```  listView.setOnScrollListener(new BitmapOnScrollListener(imageNetLoader,true));  ```
    
    参数中</br>
    imageNetLoader: 为你listView用来加载图片用的ImageNetLoader实例</br>
    true:表示在触摸滚动时可以进行图片加载

####文件下载框架
简介：基于DownloadManager进行二次开发<br>
示例代码<br>
```java

    DownLoadUtil.getInstance().startADownloadTask(url, new DownloadListener() {
      @Override
      public void onLoadChange(int total, int currentSize, int state) {

          progressBar.setMax(total);

          progressBar.setProgress(currentSize);
          /**
           * 正在下载中
           */
          if (state == DownloadManager.STATUS_RUNNING) {


          }
          /**
           * 下载失败
           */
          else if (state == DownloadManager.STATUS_FAILED) {

              startDownLoad.setText("下载失败");

              reDownload.setText("重新下载");

          }
      }

      /**
       * 下载完成
       */
      @Override
      public void onComplete(long downloadId, String filePath) {
      
          startDownLoad.setText("下载完成");
          
          reDownload.setText("重新下载");
      }
    });
```


#二．控件(1.2版本中方可使用)
####AfkImageView

简介：作用于切换图片效果的控件，也可以自定义切换动画效果。

默认效果是透明度渐变切换效果，无需设置任何代码，直接调用setImage(image)即可；

若想自定义切换效果，实现TransitionAnimation接口，然后调用AfkImageView.setTransitionAnimation(TransitionAnimation animation)即可，<b>注意，必须在setImage(image)之前设置动画效果才会生效用于切换图片效果的控件<B>

####示例代码

            AfkImageView afkImageView = (AfkImageView) v.findViewById(R.id.ii_aiv);
            afkImageView.setImage(R.drawable.image1);
            afkImageView.setTransitionAnimationEnable(false);//关闭动画效果



####NetAfkImageView

简介：继承于AfkImageView，实现了类似唯品会app的网络图片加载效果。


####示例代码

            NetAfkImageView netAfkImageView = (NetAfkImageView) v.findViewById(R.id.ii_aiv);
            netAfkImageView.setImageUrl(url, R.drawable.error, R.drawable.default);
            
以上示例代码中，setImageUrl方法中三个参数依次为，网络图片地址，加载失败要显示的图片，默认显示图片．
也有更简单的方法直接设置url：

            netAfkImageView.setImageUrl(url);
            

####NetListView
简介：全自动获取网络数据并且到底自动加载更多ListView
####示例代码：
```java
    /**
     * 还是以以下的地址来进行说明，因些地址没有做分页功能，所以我们要专门的定制化下，若是有分页功能，则正常定制就可以了
     * http://m.dahanis.com:24080/BasicService.asmx/GetVehicleLengthList?
     * token=Zrmp6OJN8JilVNd66DSRntEQAzPtNXNdQzGUK8FDhavb9Lv%2BGSEmpqVvPXLgk0S00F1isuQY5R4%3D&userId=600000032&";
     */

    /**
     * 先定义基础地址
     */
    String baseAddress = "http://m.dahanis.com:24080/BasicService.asmx/GetVehicleLengthList";
    /**
     * 然后再实例一个相应的参数对象并设值
     */
    RequestParams params = new RequestParams();
    params.putParams("token", "Zrmp6OJN8JilVNd66DSRntEQAzPtNXNdQzGUK8FDhavb9Lv%2BGSEmpqVvPXLgk0S00F1isuQY5R4%3D");
    params.putParams("userId", "600000032");

    /**
     * 接着为ListView设置加载更多功能配置监听器
     */

    listView.setLoadMoreAbleListener(new NetListViewCompat.LoadMoreAbleListener<ReturnObj>() {
        @Override
        public boolean isLoadMoreAble(ReturnObj returnObj, List allListData) {
             /**
                * 此处处理的是每次请求加载完数据后来判断是否可以再进行加载更多功能，常规做法是判断returnObj里返回的数据列表条数与期望的是否一致
                * 比如如果我们有做分页功能，且每页请求数据是期望１０条，那么我们可以这么判断
                *  if (returnObj.getTruckBeanList().size() == 10) {
                *      return true;
                *  }
                *  当获得的数据条目数为１０的时候方可继续加载
                *
                *  然而我们并没有做分页，所有为了有继续加载的效果　在正确获取到值的时候我们就直接返回true，代表永远可以加载更多
                *
                *  当returnCode为１的时候代表服务器正确返回了
                */
               if (returnObj.getReturnCode().equals("1")) {
    
                   return true;
    
               } else {
    
                   return false;
               }
        }

        @Override
        public List<?> getLoadedData(ReturnObj returnObj) {
            /**
             * 此处返回获取的对象中的列表
             */
            return returnObj.getTruckBeanList();
        }
    });

    /**
     * 因为没有做分页，所以pageName分页名称我们就不传了
     */
    listView.initNetListView(baseAddress, params, new MBaseAdapter(), null);

    /**
     * OK,全部工作已经做完了，自动加载更多并自动获取网络数据的listView配制完成
     * 注:
     * adapter必须是继承自NetListViewBaseAdapter,且尖括号必须写上类型，第一个为返回对象类型，第二个为列表数据item类型
     * 像下面的MBaseAdapter一样
     *
     */


    }

    class MBaseAdapter extends NetListViewBaseAdapter<ReturnObj, TruckBean> {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = (TextView) getLayoutInflater().inflate(android.R.layout.simple_list_item_1, listView, false);
            textView.setText(getItem(position).getId() + "  " + getItem(position).getLengthValue());
            return textView;
        }
    }
```
######也可以直接在布局文件中指定当前ListView的加载更多footer和EmptyView
示例如下：
```xml
    <com.dh.foundation.widget.netlistview.NetListView
                    android:id="@+id/listview"
                    app:load_more_layout="@layout/loadmore"
                    app:empty_view_id="@id/load_image"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"/>
```
#其它

####ProgressDialogUtil－－全局性的等待对话框框架工具
简介：内有默认实现等待对话框，可以自定义对话框样式，全局性显示隐藏对话框api，当某些异常请求需要开启等待而有时候会有多个重复的请求时
多交调用ProgressDialogUtil．show()方法时，在界面上永远只会有一个等待框，不会造成多余的等待框，从而导致内存浪费
#####示例代码
```java       
        ProgressDialogUtil.showProgressDialog(this);
        ProgressDialogUtil.dismissProgressDialog();
```

####ToastUtils--全局性toast显示
简介：调用api类似ProgressDialogUtil

####SharedPreferenceManager--直接可以使用的SharedPreference配置操作类
简介：里面已经实现基本大多数常用设置(AppToken,userId,userName,phone,password,isLogin等等)，如有需要扩展，继承此类实现方法即可

#####示例代码
两种实例方法均可：

```java

    SharedPreferenceManager sharedPreferenceManager = FoundationManager.getSharedPreferenceManager();
    SharedPreferenceManager sharedPreferenceManager1= new SharedPreferenceManager();
```
使用方法：

```java

        sharedPreferenceManager.getAppToken();
        sharedPreferenceManager.setIsLogin(true);
                       ...
                        
```

####ActivityStackManager--activity的栈管理者
简介：像正常栈管理一样出栈入栈即可，同时提供了销毁全部activity的方法，可用于退出应用时调用

####ApplicationUtil--用于获取application的应用信息
简介：获取包括应用名称，id，meta-data，versionCode等信息

####AESEncryptUtil－－加密工具
简介：AES/ECB/PKCS5Padding形式加密工具类
#####示例代码
```java

    AESEncryptUtil instance = AESEncryptUtil.getInstance("your_key");
    String encrypt = instance.encrypt("clearText");
    
```
####InputUtils－－输入法软键盘控制工具
* hideKeyboard(View v):隐藏输入软键盘
* showKeyboard(View v):显示输入软键盘
* setupUISoftKeyBoardHideSystem(final View viewGroup):设置当前ViewGroup内输入隐藏逻辑，实现效果为在当前viewGroup内点击输入框外的区域隐藏输入法软键盘.<br>
  前提，当前ViewGroup内不能有控件设置onTouchListener

####AppDownLoaderWithNotification－－app版本更新升级下载工具
会启用一个通知栏进行下载，下载完成后自动打开提示用户安装

``` new AppDownLoaderWithNotification( versionInfo.getDownLoadUrl()).start(); ```

# FrameCode

Framecode for android team

Latest Version is :1.1.3


<h3>How to use</h3>
<h4>In gradle you can set it in by a single line Code in your build.gradle:</h4>
<code>compile 'com.dahanis:foundation:1.1.3'</code>
<h4>像上面那样直接在gradle中的dependencies {...}的大括号内加入
compile 'com.dahanis:foundation:1.1.3'
即可完成引入</h4>
#一．网络请求框架
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
    params.putParams("token","Zrmp6OJN8JilVNd66DSRntEQAzPtNXNdQzGUK8FDhavb9Lv%2BGSEmpqVvPXLgk0S00F1isuQY5R4%3D");
    params.putParams("userId", "60");
    /**
     * 开始正式请求
     */
    AutoPrintHttpNetUtils.getData(this, url, params, new HttpNetUtils.RequestListener<BaseBean<List<String>>>() {

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
    });
```

#网络图片加载框架
示例代码<br>
```  ImageNetLoader.loadImage(imageView,imageUrl); ```


上面方法中两个参数依次为对应的imageView和需要获取并设置的网络图片地址
有了这个图片加载框你再也不需要担心OOM了,里面还有更多图片加载的方法，等待你去探索


#二．控件(1.2版本中方可使用)
<h4>AfkImageView</h4>

简介：作用于切换图片效果的控件，也可以自定义切换动画效果。

默认效果是透明度渐变切换效果，无需设置任何代码，直接调用setImage(image)即可；

若想自定义切换效果，实现TransitionAnimation接口，然后调用AfkImageView.setTransitionAnimation(TransitionAnimation animation)即可，<b>注意，必须在setImage(image)之前设置动画效果才会生效用于切换图片效果的控件<B>

<h4>示例代码<h4>

            AfkImageView afkImageView = (AfkImageView) v.findViewById(R.id.ii_aiv);
            afkImageView.setImage(R.drawable.image1);
            afkImageView.setTransitionAnimationEnable(false);//关闭动画效果



####NetAfkImageView

简介：继承于AfkImageView，实现了类似唯品会app的网络图片加载效果。


<h4>示例代码<h4>

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
             *  然而我们并没有做分页，所有为了有继续加载的效果　我们就直接返回true，代表永远可以加载更多
             */


            return true;
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

##其它




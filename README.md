# FrameCode

Framecode for android team

Latest Version is :1.1.3


<h3>How to use</h3>
<h4>In gradle you can set it in by a single line Code in your build.gradle:</h4>
<code>compile 'com.dahanis:foundation:1.1.3'</code>
<h4>像上面那样直接在gradle中的dependencies {...}的大括号内加入compile 'com.dahanis:foundation:1.1.3'即可完成引入</h4>
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


#二．控件
<h4>AfkImageView</h4>

简介：作用于切换图片效果的控件，也可以自定义切换动画效果。

默认效果是透明度渐变切换效果，无需设置任何代码，直接调用setImage(image)即可；

若想自定义切换效果，实现ExcessiveAnimation接口，然后调用AfkImageView.setExcessiveAnimation(ExcessiveAnimation animation)即可，<b>注意，必须在setImage(image)之前设置动画效果才会生效用于切换图片效果的控件<B>

<h4>示例代码<h4>

            AfkImageView afkImageView = (AfkImageView) v.findViewById(R.id.ii_aiv);
            afkImageView.setImage(R.drawable.image1);
            afkImageView.setImage(R.drawable.image2);//第二次setImage就能看见渐变切换效果
            
            afkImageView.setExcessiveAnimationEnable(false);//关闭动画效果

   注：第一次调用setImage时看不到效果

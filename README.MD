![效果图](http://upload-images.jianshu.io/upload_images/1766763-afa28117f3c92791.gif?imageMogr2/auto-orient/strip)

  >#####实现流程

* 绘制一个从左到由逐渐填充的圆环
* 绘制一个从左到由逐渐变色的直线 ，可设置圆角
* 继承FrameLayout,根据传入的数据（几个节点）结合设置的数据，确定共有几个圆环和直线，然后计算并设置这些子View的大小和位置，如果文字，计算文字位置和显示文字的TextView大小
* 然后开始执行动画，每个节点的动画绘制完成都会有一个回调动作通知下一个节点执行动画


> ##### How to use

Gradle Dependency
```gradle
compile 'com.lhz:StateProgressView:1.0.1'
```
> #####Attributes

Declare it inside your layout XML file like this:
```xml
<com.lhz.stateprogress.StateProgressView 
   android:id="@+id/spv"
   android:layout_height="wrap_content"
   android:layout_width="match_parent"   
   app:color_circle_fill="#d81e06"   
   app:color_circle_stroke="#d81e06"  
   app:line_height="8dp"   
   app:is_line_radius="true" 
   app:radius_circle_fill="12dp"  
   app:radius_circle_stroke="16dp"
   app:state_height="48dp"   
   app:color_text_normal="#bfbfbf"   
   app:color_line_state="#d81e06"    
   app:color_line_normal="#dbdbdb"   
 />
```
> ##### Set value and Perform the animation

```java
final StateProgressView spv= (StateProgressView) findViewById(R.id.spv);
//下方带文字 设置节点集合 最后节点的index为4 直接执行动画 list是String 类型的 index要小于list的数量
spv.setItems(list, 4, 200);
//下方带文字 设置节点集合
spv.setItems(list);
//最后节点的index为2 每个节点动画时长为1000毫秒
spv.startAnim(2,1000);
//下方不带文字 设置共有四个节点 最后节点的index为2 每个节点动画时长为1000毫秒
spv.setItems(4);
spv.startAnim(2,1000);
```
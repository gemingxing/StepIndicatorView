StepIndicatorView (流程指示器)
-----------------------------


SnapShot
-------------------
![](ART/stepindicatorview.gif)


Sample
---------------------------
> Please look at the [app](https://github.com/gemingxing/StepIndicatorView/tree/master/app) module


StepUp
------------------------------
First, add jitpack in your build.gradle at the end of repositories:

```
repositories {
    // ...
    maven { url "https://jitpack.io" }
}
```
Then, add the library dependency:

```
    compile 'com.github.gemingxing:stepindicatorview:1.0.0'
```
Now go do some awesome stuff!





Xml Attributes
--------------------
|           Name         |        Description-    |      Default value     |
|------------------------|------------------------|------------------------|
|stepIndicatorViewOrientation|set orientation of view|horizontal|
|stepIndicatorViewTextList|set step text list of view||
|stepIndicatorViewCompletedPosition|set completed position of view||
|stepIndicatorViewBackgroundColor|set background color of view|#00988c|
|stepIndicatorViewCompletedIcon|set completed icon of the view||
|stepIndicatorViewUnCompleteIcon|set unComplete icon of the view||
|stepIndicatorViewDefaultIcon|set default icon of the view||
|stepIndicatorViewIcon|set icon of the view||
|........|......|.....|

Use HorizontalStepIndicatorView
----------------------------
> 这里会引用图片

in xml
``` xml

```

in code
``` java

```

Use VerticalStepIndicatorView
--------------------------
in xml
``` xml

```
in code
``` java

```

Use StepIndicatorView(without text)
----------------------------
in xml
``` xml

```
in code
``` java

```


End
---------
Like the control of friend can click on the star!


Licence
----------------
```
The MIT License (MIT)

Copyright (c) 2016 gmx

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
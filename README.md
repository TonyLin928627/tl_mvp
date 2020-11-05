# tl_mvp

The app is called pin_block, it was implemented with a mvp framework, which is included in a separate module named lib in the same projects.
Here are the introduction about the framework, hope they will save you time for reading my codes.

Apps done with this framework are separated into some units named “SCREEN”,
A screen includes the following components
1>A sub class of MvpActivity, which is a container of the screen. It holds a reference of a MvpPresenter, which also delegates the life cycle callbacks of the activity.
2> A sub class of MvpPresenter,  this is the presenter which works as coordinator amount MvpActiviy and MvpModel, MvpView
3> A sub class of MvpMode. This is the component that implement the specific features and business logics of the screen.
4> A sub class of MvpView, this is the view that used to represents the screen.
5> A sub class of DataBridge,  as its name, it is a bridge for data IO.
6> Dagger.kt, this is the definitions of dagger2, which defines the injection of the screen.


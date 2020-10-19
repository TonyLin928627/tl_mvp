package io.tl.mvp.lib.menu

abstract class MenuItem (
    val nameTxtResId: Int,
    val iconResId: Int,
    var OnClicked: ()->Unit,

    val menuItemType: MenuItemType
)

enum class MenuItemType{
    HOME,
    SIGN_OUT,
    OTHER,
}
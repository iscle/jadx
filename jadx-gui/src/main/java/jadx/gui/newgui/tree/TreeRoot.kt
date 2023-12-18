package jadx.gui.newgui.tree

import jadx.gui.utils.UiUtils

class TreeRoot(
		override val text: String,
) : JadxTreeNode() {
	override val icon = UiUtils.openSvgIcon("nodes/rootPackageFolder")
}

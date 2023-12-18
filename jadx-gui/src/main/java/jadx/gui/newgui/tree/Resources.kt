package jadx.gui.newgui.tree

import jadx.gui.utils.NLS
import jadx.gui.utils.UiUtils
import javax.swing.Icon

class Resources : JadxTreeNode() {
	override val text = NLS.str("tree.resources_title")
	override val icon = UiUtils.openSvgIcon("nodes/resourcesRoot")

	init {
		// TODO: add resources
	}
}

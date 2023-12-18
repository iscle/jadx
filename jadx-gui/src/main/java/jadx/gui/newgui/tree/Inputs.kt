package jadx.gui.newgui.tree

import jadx.gui.utils.NLS
import jadx.gui.utils.UiUtils

class Inputs : JadxTreeNode() {
	override val text = NLS.str("tree.inputs_title")
	override val icon = UiUtils.openSvgIcon("nodes/projectStructure")

	init {
		// TODO: add inputs
	}
}

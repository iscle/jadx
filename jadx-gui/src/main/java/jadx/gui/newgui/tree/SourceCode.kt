package jadx.gui.newgui.tree

import jadx.gui.utils.NLS
import jadx.gui.utils.UiUtils

class SourceCode : JadxTreeNode() {
	override val text = NLS.str("tree.sources_title")
	override val icon = UiUtils.openSvgIcon("nodes/packageClasses")
}

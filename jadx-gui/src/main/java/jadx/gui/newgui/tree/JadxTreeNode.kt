package jadx.gui.newgui.tree

import javax.swing.Icon
import javax.swing.tree.DefaultMutableTreeNode

open class JadxTreeNode : DefaultMutableTreeNode() {
	open val text: String? = null
	open val icon: Icon? = null
	open val toolTipText: String? = null
}

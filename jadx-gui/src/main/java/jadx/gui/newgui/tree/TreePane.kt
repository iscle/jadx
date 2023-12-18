package jadx.gui.newgui.tree

import java.awt.Component
import javax.swing.JScrollPane
import javax.swing.JTree
import javax.swing.ToolTipManager
import javax.swing.tree.DefaultTreeCellRenderer
import javax.swing.tree.DefaultTreeModel
import javax.swing.tree.TreeModel

class TreePane : JScrollPane() {
	private val tree: JTree
	private val treeModel: TreeModel

	var flattenPackages = false

	private val cellRenderer = object : DefaultTreeCellRenderer() {
		override fun getTreeCellRendererComponent(
				tree: JTree?,
				value: Any?,
				sel: Boolean,
				expanded: Boolean,
				leaf: Boolean,
				row: Int,
				hasFocus: Boolean
		): Component {
			val component = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus)

			if (value is JadxTreeNode) {
				text = value.text
				icon = value.icon
				toolTipText = value.toolTipText
			} else {
				// text and icon get overwritten by super
				toolTipText = null
			}

			return component
		}
	}

	init {
		val root = TreeRoot("This is the root object name")

		val inputs = Inputs()
		root.add(inputs)

		// If it has source code
		if (true) {
			val sourceCode = SourceCode()
			root.add(sourceCode)
		}

		// If it has resources
		if (true) {
			val resources = Resources()
			root.add(resources)
		}

		treeModel = DefaultTreeModel(root)
		tree = JTree(treeModel)
		tree.cellRenderer = cellRenderer
		ToolTipManager.sharedInstance().registerComponent(tree)
	    setViewportView(tree)
	}
}

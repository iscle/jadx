package jadx.gui.newgui

import javax.swing.JSplitPane

class CodePane : JSplitPane() {

	init {
	    setOrientation(HORIZONTAL_SPLIT)
		resizeWeight = CODE_PANE_RESIZE_WEIGHT
	}

	companion object {
		private const val CODE_PANE_RESIZE_WEIGHT = 0.5
	}
}

package jadx.gui.newgui

import jadx.core.Jadx
import jadx.gui.newgui.tree.TreePane
import jadx.gui.ui.ExceptionDialog
import jadx.gui.ui.HeapUsageBar
import jadx.gui.ui.action.ActionModel
import jadx.gui.ui.action.JadxGuiAction
import jadx.gui.utils.NLS
import jadx.gui.utils.UiUtils
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.AbstractAction
import javax.swing.Box
import javax.swing.JFrame
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JPanel
import javax.swing.JSplitPane
import javax.swing.JToolBar
import kotlin.system.exitProcess

class NewMainWindow : JFrame() {
	// Main components
	private val mainPanel = JPanel(BorderLayout())
	private val mainSplitPane = JSplitPane(JSplitPane.HORIZONTAL_SPLIT)
	private val bottomPanel = JPanel(BorderLayout())

	// GUI actions
	private val openAction = JadxGuiAction(ActionModel.OPEN, Runnable {  })
	private val openProjectAction = JadxGuiAction(ActionModel.OPEN_PROJECT, Runnable {  })
	private val addFilesAction = JadxGuiAction(ActionModel.ADD_FILES, Runnable {  })
	private val newProjectAction = JadxGuiAction(ActionModel.NEW_PROJECT, Runnable {  })
	private val saveProjectAction = JadxGuiAction(ActionModel.SAVE_PROJECT, Runnable {  })
	private val saveProjectAsAction = JadxGuiAction(ActionModel.SAVE_PROJECT_AS, Runnable {  })
	private val reloadAction = JadxGuiAction(ActionModel.RELOAD, Runnable {  })
	private val liveReloadAction = JadxGuiAction(ActionModel.LIVE_RELOAD, Runnable {  })
	private val saveAllAction = JadxGuiAction(ActionModel.SAVE_ALL, Runnable {  })
	private val exportAction = JadxGuiAction(ActionModel.EXPORT, Runnable {  })
	private val prefsAction = JadxGuiAction(ActionModel.PREFS, Runnable {  })
	private val exitAction = JadxGuiAction(ActionModel.EXIT, Runnable {  })
	private val syncAction = JadxGuiAction(ActionModel.SYNC, Runnable {  })
	private val textSearchAction = JadxGuiAction(ActionModel.TEXT_SEARCH, Runnable {  })
	private val classSearchAction = JadxGuiAction(ActionModel.CLASS_SEARCH, Runnable {  })
	private val commentSearchAction = JadxGuiAction(ActionModel.COMMENT_SEARCH, Runnable {  })
	private val gotoMainActivityAction = JadxGuiAction(ActionModel.GOTO_MAIN_ACTIVITY, Runnable {  })
	private val decompileAllAction = JadxGuiAction(ActionModel.DECOMPILE_ALL, Runnable {  })
	private val resetCacheAction = JadxGuiAction(ActionModel.RESET_CACHE, Runnable {  })
	private val deobfAction = JadxGuiAction(ActionModel.DEOBF, Runnable {  })
	private val showLogAction = JadxGuiAction(ActionModel.SHOW_LOG, Runnable {  })
	private val aboutAction = JadxGuiAction(ActionModel.ABOUT, Runnable {  })
	private val backAction = JadxGuiAction(ActionModel.BACK, Runnable {  })
	private val backVariantAction = JadxGuiAction(ActionModel.BACK_V, Runnable {  })
	private val forwardAction = JadxGuiAction(ActionModel.FORWARD, Runnable {  })
	private val forwardVariantAction = JadxGuiAction(ActionModel.FORWARD_V, Runnable {  })
	private val quarkAction = JadxGuiAction(ActionModel.QUARK, Runnable {  })
	private val openDeviceAction = JadxGuiAction(ActionModel.OPEN_DEVICE, Runnable {  })

	init {
		title = "jadx"
		iconImages = listOf(
				"/logos/jadx-logo-16px.png",
				"/logos/jadx-logo-32px.png",
				"/logos/jadx-logo-48px.png",
				"/logos/jadx-logo.png",
		).map { UiUtils.openImage(it) }
		contentPane = mainPanel
		minimumSize = Dimension(200, 150)

		defaultCloseOperation = DO_NOTHING_ON_CLOSE
		addWindowListener(object : WindowAdapter() {
			override fun windowClosing(e: WindowEvent?) {
				closeWindow()
			}
		})

		configureMenuBar()
		configureMainPanel()
		configureToolBar()
		configureMainSplitPane()
		configureBottomPanel()

		pack()
		isVisible = true
	}

	private fun configureMenuBar() {
		val fileMenu = JMenu(NLS.str("menu.file"))
		fileMenu.mnemonic = KeyEvent.VK_F
		fileMenu.add(openAction)
		fileMenu.add(openProjectAction)
		fileMenu.add(addFilesAction)
		fileMenu.addSeparator()
		fileMenu.add(newProjectAction)
		fileMenu.add(saveProjectAction)
		fileMenu.add(saveProjectAsAction)
		fileMenu.addSeparator()
		fileMenu.add(reloadAction)
//		file.add(liveReloadMenuItem)
//		renameMappings.addMenuActions(file)
		fileMenu.addSeparator()
		fileMenu.add(saveAllAction)
		fileMenu.add(exportAction)
		fileMenu.addSeparator()
//		file.add(recentProjects)
		fileMenu.addSeparator()
		fileMenu.add(prefsAction)
		fileMenu.addSeparator()
		fileMenu.add(exitAction)

		val viewMenu = JMenu(NLS.str("menu.view"))
		viewMenu.mnemonic = KeyEvent.VK_V
//		viewMenu.add(flatPkgMenuItem)
		viewMenu.add(syncAction)
//		viewMenu.add(heapUsageBarMenuItem)
//		viewMenu.add(alwaysSelectOpened)
//		viewMenu.add(dockLog)

		val navMenu = JMenu(NLS.str("menu.navigation"))
		navMenu.mnemonic = KeyEvent.VK_N
		navMenu.add(textSearchAction)
//		navMenu.add(clsSearchAction)
		navMenu.add(commentSearchAction)
		navMenu.add(gotoMainActivityAction)
		navMenu.addSeparator()
		navMenu.add(backAction)
		navMenu.add(forwardAction)

		val pluginsMenu = JMenu(NLS.str("menu.plugins"))
		pluginsMenu.mnemonic = KeyEvent.VK_P

		val toolsMenu = JMenu(NLS.str("menu.tools"))
		toolsMenu.mnemonic = KeyEvent.VK_T
		toolsMenu.add(decompileAllAction)
		toolsMenu.add(resetCacheAction)
//		toolsMenu.add(deobfMenuItem)
		toolsMenu.add(quarkAction)
		toolsMenu.add(openDeviceAction)

		val helpMenu = JMenu(NLS.str("menu.help"))
		helpMenu.mnemonic = KeyEvent.VK_H
		helpMenu.add(showLogAction)
		if (Jadx.isDevVersion()) {
			helpMenu.add(object : AbstractAction("Show sample error report") {
				override fun actionPerformed(e: ActionEvent) {
					ExceptionDialog.throwTestException()
				}
			})
		}
		helpMenu.add(aboutAction)

		val menuBar = JMenuBar()
		menuBar.add(fileMenu)
		menuBar.add(viewMenu)
		menuBar.add(navMenu)
		menuBar.add(pluginsMenu)
		menuBar.add(toolsMenu)
		menuBar.add(helpMenu)
		jMenuBar = menuBar
	}

	private fun configureMainPanel() {
		mainPanel.add(mainSplitPane, BorderLayout.CENTER)
		mainPanel.add(bottomPanel, BorderLayout.SOUTH)
	}

	private fun configureToolBar() {
		val toolBar = JToolBar()
		toolBar.isFloatable = false

		toolBar.add(openAction)
		toolBar.add(addFilesAction)
		toolBar.addSeparator()
		toolBar.add(reloadAction)
		toolBar.addSeparator()
		toolBar.add(saveAllAction)
		toolBar.add(exportAction)
		toolBar.addSeparator()
		toolBar.add(syncAction)
//		toolBar.add(flatPkgButton)
		toolBar.addSeparator()
		toolBar.add(textSearchAction)
//		toolBar.add(clsSearchAction)
		toolBar.add(commentSearchAction)
		toolBar.add(gotoMainActivityAction)
		toolBar.addSeparator()
		toolBar.add(backAction)
		toolBar.add(forwardAction)
		toolBar.addSeparator()
//		toolBar.add(deobfToggleBtn)
		toolBar.add(quarkAction)
		toolBar.add(openDeviceAction)
		toolBar.addSeparator()
		toolBar.add(showLogAction)
		toolBar.addSeparator()
		toolBar.add(prefsAction)
		toolBar.addSeparator()
		toolBar.add(Box.createHorizontalGlue())
//		toolBar.add(updateLink)

		mainPanel.add(toolBar, BorderLayout.NORTH)
	}

	private fun configureMainSplitPane() {
		val leftPanel = JPanel(BorderLayout())

		val treePane = TreePane()
		leftPanel.add(treePane, BorderLayout.CENTER)

		val codePane = CodePane()

		mainSplitPane.resizeWeight = MAIN_SPLIT_PANE_RESIZE_WEIGHT
		mainSplitPane.leftComponent = leftPanel
		mainSplitPane.rightComponent = codePane
	}

	private fun configureBottomPanel() {
		val heapUsageBar = HeapUsageBar()
		val versionLabel = VersionLabel()

		bottomPanel.add(heapUsageBar, BorderLayout.CENTER)
		bottomPanel.add(versionLabel, BorderLayout.EAST)
	}

	private fun closeWindow() {
		exitProcess(0)
	}

	companion object {
		private const val MAIN_SPLIT_PANE_RESIZE_WEIGHT = 0.15
	}
}

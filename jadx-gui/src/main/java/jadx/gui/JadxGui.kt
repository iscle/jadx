package jadx.gui

import jadx.cli.LogHelper
import jadx.core.utils.files.FileUtils
import jadx.gui.logs.LogCollector
import jadx.gui.settings.JadxSettingsAdapter
import jadx.gui.ui.ExceptionDialog
import jadx.gui.ui.MainWindow
import jadx.gui.utils.LafManager
import jadx.gui.utils.NLS
import jadx.gui.utils.SystemInfo
import org.slf4j.LoggerFactory
import java.awt.Desktop
import java.lang.invoke.MethodHandles
import javax.swing.SwingUtilities
import kotlin.system.exitProcess

private val LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())

fun main(args: Array<String>) {
	try {
		LogCollector.register()
		val settings = JadxSettingsAdapter.load()
		settings.logLevel = LogHelper.LogLevelEnum.INFO
		// overwrite loaded settings by command line arguments
		if (!settings.overrideProvided(args)) {
			return
		}
		LogHelper.initLogLevel(settings)
		LogHelper.setLogLevelsForDecompileStage()
		printSystemInfo()

		LafManager.init(settings)
		NLS.setLocale(settings.langLocale)
		ExceptionDialog.registerUncaughtExceptionHandler()
		SwingUtilities.invokeLater {
			val mw = MainWindow(settings)
			mw.init()
			registerOpenFileHandler(mw)
		}
	} catch (e: Exception) {
		LOG.error("Error: {}", e.message, e)
		exitProcess(1)
	}
}

private fun registerOpenFileHandler(mw: MainWindow) {
	try {
		if (Desktop.isDesktopSupported()) {
			val desktop = Desktop.getDesktop()
			if (desktop.isSupported(Desktop.Action.APP_OPEN_FILE)) {
				desktop.setOpenFileHandler { event ->
					mw.open(FileUtils.toPaths(event.files))
				}
			}
		}
	} catch (e: Exception) {
		LOG.error("Failed to register open file handler", e)
	}
}

private fun printSystemInfo() {
	if (LOG.isDebugEnabled) {
		LOG.debug("Starting jadx-gui. Version: '{}'. JVM: {} {}. OS: {} {}",
				SystemInfo.JADX_VERSION,
				SystemInfo.JAVA_VM, SystemInfo.JAVA_VER,
				SystemInfo.OS_NAME, SystemInfo.OS_VERSION)
	}
}

package jadx.gui.ui

import hu.akarnokd.rxjava2.swing.SwingSchedulers
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import jadx.gui.utils.NLS
import jadx.gui.utils.UiUtils
import org.slf4j.LoggerFactory
import java.awt.Color
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.concurrent.TimeUnit
import javax.swing.FocusManager
import javax.swing.JProgressBar

class HeapUsageBar : JProgressBar() {
    @Transient private val runtime = Runtime.getRuntime()
    @Transient private val focusManager = FocusManager.getCurrentManager()

    @Transient private var timer: Disposable? = null

	@Transient private val maxGB: Double
	@Transient private val limit: Long
	@Transient private val labelTemplate = NLS.str("heapUsage.text")

	init {
		isBorderPainted = false
		isStringPainted = true

		// Set runtime constants
		val maxMemory = runtime.maxMemory()
		maxGB = maxMemory / GB
		limit = maxMemory - UiUtils.MIN_FREE_MEMORY

		maximum = (maxMemory / 1024).toInt()
		foreground = GREEN

		addMouseListener(object : MouseAdapter() {
			override fun mouseClicked(e: MouseEvent) {
				Runtime.getRuntime().gc()
				update()
				if (LOG.isDebugEnabled) {
					LOG.debug("Memory used: {}", UiUtils.memoryInfo())
				}
			}
		})

		if (isVisible) {
			startTimer()
		}
	}

    override fun setVisible(enabled: Boolean) {
        super.setVisible(enabled)
        if (enabled) {
            startTimer()
        } else {
            reset()
        }
    }

    private fun startTimer() = synchronized(this) {
        if (timer != null) return

		update()
		timer = Flowable.interval(UPDATE_PERIOD_SECONDS, TimeUnit.SECONDS, Schedulers.newThread())
				.map { _ -> prepareUpdate() }
				.filter { it != SKIP_UPDATE }
				.distinctUntilChanged { a, b -> a.label == b.label }
				.subscribeOn(SwingSchedulers.edt())
				.subscribe(this::update)
    }

	fun reset() = synchronized(this) {
		if (timer == null) return
		timer!!.dispose()
		timer = null
	}

    private fun prepareUpdate(): UpdateData {
		// skip update if app window not active
        if (focusManager.activeWindow == null) return SKIP_UPDATE

		val used = runtime.totalMemory() - runtime.freeMemory()
		return UpdateData(
				value = (used / 1024).toInt(),
				label = String.format(labelTemplate, used / GB, maxGB),
				color = if (used > limit) RED else GREEN
		)
    }

    private fun update(update: UpdateData) {
		if (update == SKIP_UPDATE) return
        value = update.value
        string = update.label
		foreground = update.color
    }

    private fun update() {
		update(prepareUpdate())
    }

	data class UpdateData(
			val value: Int = 0,
			val label: String? = null,
			val color: Color? = null
	)

    companion object {
        private const val serialVersionUID = -8739563124249884967L

        private val LOG = LoggerFactory.getLogger(HeapUsageBar::class.java)

        private const val GB: Double = (1024L * 1024L * 1024L).toDouble()

        private val GREEN = Color(0, 180, 0)
        private val RED = Color(200, 0, 0)

		private val SKIP_UPDATE = UpdateData()

		private const val UPDATE_PERIOD_SECONDS: Long = 2
    }
}

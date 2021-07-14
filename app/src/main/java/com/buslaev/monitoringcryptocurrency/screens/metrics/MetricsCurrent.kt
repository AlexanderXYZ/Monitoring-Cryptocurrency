package com.buslaev.monitoringcryptocurrency.screens.metrics

import android.widget.TextView
import java.io.Serializable

data class MetricsCurrent(
    var currentChart: MetricsFragment.Range
):Serializable
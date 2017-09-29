package entity

import java.io.Serializable
import java.util.*

/**
 * POJO used by QuoteRequest Service.
 */
data class QuoteRequest(
        var senderName: String,
        var year: Date,
        var price: Double
) : Serializable
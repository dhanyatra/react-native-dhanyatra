package com.dhanyatra.rn

import com.facebook.react.bridge.*
import com.dhanyatra.checkout.*

public fun convertToPaymentOptions(options: ReadableMap): PaymentOptions {
    val key = options.getString("key") ?: ""
    val currency = options.getString("currency") ?: ""
    val amount = options.getString("amount") ?: ""

    val configMap = options.getMap("config")
    val displayMap = configMap?.getMap("display")

    val blocksArray = displayMap?.getArray("blocks")
    val blocks = blocksArray?.let { convertBlocks(it) } ?: emptyList()

    val sequenceArray = displayMap?.getArray("sequence")
    val sequence = sequenceArray?.toArrayList()?.map { it as String } ?: emptyList()

    val preferencesMap = displayMap?.getMap("preferences")
    val preferences = preferencesMap?.let {
        Preferences(
            show_default_blocks = it.getBoolean("show_default_blocks")
        )
    } ?: Preferences(show_default_blocks = false) // Default Preferences object

    val config = Config(
        display = Display(
            blocks = blocks,
            sequence = sequence,
            preferences = preferences
        )
    )

    val arkMap = options.getMap("ark")
    val ark = arkMap?.let {
        Ark(
            user_id = it.getString("user_id") ?: "",
            org_id = it.getString("org_id") ?: "",
            mode = it.getString("mode") ?: "",
            pay_complete = it.getBoolean("pay_complete")
        )
    }

    return PaymentOptions(
        key = key,
        currency = currency,
        amount = amount,
        config = config,
        ark = ark
    )
}

private fun convertBlocks(blocksArray: ReadableArray): List<Block> {
    val blocks = mutableListOf<Block>()

    for (i in 0 until blocksArray.size()) {
        val blockMap = blocksArray.getMap(i)
        val preferredMap = blockMap?.getMap("preferred")

        val instrumentsArray = preferredMap?.getArray("instruments")
        val instruments = instrumentsArray?.let { convertInstruments(it) } ?: emptyList()

        val preferred = preferredMap?.let {
            Preferred(
                name = it.getString("name") ?: "",
                instruments = instruments
            )
        } ?: Preferred(name = "", instruments = emptyList()) // Default Preferred object

        val block = Block(preferred = preferred)
        blocks.add(block)
    }

    return blocks
}

private fun convertInstruments(instrumentsArray: ReadableArray): List<Instrument> {
    val instruments = mutableListOf<Instrument>()

    for (i in 0 until instrumentsArray.size()) {
        val instrumentMap = instrumentsArray.getMap(i)

        val flowsArray = instrumentMap?.getArray("flows")
        val flows = flowsArray?.toArrayList()?.map { it as String } ?: emptyList()

        val appsArray = instrumentMap?.getArray("apps")
        val apps = appsArray?.toArrayList()?.map { it as String } ?: emptyList()

        val instrument = Instrument(
            method = instrumentMap?.getString("method") ?: "",
            flows = flows,
            apps = apps
        )
        instruments.add(instrument)
    }

    return instruments
}
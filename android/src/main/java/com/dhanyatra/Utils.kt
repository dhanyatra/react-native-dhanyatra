package com.dhanyatra.rn

import android.os.Parcelable
import com.facebook.react.bridge.*
import com.dhanyatra.checkout.*
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.declaredMemberProperties
import org.json.JSONArray
import org.json.JSONObject

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
            amount = it.getString("amount") ?: "",
            pay_complete = it.hasKey("pay_complete") && it.getBoolean("pay_complete")
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
        val preferredMap = blockMap.getMap("preferred")

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

        val flowsArray = instrumentMap.getArray("flows")
        val flows = flowsArray?.toArrayList()?.map { it as String } ?: emptyList()

        val appsArray = instrumentMap.getArray("apps")
        val apps = appsArray?.toArrayList()?.map { it as String } ?: emptyList()

        val instrument = Instrument(
            method = instrumentMap.getString("method") ?: "",
            flows = flows,
            apps = apps
        )
        instruments.add(instrument)
    }

    return instruments
}

public fun jsonToWritableMap(data: Any): WritableMap {
    val writableMap = WritableNativeMap()

    // Iterate through declared fields
    data::class.java.declaredFields.forEach { field ->
        field.isAccessible = true
        val name = field.name
        val value = field.get(data)

        // Skip 'CREATOR' field
        if (name == "CREATOR") {
            return@forEach
        }

        // Check if value is null
        if (value == null) {
            writableMap.putNull(name)
            return@forEach
        }

        // Handle Parcelable objects
        if (value is Parcelable) {
            writableMap.putMap(name, jsonToWritableMap(value))
            return@forEach
        }

        // Handle primitive types and strings
        when (value) {
            is String -> writableMap.putString(name, value)
            is Int -> writableMap.putInt(name, value)
            is Double -> writableMap.putDouble(name, value)
            is Float -> writableMap.putDouble(name, value.toDouble())
            is Boolean -> writableMap.putBoolean(name, value)
            is Long -> writableMap.putDouble(name, value.toDouble())

            // Handle nested JSONObject
            is JSONObject -> {
                try {
                    val jsonMap = jsonToWritableMap(value)
                    writableMap.putMap(name, jsonMap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            // Handle other cases
            else -> {
                try {
                    writableMap.putString(name, value.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    return writableMap
}

private fun jsonToWritableArray(data: List<*>): WritableArray {
    val writableArray = WritableNativeArray()
    data.forEach { value ->
        when (value) {
            is String -> writableArray.pushString(value)
            is Int -> writableArray.pushInt(value)
            is Double -> writableArray.pushDouble(value)
            is Float -> writableArray.pushDouble(value.toDouble())
            is Boolean -> writableArray.pushBoolean(value)
            is Long -> writableArray.pushDouble(value.toDouble())
            is Parcelable -> writableArray.pushMap(jsonToWritableMap(value))
            is JSONObject -> writableArray.pushMap(jsonToWritableMap(value))
            null -> writableArray.pushNull()
            else -> throw IllegalArgumentException("Unsupported type: ${value?.javaClass?.name}")
        }
    }
    return writableArray
}
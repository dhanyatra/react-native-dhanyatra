package com.dhanyatra.rn

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.dhanyatra.checkout.*
import com.dhanyatra.rn.*

import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule


class DhanyatraModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext), PaymentResultListener, ActivityEventListener {

  private var paymentSDK: PaymentSDK? = null

  init {
      reactContext.addActivityEventListener(this)
  }

  override fun getName(): String {
    return NAME
  }

  @ReactMethod
    fun startPayment(options: ReadableMap) {
        val activity = currentActivity
        try {
            // Convert ReadableMap to PaymentOptions
            val paymentOptions = convertToPaymentOptions(options)

            if(activity != null){
            // Initialize PaymentSDK
            paymentSDK = PaymentSDK(activity, paymentOptions, this)

            // Start Payment
            paymentSDK?.startPayment()
            }

        } catch (e: Exception) { }
    }

  override fun onPaymentSuccess(dhanyatraPaymentId: String?, paymentData: PaymentSuccessData) {
        val params = Arguments.createMap()
        params.putString("status", "success")
        params.putString("paymentId", dhanyatraPaymentId)
        params.putMap("data", Arguments.createMap()) // Add paymentData details
        sendEvent("Dhanyatra::PAYMENT_SUCCESS", params)
        paymentSDK?.close()
    }

    override fun onPaymentError(dhanyatraPaymentId: String?, response: PaymentFailureData?) {
        val params = Arguments.createMap()
        params.putString("status", "error")
        params.putString("paymentId", dhanyatraPaymentId)
        params.putMap("data", Arguments.createMap()) // Add response details
        sendEvent("Dhanyatra::PAYMENT_ERROR", params)
        paymentSDK?.close()
    }

    override fun onDismiss() {
        //val params = Arguments.createMap()
        //params.putString("status", "dismissed")
        //sendEvent("Dhanyatra:PAYMENT_DISMISS", params)
        paymentSDK?.close()
    }

    private fun sendEvent(eventName: String, params: WritableMap?) {
        reactApplicationContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(eventName, params)
    }

    override public fun onActivityResult(activity: Activity?, requestCode: Int, resultCode: Int, data: Intent?) {
        // Handle the activity result here
    }

    override fun onNewIntent(intent: Intent?) {
        // Handle new intent here
    }

  companion object {
    const val NAME = "RNDhanyatraCheckout"
  }
}

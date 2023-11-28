package com.example.mystamp.utils

import android.app.Activity
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity

object QRHelper {
    class AnyOrientationCaptureActivity : CaptureActivity() {
        //QR 코드 스캐너 화면 회전 관련
    }
    // QR 코드 스캐너 실행 함수
    fun scanQRCode(activity: Activity?) {
        val integrator = IntentIntegrator(activity)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("QR Code Scanner")
        integrator.captureActivity = AnyOrientationCaptureActivity::class.java // //QR 코드 스캐너 회전 관련
        integrator.setOrientationLocked(true) // 스캔 화면 세로 고정
        integrator.setBeepEnabled(false)    //  스캔 시 소리 끔
        integrator.initiateScan()
    }

}
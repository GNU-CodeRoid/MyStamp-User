package com.example.mystamp.utils

import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity

class QRHelper(private val activity: ComponentActivity, private val onScanResult: (String) -> Unit) {
    class AnyOrientationCaptureActivity : CaptureActivity() {
        //QR 코드 스캐너 화면 회전 관련
    }

    private val qrCodeForResult =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data: Intent? = result.data

            if (resultCode == Activity.RESULT_OK) {
                // QR 코드 스캔 결과 처리
                val scanResult = IntentIntegrator.parseActivityResult(resultCode, data)
                val scannedContent = scanResult.contents
                // 이제 scannedContent를 사용할 수 있습니다.
                onScanResult.invoke(scannedContent)
            }

        }

    // QR 코드 스캐너 실행 함수
    fun scanQRCode() {
        val integrator = IntentIntegrator(activity)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("QR Code Scanner")
        integrator.captureActivity =
            AnyOrientationCaptureActivity::class.java // //QR 코드 스캐너 회전 관련
        integrator.setOrientationLocked(true) // 스캔 화면 세로 고정
        integrator.setBeepEnabled(false)    //  스캔 시 소리 끔
        qrCodeForResult.launch(integrator.createScanIntent())
    }
}
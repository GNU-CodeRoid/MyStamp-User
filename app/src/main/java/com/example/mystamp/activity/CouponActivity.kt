package com.example.mystamp.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.imageLoader
import com.example.mystamp.dto.Coupon
import com.example.mystamp.ui.theme.MyStampTheme
import com.example.mystamp.viewmodel.CouponViewModel
import com.example.mystamp.viewmodel.MainViewModel

class CouponActivity : ComponentActivity() {

    private val _viewModel = CouponViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyStampTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CouponScreen()
                }
            }
        }
    }
    @Composable
    private fun CouponScreen(){

        val fetchCouponTrigger = _viewModel.fetchTrigger
        val coupons = _viewModel.coupons
        LaunchedEffect(fetchCouponTrigger) {
            try {
                _viewModel.fetchCoupons()
                Log.d("coupon", " 쿠폰 크기: ${coupons.size}")
            } catch (e: Exception) {
                Log.e("NetworkError", "쿠폰 가져오기 오류", e)
            }
        }
        LazyColumn(
            modifier = Modifier.padding(8.dp)
        ){
            items(coupons) {coupon->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Icon(
                            modifier= Modifier.size(40.dp),
                            imageVector = Icons.Filled.QrCode,
                            contentDescription = "쿠폰"
                        )
                        Column(
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Text(
                                coupon.shopId.shopName,
                                modifier = Modifier,
                                fontSize = 30.sp
                            )
                            Text(
                                fontSize = 12.sp,
                                text = "쿠폰 코드: ${coupon.couponCode}")

                        }
                    }

                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun PreviewCouponScreen(){
        CouponScreen()
    }
}


package com.example.mystamp.activity

import android.graphics.fonts.FontStyle
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.mystamp.ui.theme.MainYellow
import com.example.mystamp.ui.theme.MyStampTheme
import com.example.mystamp.viewmodel.CouponViewModel

class CouponActivity : ComponentActivity() {

    private val _viewModel = CouponViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyStampTheme {
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

        val fetchCouponTrigger = _viewModel.isFetchTrigger
        val coupons = _viewModel.coupons
        // 다이얼로그 표시 상태를 관리하는 MutableState
        val isShowDialog = _viewModel.isShowDialog
        val isShowCheckDialog = _viewModel.isShowCheckDialog

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
            itemsIndexed(coupons) {index ,coupon ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .clickable {
                            _viewModel.showDialog(index)
                        }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {

                        Icon(
                            modifier= Modifier.size(80.dp),
                            imageVector = Icons.Filled.LocalActivity,
                            tint = MainYellow,
                            contentDescription = "쿠폰"
                        )
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically){
                            Text(
                                coupon.shopId.shopName,
                                modifier = Modifier,
                                fontSize = 30.sp
                            )
                            Text(
                                coupon.shopId.couponCategory
                            )
                        }



                    }

                }
            }
        }
        if(isShowDialog){
            CouponDialog()
            if(isShowCheckDialog){
                CouponCheckDialog()
            }
        }
    }

    @Composable
    private fun CouponDialog(){
        val currentIndex = _viewModel.currentIndex
        val coupon = _viewModel.coupons[currentIndex]
        Dialog(onDismissRequest = { _viewModel.closeDialog() }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp)) // 요소 사이에 여백을 추가합니다.
                    Text(
                        text = coupon.shopId.shopName,
                        style = MaterialTheme.typography.titleLarge//
                    )
                    Spacer(modifier = Modifier.height(16.dp)) // 요소 사이에 여백을 추가합니다.
                    Text(
                        text = coupon.shopId.couponDescription,
                        style = MaterialTheme.typography.bodyLarge//
                    )
                    Spacer(modifier = Modifier.height(16.dp)) // 요소 사이에 여백을 추가합니다.
                    Text(
                        fontSize = 12.sp,
                        text = "쿠폰 코드:")
                    Spacer(modifier = Modifier.height(4.dp)) // 요소 사이에 여백을 추가합니다.
                    Text(
                        fontSize = 12.sp,
                        text = coupon.couponCode
                    )
                    Spacer(modifier = Modifier.height(16.dp)) // 요소 사이에 여백을 추가합니다.
                    Button( // QR 코드를 스캔하기 위한 버튼입니다.
                        onClick = {
                            _viewModel.showCheckDialog()
                        },
                        modifier = Modifier
                            .widthIn(max = 200.dp)
                            .heightIn(max = 50.dp)
                    ) {
                        Text( // 버튼에 표시되는 텍스트입니다.
                            "쿠폰 사용"
                        )

                    }

                }

            }
        }
    }

    @Composable
    private fun CouponCheckDialog(){
        val currentIndex = _viewModel.currentIndex
        Dialog(onDismissRequest = { _viewModel.closeCheckDialog() }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(4.dp)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp)) // 요소 사이에 여백을 추가합니다.
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = "경고",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red,
                        )
                        Column(
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                fontSize = 13.sp,
                                text = "쿠폰 사용시 가게 사장님만 눌러주시길 바랍니다"
                            )
                            Spacer(modifier= Modifier.height(4.dp))
                            Text(
                                fontSize = 13.sp,
                                text = "* 사용하면 되돌릴 수 없습니다!!",
                                color = Color.Red,
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(16.dp)) // 요소 사이에 여백을 추가합니다.
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        Button( // QR 코드를 스캔하기 위한 버튼입니다.
                            onClick = {
                                _viewModel.deleteCoupon()
                                Toast.makeText(
                                    this@CouponActivity,
                                    "쿠폰 사용",
                                    Toast.LENGTH_SHORT)
                                    .show()
                            },
                            modifier = Modifier
                                .widthIn(max = 200.dp)
                                .heightIn(max = 50.dp)
                        ) {
                            Text( // 버튼에 표시되는 텍스트입니다.
                                "예",
                            )

                        }
                        Button( // QR 코드를 스캔하기 위한 버튼입니다.
                            onClick = {
                                _viewModel.closeCheckDialog()
                            },
                            modifier = Modifier
                                .widthIn(max = 200.dp)
                                .heightIn(max = 50.dp)
                        ) {
                            Text( // 버튼에 표시되는 텍스트입니다.
                                "아니요",
                            )

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

